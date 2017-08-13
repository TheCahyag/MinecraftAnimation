package com.servegame.bl4de.Animation.model;

import com.google.common.reflect.TypeToken;
import com.servegame.bl4de.Animation.command.animation.action.StartAnimation;
import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.util.AnimationUtil;
import com.servegame.bl4de.Animation.util.FrameUtil;
import com.servegame.bl4de.Animation.util.TextResponses;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import static com.servegame.bl4de.Animation.data.DataQueries.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

/**
 * File: AnimationPlugin.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class Animation implements DataSerializable {

    private final Status DEFAULT_STATUS = Status.STOPPED;
    private Status status;

    private UUID owner;
    private String animationName;
    private List<Frame> frames;
    private SubSpace3D masterSubSpace;
    private int frameIndex = 0;
    private int tickDelay = 20;
    private int cycles = -1;

    /**
     * Does-nothing constructor does nothing
     */
    public Animation(){}

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
    private enum Status {
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
            throw new UninitializedException(TextResponses.SUBSPACE_NOT_INITIALIZED_ERROR);
        }
        return new Frame(owner, name, this.masterSubSpace);
    }

    /**
     * Get a blank frame with generated name
     * @param owner player {@link UUID}
     * @return blank {@link Frame}
     * @throws UninitializedException subspace is not initialized correctly/completely
     */
    public Frame getBlankFrame(UUID owner) throws UninitializedException {
        if (!this.masterSubSpace.isInitialized()){
            throw new UninitializedException(TextResponses.SUBSPACE_NOT_INITIALIZED_ERROR);
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
            // Frame wasn't initialized correctly/completely, don't think this can happen but why not
            throw new UninitializedException(TextResponses.FRAME_NOT_INITIALIZED_ERROR);
        }
    }

    /**
     * Checks to make sure everything that needs to be setup is setup
     * @return boolean indicating if an animation is ready to be played
     */
    public boolean isInitialized(){
        if (!this.masterSubSpace.isInitialized()){
            return false;
        }
        if (this.frames.size() <= 0){
            return false;
        }
        return true;
    }

    /* START ACTION METHODS */

    /**
     * TODO
     * @param frame
     */
    public void start(int frame){
        this.frameIndex = frame;
        setStatus(Status.RUNNING);
        if (AnimationUtil.saveAnimation(this)){

        } else {

        }
    }

    /**
     * TODO
     */
    public void start(){
        start(0);
    }

    /**
     * TODO
     */
    public void stop(){
        setStatus(Status.STOPPED);
        if (AnimationUtil.saveAnimation(this)){

        } else {

        }
    }

    /**
     * TODO
     */
    public void pause(){
        setStatus(Status.PAUSED);
        if (AnimationUtil.saveAnimation(this)){

        } else {

        }
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
     * Setter for the master subspace
     * @param subSpace
     */
    private void setSubSpace(SubSpace3D subSpace){
        this.masterSubSpace = subSpace;
    }

    /**
     * Getter for the {@link Frame}s of the {@link Animation}
     * @return All the {@link Frame}s in an ArrayList
     */
    public List<Frame> getFrames() {
        return this.frames;
    }

    /**
     * Setter for the frames
     * @param frames
     */
    private void setFrames(List<Frame> frames){
        this.frames = frames;
    }

    /**
     * Gets a particular {@link Frame} at a given index
     * @param frameNum index for frame array
     * @return resulting {@link Frame}
     */
    public Optional<Frame> getFrame(int frameNum){
        if (frameNum > this.frames.size()){
            return Optional.empty();
        } else {
            return Optional.of(this.frames.get(frameNum));
        }
    }

    /**
     * Gets a particular named {@link Frame}
     * @param frameName given string name
     * @return given {@link Frame}
     */
    public Optional<Frame> getFrame(String frameName){
        for (Frame frame :
                this.frames) {
            if (frame.getName().equals(frameName)) {
                return Optional.of(frame);
            }
        }
        return Optional.empty();
    }

    /**
     * Deletes a frame from the frame list
     * @param frame given {@link Frame}
     */
    public void deleteFrame(Frame frame){
        this.frames.remove(frame);
    }

    /**
     * Getter for the frame index (-f flag in {@link StartAnimation} command)
     * @return int - the frame the animation will start on when it is ran
     */
    public int getFrameIndex() {
        return frameIndex;
    }

    /**
     * Setter for the frame index (-f flag in {@link StartAnimation} command)
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

    /* START DATA SERIALIZATION METHODS */

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = DataContainer.createNew()
                .set(ANIMATION_STATUS, getStatus().name())
                .set(ANIMATION_OWNER, getOwner())
                .set(ANIMATION_NAME, getAnimationName())
                .set(ANIMATION_FRAMES, getFrames())
                .set(ANIMATION_SUBSPACE, getSubSpace())
                .set(ANIMATION_FRAME_INDEX, getFrameIndex())
                .set(ANIMATION_TICK_DELAY, getTickDelay())
                .set(ANIMATION_CYCLES, getCycles());
        return container;
    }

    /* END DATA SERIALIZATION METHODS */

    public static class Builder extends AbstractDataBuilder<Animation> {

        /**
         * TODO
         */
        public Builder(){
            super(Animation.class, 0);
        }

        @Override
        protected Optional<Animation> buildContent(DataView container) throws InvalidDataException {
            Animation animation = null;
            if (container.contains(ANIMATION_STATUS, ANIMATION_OWNER, ANIMATION_NAME,
                    ANIMATION_FRAMES, ANIMATION_FRAME_INDEX,
                    ANIMATION_TICK_DELAY, ANIMATION_CYCLES)){
                // Get data from the container
                Status status = Status.valueOf(container.getString(ANIMATION_STATUS).get());
                UUID owner = container.getObject(ANIMATION_OWNER, UUID.class).get();
                String name = container.getString(ANIMATION_NAME).get();
                List<Frame> frames = container.getObjectList(ANIMATION_FRAMES, Frame.class).get();
                int frameIndex = container.getInt(ANIMATION_FRAME_INDEX).get();
                int tickDelay = container.getInt(ANIMATION_TICK_DELAY).get();
                int cycles = container.getInt(ANIMATION_CYCLES).get();

                // Create animation and set information
                animation = new Animation(owner, name);
                animation.setStatus(status);
                animation.setFrames(frames);
                animation.setFrameIndex(frameIndex);
                animation.setTickDelay(tickDelay);
                animation.setCycles(cycles);

                // Check for objects that may be there
                if (container.contains(ANIMATION_SUBSPACE)){
                    // SubSpace is available
                    SubSpace3D subSpace = container.getObject(ANIMATION_SUBSPACE, SubSpace3D.class).get();
                    animation.setSubSpace(subSpace);
                }
            } else {
                System.out.println("Didn't have all the animation criteria");
            }
            return Optional.ofNullable(animation);
        }
    }

    public static String serialize(Animation animation) {
        try {
            StringWriter sink = new StringWriter();
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setSink(() -> new BufferedWriter(sink)).build();
            ConfigurationNode node = loader.createEmptyNode();
            node.setValue(TypeToken.of(Animation.class), animation);
            loader.save(node);
            return sink.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Animation deserialize(String item) {
        try {
            StringReader source = new StringReader(item);
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(source)).build();
            ConfigurationNode node = loader.load();
            return node.getValue(TypeToken.of(Animation.class));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
