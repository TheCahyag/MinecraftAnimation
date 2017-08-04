package com.servegame.bl4de.Animation.models;

import com.servegame.bl4de.Animation.exceptions.UninitializedException;
import com.servegame.bl4de.Animation.util.FrameUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * File: AnimationPlugin.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class Animation implements Serializable {
    private long serialVersionUID = -334896027741840235L;

    private final Status DEFAULT_STATUS = Status.STOPPED;
    private Status status;

    private UUID owner;
    private String animationName;
    private ArrayList<Frame> frames;
    private SubSpace3D masterSubSpace;
    private int frameIndex = 0;
    private int tickDelay = 20;
    private int cycles = 10;

    /**
     * Designated Animation constructor
     * @param owner animation owner represented by a {@link UUID}
     * @param name name of animation
     */
    public Animation(UUID owner, String name){
        this.owner = owner;
        this.animationName = name;
        this.masterSubSpace = new SubSpace3D();
        this.frames = new ArrayList<>();
    }

    /**
     * The possible states for the {@link Animation}
     */
    public static enum Status {
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

    /**
     * Get a blank frame with a name
     * @param owner player {@link UUID}
     * @param name frame name
     * @return blank {@link Frame}
     * @throws UninitializedException subspace is not initialized correctly/completely
     */
    public Frame getBlankFrame(UUID owner, String name) throws UninitializedException {
        if (!this.masterSubSpace.isInitialized()){
            throw new UninitializedException("Sub space has not yet been defined.");
        }
        return new Frame(owner, name, this.masterSubSpace);
    }

    /**
     * Get a blank frame with the default "" name TODO this probably needs to change, can't have empty string names since I might be referencing these frames
     * @param owner
     * @return
     * @throws UninitializedException subspace is not initialized correctly/completely
     */
    public Frame getBlankFrame(UUID owner) throws UninitializedException {
        if (!this.masterSubSpace.isInitialized()){
            throw new UninitializedException("Sub space has not yet been defined.");
        }
        return new Frame(owner, FrameUtil.generateFrameName(this), this.masterSubSpace);
    }

    /**
     * Add a frame to the animation
     * @param frame the new {@link Frame}
     * @throws UninitializedException frame is not initialized correctly/completely
     */
    public void addFrame(Frame frame) throws UninitializedException {
        if (frame.isInitialized()){
            this.frames.add(frame);
        } else {
            // Frame wasn't initialized correctly/completely
            throw new UninitializedException("Frame is not initialized correctly/completely.");
        }
    }

    /**
     * Checks to make sure everything that needs to be setup is setup
     * @return boolean indicating if an animation is ready to be played
     */
    public boolean isReady(){
        if (!this.masterSubSpace.isInitialized()){
            return false;
        }
        if (this.frames.size() <= 0){
            return false;
        }
        return true;
    }

    /* START ACTION METHODS */

    public void start(){
        setStatus(Status.RUNNING);
    }

    public void stop(){
        setStatus(Status.STOPPED);
    }

    public void pause(){
        setStatus(Status.PAUSED);
    }

    /* END ACTION METHODS */

    /* START GETTERS AND SETTERS */

    /**
     * Getter for the owner
     * @return {@link UUID}
     */
    public UUID getOwner() {
        return owner;
    }

    /**
     * Setter for the owner
     * @param owner {@link UUID}
     */
    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    /**
     * Getter for the name of the animation
     * @return the string representation of the name
     */
    public String getAnimationName() {
        return animationName;
    }

    /**
     * Setter for the string representation of the name
     * @param animationName the name
     */
    public void setAnimationName(String animationName) {
        this.animationName = animationName;
    }

    /**
     * Setter for the status of the {@link Animation}
     * @param status {@link Status}
     */
    private void setStatus(Status status){
        this.status = status;
    }

    /**
     * Getter for the status of the {@link Animation}
     * @return the {@link Status}
     */
    public Status getStatus(){
        return (status == null) ? DEFAULT_STATUS : this.status;
    }

    /**
     * Get the number of frames contained in the {@link Animation#frames} list
     * @return int - number of frames currently assigned to this {@link Animation}
     */
    public int getNumOfFrames(){
        return this.frames.size();
    }

    /**
     * Getter for the {@link SubSpace3D}
     * @return {@link SubSpace3D}
     */
    public SubSpace3D getSubSpace() {
        return masterSubSpace;
    }

    /**
     * Getter for the {@link Frame}s of the {@link Animation}
     * @return All the {@link Frame}s in an ArrayList
     */
    public ArrayList<Frame> getFrames() {
        return this.frames;
    }

    /**
     * Getter for the frame index (-f flag in {@link com.servegame.bl4de.Animation.commands.animation.StartAnimation} command)
     * @return int - the frame the animation will start on when it is ran
     */
    public int getFrameIndex() {
        return frameIndex;
    }

    /**
     * Setter for the frame index (-f flag in {@link com.servegame.bl4de.Animation.commands.animation.StartAnimation} command)
     * @param frameIndex int - the new frame index
     */
    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    /**
     * Getter for the tick delay between frames
     * @return int - tick delay
     */
    public int getTickDelay() {
        return tickDelay;
    }

    /**
     * Setter for the tick delay between frames
     * @param tickDelay int - tick delay
     */
    public void setTickDelay(int tickDelay) {
        this.tickDelay = tickDelay;
    }

    /**
     * Getter for the number of cycles the animation
     * will complete before it auto stops
     * @return int - cycles
     */
    public int getCycles() {
        return cycles;
    }

    /**
     * Setter for the number of cycles the animation
     * will complete before it auto stops
     * @param cycles int - cycles
     */
    public void setCycles(int cycles) {
        this.cycles = cycles;
    }

    /* END GETTERS AND SETTERS */
}
