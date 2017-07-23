package com.servegame.bl4de.Animation.models;

import com.servegame.bl4de.Animation.exceptions.UninitializedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * File: AnimationPlugin.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class Animation implements Serializable {
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
        return new Frame(owner,"", this.masterSubSpace);
    }

    /**
     * Add a frame to the animation
     * @param frame the new {@link Frame}
     * @throws UninitializedException frame is not initialized correctly/completely
     */
    public void addFrame(Frame frame) throws UninitializedException {
        if (frame.getName().equals("")){
            frame.setName(this.animationName + ":frame" + this.frameIndex++);
        }
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
