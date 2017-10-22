package com.servegame.bl4de.Animation.model;

import com.google.common.collect.ImmutableList;
import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.command.animation.action.StartAnimation;
import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.controller.FrameController;
import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.task.TaskManager;
import com.servegame.bl4de.Animation.util.TextResponses;
import com.servegame.bl4de.Animation.util.Util;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.HeaderMode;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.scheduler.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.servegame.bl4de.Animation.data.DataQueries.*;

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
        setOwner(owner);
        setAnimationName(name);
        setStatus(DEFAULT_STATUS);
        setSubSpace(new SubSpace3D());
        setFrames(new ArrayList<>());
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
        return new Frame(owner, FrameController.generateFrameName(this), this.masterSubSpace);
    }

    /**
     * Adds a initialized {@link Frame} to the current Animation at a given index
     * @param frame {@link Frame} to add
     * @param index The index to insert upon
     * @throws UninitializedException
     */
    public void addFrame(Frame frame, int index) throws UninitializedException {
        if (frame.isInitialized()){
            this.frames.add(index, frame);
        } else {
            // Frame wasn't initialized correctly/completely, don't think this can happen but why not
            throw new UninitializedException(TextResponses.FRAME_NOT_INITIALIZED_ERROR);
        }
    }

    /**
     * Add a frame to the animation at the end of the frames list
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
     * TODO
     * @param frame
     * @return
     */
    public boolean removeFrame(Frame frame){
        return this.frames.remove(frame);
    }

    /**
     * TODO
     * @param frame
     * @return
     */
    public int getIndexOfFrame(Frame frame){
        List<Frame> frames = this.getFrames();
        for (int i = 0; i < frames.size(); i++) {
            if (frames.get(i).equals(frame)){
                return i;
            }
        }
        return -1;
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

    /**
     * isRunning returns a boolean if the {@link Animation}'s
     * {@link Animation.Status} is {@link Animation.Status#RUNNING}
     * @return boolean representing if the animation is running or not
     */
    public boolean isRunning(){
        return this.getStatus() == Status.RUNNING;
    }

    /* START ACTION METHODS */

    /**
     * Starts the {@link Animation} on a given frame.
     *
     * Starting the {@link Animation} requires calling the {@link TaskManager#createBatch(Animation)}
     * method, which creates a {@link Task} for each {@link Frame}
     * @param frame the starting {@link Frame}
     */
    public void start(int frame) throws UninitializedException {
        this.frameIndex = frame;
        setStatus(Status.RUNNING);
        if (AnimationController.saveAnimation(this)){
            AnimationPlugin.taskManager.createBatch(this);
        } else {
            AnimationPlugin.logger.info("Failed to save animation");
        }
    }

    /**
     * Starts the {@link Animation}
     *
     * Calls {@link Animation#start(int)} passing the start {@link Frame} as
     * 0 (the first {@link Frame})
     */
    public void start() throws UninitializedException {
        start(0);
    }

    /**
     * Stops the {@link Animation}.
     *
     * The {@link TaskManager#stopAnimation(Animation)} is called to end all
     * {@link Task}s associated with this {@link Animation}
     */
    public void stop(){
        setStatus(Status.STOPPED);
        if (AnimationController.saveAnimation(this)){
            AnimationPlugin.taskManager.stopAnimation(this);
        } else {
            AnimationPlugin.logger.info("Failed to save animation");
        }
    }

    /**
     * Pauses the {@link Animation}.
     *
     * There's no need to specifically save {@link Frame} it was on because
     * that is always saved under the {@link Animation#frameIndex} state.
     */
    public void pause(){
        setStatus(Status.PAUSED);
        if (AnimationController.saveAnimation(this)){
            // No functionality right now, need to add some things for this to work
        } else {
            AnimationPlugin.logger.info("Failed to save animation");
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
    public void setStatus(Status status){
        this.status = status;
    }

    /**
     * Getter for the status of the {@link Animation}
     * @return the {@link Status}
     */
    public Status getStatus(){
        return (status == null) ? DEFAULT_STATUS : this.status;
    }

    public void setStatusAsInt(int status){
        switch (status){
            case 0:
                this.status = Status.STOPPED;
                break;
            case 1:
                this.status = Status.RUNNING;
                break;
            case 2:
                this.status = Status.PAUSED;
                break;
            default:
                this.status = Status.STOPPED;
        }
    }

    public int getStatusAsInt(){
        if (status == null){
            return 0;
        }
        switch (this.status){
            case STOPPED:
                return 0;
            case RUNNING:
                return 1;
            case PAUSED:
                return 2;
            default:
                return 0;
        }
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
     * @param subSpace the {@link SubSpace3D}
     */
    public void setSubSpace(SubSpace3D subSpace){
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
     * @param frames the {@link Frame}
     */
    public void setFrames(List<Frame> frames){
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

    @Override
    public String toString() {
        return "\n*******************************   ANIMATION INFO    ******************************\n" +
                "Animation{" +
                "\nDEFAULT_STATUS=" + DEFAULT_STATUS +
                ", \nstatus=" + status +
                ", \nowner=" + owner +
                ", \nanimationName='" + animationName + '\'' +
                ", \nframes=" + frames +
                ", \nmasterSubSpace=\n" + masterSubSpace +
                ", \nframeIndex=" + frameIndex +
                ", \ntickDelay=" + tickDelay +
                ", \ncycles=" + cycles +
                "\n}";
    }

    /* START DATA SERIALIZATION METHODS */

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        if (AnimationPlugin.instance.isDebug()){
            System.out.println("Hello from Animation.toContainer");
            System.out.println(this);
        }
        DataContainer container = DataContainer.createNew()
                .set(ANIMATION_STATUS, getStatus().name())
                .set(ANIMATION_OWNER, getOwner())
                .set(ANIMATION_NAME, getAnimationName())
                .set(ANIMATION_FRAMES, getFrames())
                .set(ANIMATION_SUBSPACE, getSubSpace())
                .set(ANIMATION_FRAME_INDEX, getFrameIndex())
                .set(ANIMATION_TICK_DELAY, getTickDelay())
                .set(ANIMATION_CYCLES, getCycles());

        if (AnimationPlugin.instance.isDebug()){
            System.out.println("Container: " + container.toString());
        }
        return container;
    }

    /* END DATA SERIALIZATION METHODS */

    /**
     * The Builder class is used to create a {@link Animation} object from a
     * {@link DataContainer}. The {@link DataContainer} is created in the serialization
     * process using the {@link Animation#toContainer()} method.
     */
    public static class Builder extends AbstractDataBuilder<Animation> {

        /**
         * Default constructor
         */
        public Builder(){
            super(Animation.class, 0);
        }

        @Override
        protected Optional<Animation> buildContent(DataView container) throws InvalidDataException {
            if (AnimationPlugin.instance.isDebug()){
                System.out.println("Hello from Animation.Builder.buildContent");
                System.out.println("Container: " + container.toString());
                for (DataQuery q :
                        container.getKeys(false)) {
                    System.out.println(q.toString());
                }
                System.out.println("Path: " + container.getCurrentPath());
                System.out.println(container.getObject(ANIMATION_OWNER, UUID.class));
            }
            Animation animation = null;

            if (container.contains(ANIMATION_STATUS, ANIMATION_OWNER, ANIMATION_NAME,
                    ANIMATION_FRAME_INDEX, ANIMATION_TICK_DELAY, ANIMATION_CYCLES)){
                // Get data from the container
                Status status = Status.valueOf(container.getString(ANIMATION_STATUS).get());
                UUID owner = container.getObject(ANIMATION_OWNER, UUID.class).get();
                String name = container.getString(ANIMATION_NAME).get();
                int frameIndex = container.getInt(ANIMATION_FRAME_INDEX).get();
                int tickDelay = container.getInt(ANIMATION_TICK_DELAY).get();
                int cycles = container.getInt(ANIMATION_CYCLES).get();

                // Create animation and set information
                animation = new Animation(owner, name);
                animation.setStatus(status);
                animation.setFrameIndex(frameIndex);
                animation.setTickDelay(tickDelay);
                animation.setCycles(cycles);

                // Check for objects that may be there
                if (container.contains(ANIMATION_FRAMES)){
                    Object soonToBeFrames = container.get(ANIMATION_FRAMES).get();
                    ImmutableList list = ((ImmutableList) soonToBeFrames);

                    List<Frame> frames = new ArrayList<>();
                    Frame.Builder builder = new Frame.Builder();
                    try {
                        for (Object o :
                                list) {
                            DataView dataView = hoconToContainer(Util.encapColons(o.toString()));
                            frames.add(builder.buildContent(dataView).get());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    animation.setFrames(frames);
                }
                if (container.contains(ANIMATION_SUBSPACE)){
                    // SubSpace is available
                    if (AnimationPlugin.instance.isDebug()){
                        System.out.println("IT HAS THE SUBSPACE");
                    }
                    Optional<SubSpace3D> optionalSubspace = new SubSpace3D.Builder()
                                    .buildContent((DataView) container.get(ANIMATION_SUBSPACE).get());
                    SubSpace3D subSpace;
                    if (optionalSubspace.isPresent()){
                        subSpace = optionalSubspace.get();
                    } else {
                        System.err.println("Failed to retrieve SubSpace. Defaulting to a empty subspace...");
                        subSpace = new SubSpace3D();
                    }
                    animation.setSubSpace(subSpace);
                } else {
                    if (AnimationPlugin.instance.isDebug()){
                        System.out.println("IT DOESN'T HAVE THE SUBSPACE");
                    }
                    animation.setSubSpace(new SubSpace3D());
                }
            } else {
                System.out.println("Didn't have all the animation criteria");
            }
            return Optional.ofNullable(animation);
        }
    }

    /**
     * Serializes the {@link Animation}.
     *
     * The {@link Animation#toContainer()} method is used to obtain a
     * {@link DataContainer} which is then converted to a string using the
     * {@link DataFormats#HOCON} writer. The string that is returned is then
     * later put in a H2 Database
     *
     * Note: The {@link DataFormats#HOCON} was used because the {@link DataFormats#JSON}
     * was thought to be the source of a issue where the UUID couldn't be extracted in the
     * {@link Animation#deserialize(String)} process.
     * @param animation {@link Animation} to serialize
     * @return Resulting {@link DataFormats#HOCON} string
     */
    public static String serialize(Animation animation) {
        try {
            if (AnimationPlugin.instance.isDebug()){
                System.out.println("Hello from Animation.serialize");
                System.out.println("Container: " + animation.toContainer().toString());
                System.out.println(DataFormats.HOCON.write(animation.toContainer()));
                System.out.println(animation);
            }
            return DataFormats.HOCON.write(animation.toContainer());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deserializes a {@link Animation} from a given String.
     *
     * The string (which was obtained from the database) is first converted to a
     * {@link DataContainer} using the {@link DataFormats#HOCON} read method. The
     * {@link DataContainer} is then built in the {@link Animation.Builder#buildContent(DataView)}
     * builder.
     * @param item String that should represent an {@link Animation}
     * @return If the building process is successful a {@link Animation} is returned. In the cases where
     * problems in the building process null is returned - Ideally this should never happen
     */
    public static Animation deserialize(String item) {
        try {
            if (AnimationPlugin.instance.isDebug()) {
                System.out.println("Hello from Animation.deserialize");
                System.out.println("Item: " + item);
            }
            DataContainer dataContainer = hoconToContainer(item);
            Optional<Animation> optionalAnimation = new Animation.Builder().build(dataContainer);
            return optionalAnimation.orElse(null);
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static DataContainer hoconToContainer(String hoconString) throws InvalidDataException, IOException {
        final String hoconWithOutNewLines = Util.replaceNewLineWithCommaSometimes(
                hoconString.replace("\t", "").replace(" ", ""));
        if (AnimationPlugin.instance.isDebug()){
            System.out.println(hoconString);
            System.out.println(hoconWithOutNewLines);
        }
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .setHeaderMode(HeaderMode.NONE)
                .setSource(() -> new BufferedReader(new StringReader(hoconString)))
                .build();
        return DataTranslators.CONFIGURATION_NODE.translate(loader.load());
    }
}
