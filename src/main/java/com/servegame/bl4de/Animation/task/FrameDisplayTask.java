package com.servegame.bl4de.Animation.task;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.scheduler.Task;

import java.util.Optional;

import static com.servegame.bl4de.Animation.task.FrameTask.STATUS.CANCELLED;
import static com.servegame.bl4de.Animation.task.FrameTask.STATUS.RUNNING;

/**
 * File: FrameDisplayTask.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class FrameDisplayTask implements FrameTask {

    /** The Frame we will be displaying */
    private Frame frame;

    /** Delay between the frames (in ticks) */
    private int tickDelay;

    /** The index of the Frame in the order in which they are displayed */
    private int index;

    /** Number of times to display this frame, effectively the number of times the animation will cycle */
    private int cyclesToRun;

    /** Number of times the frame has been displayed */
    private int cyclesRan;

    /** Reference to the {@link Task} from the Sponge Scheduler */
    private Task me;

    /** Status of this FrameDisplayTask */
    private STATUS status;

    /** Reference to the object that contains the collection of FrameDisplayTasks */
    private AnimationTasks parent;

    public FrameDisplayTask(Frame frame, int tickDelay, int index, int cyclesToRun){
        this.frame = frame;
        this.tickDelay = tickDelay;
        this.index = index;
        this.cyclesToRun = cyclesToRun;
        this.cyclesRan = 0;
        this.status = RUNNING;
    }

    @Override
    public void run() {
        boolean debug = AnimationPlugin.instance.isDebug();
        if (debug) {
            this.debugInfo();
        }
        this.modifyWorld();
        this.cyclesRan++;

        // -1 means the animation should run for ever
        if (cyclesToRun != -1 && cyclesToRun <= cyclesRan){
            this.cancel();
        }
    }

    @Override
    public void modifyWorld() {
        try {
            Util.copySubSpaceToWorld(this.frame.getSubspace());
        } catch (UninitializedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void debugInfo() {
        String messageToPrint   = "\n********************************* DEBUG   START  ********************************\n";
        messageToPrint          +=  this.frame.toString();
        messageToPrint          +=  this.toString();
        messageToPrint          +=  "********************************* DEBUG     END  ********************************";
        System.out.println(messageToPrint);
    }

    @Override
    public String toString() {
        return "********************************  TASK SETTINGS   *******************************\n" +
                "Tick Delay: " + this.tickDelay + "\n" +
                "Animation Index: " + this.index + "\n" +
                "Current System Time: " + System.currentTimeMillis() + "\n";
    }

    /**
     * Cancel the task this FrameDisplayTask is associated with
     */
    void cancel() {
        this.getTask().ifPresent(Task::cancel);
        this.status = CANCELLED;
        this.parent.reportCancel();
    }

    public void setTask(Task me) {
        this.me = me;
    }

    void setParent(AnimationTasks parent) {
        this.parent = parent;
    }

    public Optional<Task> getTask() {
        return Optional.ofNullable(this.me);
    }

    public STATUS getStatus() {
        return status;
    }
}
