package com.servegame.bl4de.Animation.task;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.model.Frame;

/**
 * File: FrameDisplayTask.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class FrameDisplayTask implements FrameTask {
    private Frame frame;
    private int tickDelay, index;

    public FrameDisplayTask(Frame frame, int tickDelay, int index){
        this.frame = frame;
        this.tickDelay = tickDelay;
        this.index = index;
    }

    @Override
    public void run() {
        boolean debug = AnimationPlugin.instance.isDebug();
        if (debug) {
            this.debugInfo();
        }
        this.modifyWorld();
    }

    @Override
    public void modifyWorld() {

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
}
