package com.servegame.bl4de.animation.model.type;

import com.servegame.bl4de.animation.model.MasterSubSpace3D;

import java.util.UUID;

/**
 * File: Animation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public abstract class Animation {

    /* Final State */

    /** TODO **/
    private final Status DEFAULT_STATUS = Status.STOPPED;

    /* State */

    /** Owner of the animation **/
    private UUID owner;

    /** Name of the Animation */
    private String animationName;

    /** TODO **/
    private MasterSubSpace3D masterSubSpace;

    /** TODO **/
    private Status status;

    /** Index of the Frame that the Animation will start on **/
    private int startFrameIndex;

    /** Delay between Frames in Minecraft ticks **/
    private int tickDelay;

    /** Number of times the Animation will repeat before it stops automatically **/
    private int cycles;


    /**
     * The possible states for the {@link Animation#status status}
     */
    public enum Status {
        /**
         * The paused state.
         */
        PAUSED,

        /**
         * The running state.
         */
        RUNNING,

        /**
         * The stopped state.
         */
        STOPPED
    }

    /* Constructors */

    /**
     * TODO
     * @param owner TODO
     * @param name TODO
     */
    protected Animation(UUID owner, String name){
        // TODO
    }

    /**
     * TODO
     * @param name TODO
     */
    protected Animation(String name){
        // TODO
    }

    /**
     * TODO
     * @return TODO
     */
    public abstract boolean isInit();







    /* START: Getters and Setters */

    public UUID getOwner() {
        return owner;
    }

    public String getAnimationName() {
        return animationName;
    }

    public void setAnimationName(String animationName) {
        this.animationName = animationName;
    }

    public MasterSubSpace3D getMasterSubSpace() {
        return masterSubSpace;
    }

    public void setMasterSubSpace(MasterSubSpace3D masterSubSpace) {
        this.masterSubSpace = masterSubSpace;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getStartFrameIndex() {
        return startFrameIndex;
    }

    public void setStartFrameIndex(int startFrameIndex) {
        this.startFrameIndex = startFrameIndex;
    }

    public int getTickDelay() {
        return tickDelay;
    }

    public void setTickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
    }

    public int getCycles() {
        return cycles;
    }

    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    /* END: Getters and Setters */

    @Override
    public String toString() {
        return "Animation{" +
                "owner=" + owner +
                ", animationName='" + animationName + '\'' +
                ", masterSubSpace=" + masterSubSpace +
                ", status=" + status +
                ", startFrameIndex=" + startFrameIndex +
                ", tickDelay=" + tickDelay +
                ", cycles=" + cycles +
                '}';
    }
}
