package com.servegame.bl4de.Animation.task;

import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import org.spongepowered.api.scheduler.SpongeExecutorService;
import org.spongepowered.api.scheduler.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * File: AnimationTasks.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class AnimationTasks {

    protected Animation animation;
    private List<Task> tasks;
    private SpongeExecutorService syncExecutorService;

    /**
     * Constructor for AnimationTasks
     * @param animation - the {@link Animation}
     */
    public AnimationTasks(Animation animation, SpongeExecutorService spongeExecutorService){
        this.animation = animation;
        this.syncExecutorService = spongeExecutorService;
        this.tasks = new ArrayList<>();
    }

    /**
     * PopulateTaskList will start a new task for each {@link Frame} of the {@link Animation}and give
     * it to {@link org.spongepowered.api.Sponge} to execute
     */
    private void populateTaskList(){
        List<Frame> frames = this.animation.getFrames();
        long initDelay = 0;
        long periodBetweenRepeat = this.animation.getTickDelay() * frames.size();
        for (int i = 0; i < frames.size(); i++, initDelay += this.animation.getTickDelay()) {
            // Create a FrameDisplayTask that will be used for the Task
            Frame frame = frames.get(i);
            FrameDisplayTask frameDisplayTask =
                    new FrameDisplayTask(frame, this.animation.getTickDelay(), i);

            // Create a task for each frame giving proper delay times
            SpongeExecutorService.SpongeFuture tmp = syncExecutorService.scheduleAtFixedRate(
                    frameDisplayTask,
                    ticksToMilliseconds(initDelay),
                    ticksToMilliseconds(periodBetweenRepeat),
                    TimeUnit.MILLISECONDS);
            this.tasks.add(tmp.getTask());
        }
    }

    /**
     * StopAll will cancel all {@link Task}s
     * that were created for this {@link Animation}
     */
    public void stopAll(){
        for (Task task :
                this.tasks) {
            task.cancel();
        }
    }

    /**
     * Convert Minecraft Ticks to milliseconds
     * @param ticks - number of ticks
     * @return - ticks in milliseconds
     */
    private long ticksToMilliseconds(long ticks) {
        return ticks * 50L;
    }

}
