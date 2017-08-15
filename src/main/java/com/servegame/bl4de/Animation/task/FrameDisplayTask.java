package com.servegame.bl4de.Animation.task;

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
        this.modifyWorld();
    }

    @Override
    public void modifyWorld() {

    }
}
