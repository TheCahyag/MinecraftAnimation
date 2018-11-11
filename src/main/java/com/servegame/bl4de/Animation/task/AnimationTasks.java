package com.servegame.bl4de.Animation.task;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * File: AnimationTasks.java
 * AnimationTasks is a class to keep track of all the {@link FrameDisplayTask}s
 * that are created for one {@link Animation}.
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class AnimationTasks {

    private Animation animation;
    private List<FrameDisplayTask> tasks;

    /**
     * Constructor for AnimationTasks
     * @param animation - the {@link Animation}
     */
    AnimationTasks(Animation animation){
        this.animation = animation;
        this.tasks = new ArrayList<>();
    }

    /**
     * PopulateTaskList will start a new task for each {@link Frame} of the {@link Animation}and give
     * it to {@link Sponge} to execute
     */
    void populateTaskList(){
        List<Frame> frames = this.animation.getFrames();

        // Prune out frames that don't have content
        frames.removeIf(frame -> !frame.getSubspace().getContents().isPresent());

        long initDelay = 0;
        long periodBetweenRepeat = this.animation.getTickDelay() * frames.size();
        for (int i = 0; i < frames.size(); i++, initDelay += this.animation.getTickDelay()) {
            // Create a FrameDisplayTask that will be used for the Task
            Frame frame = frames.get(i);
            FrameDisplayTask frameDisplayTask =
                    new FrameDisplayTask(frame, this.animation.getTickDelay(), i, this.animation.getCycles());

            // Create a task for each frame giving proper delay times
            Task.Builder taskBuilder = Task.builder()
                    .delayTicks(initDelay)
                    .intervalTicks(periodBetweenRepeat)
                    .execute(frameDisplayTask);
            Task task = taskBuilder.submit(AnimationPlugin.plugin);
            frameDisplayTask.setTask(task);
            frameDisplayTask.setParent(this);
            this.tasks.add(frameDisplayTask);
        }
    }

    /**
     * StopAll will cancel all {@link Task}s
     * that were created for this {@link Animation}
     */
    void stopAll(){
        this.setAnimationStatusToStopped();
        for (FrameDisplayTask task :
                this.tasks) {
            if (AnimationPlugin.instance.isDebug()){
                System.out.println("Stopping: " + task.getTask());
            }
            if (task.getStatus().equals(FrameTask.STATUS.RUNNING)){
                // Only cancel the task if it's running
                task.cancel();
            }
        }
    }

    private void setAnimationStatusToStopped(){
        AnimationController.updateAnimationStatus(this.animation, Animation.Status.STOPPED);
    }

    /**
     * If a {@link FrameDisplayTask} cancels itself, it will call this method
     */
    void reportCancel(){
        int runningTask = 0;
        for (FrameDisplayTask task :
                this.tasks) {
            if (task.getStatus().equals(FrameTask.STATUS.RUNNING)){
                runningTask++;
            }
        }
        if (runningTask == 0){
            // If there are no tasks running, tell the task manager
            AnimationPlugin.taskManager.stopAnimation(this.animation);
        }
    }

    /**
     * Getter for the {@link Animation}'s name
     * @return String - name of the animation
     */
    public String getAnimationName(){
        return this.animation.getAnimationName();
    }

}
