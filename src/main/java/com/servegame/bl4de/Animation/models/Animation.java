package com.servegame.bl4de.Animation.models;

import java.util.ArrayList;
import java.util.UUID;

/**
 * File: AnimationPlugin.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class Animation {
    private UUID owner;
    private String animationName;
    private ArrayList<Frame> frames;
    private SubSpace3D masterSubSpace;
    private int frameIndex = 0;

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

    public Frame getBlankFrame(UUID owner, String name){
        if (!this.masterSubSpace.isInitialized()){
            throw new IllegalArgumentException("Sub space has not yet been defined.");
        }
        return new Frame(owner, name, this.masterSubSpace);
    }

    public Frame getBlankFrame(UUID owner){
        if (!this.masterSubSpace.isInitialized()){
            throw new IllegalArgumentException("Sub space has not yet been defined.");
        }
        return new Frame(owner,"", this.masterSubSpace);
    }

    /**
     * Add a frame to the animation
     * @param frame the new {@link Frame}
     */
    public void addFrame(Frame frame){
        if (frame.getName().equals("")){
            frame.setName(this.animationName + ":frame" + this.frameIndex++);
        }
        if (frame.isInitialized()){

            this.frames.add(frame);
        } else {
            // Frame wasn't initialized correctly/completely
            throw new IllegalArgumentException("Frame is not initialized correctly/completely.");
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
        // Not sure if this.frames.iterator will return null on a null Iterable
        if (this.frames.size() <= 0){
            return false;
        }
        return true;
    }

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
}
