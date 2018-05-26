package com.servegame.bl4de.animation.model.type;

import com.servegame.bl4de.animation.data.DataSerializable;
import com.servegame.bl4de.animation.model.Frame;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * File: AnimationFull.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class AnimationFull extends Animation implements DataSerializable {

    /* State */

    /** List containing ALL frames associated with this Animation **/
    private List<Frame> frames;

    /* Constructors */

    /**
     * TODO
     */
    public AnimationFull(){}

    /**
     * TODO
     * @param owner TODO
     * @param name TODO
     */
    public AnimationFull(UUID owner, String name) {
        super(owner, name);
        this.frames = new ArrayList<>();
    }

    /**
     * TODO
     * @param name TODO
     */
    public AnimationFull(String name) {
        super(name);
        this.frames = new ArrayList<>();
    }

    /* Methods */

    @Override
    public boolean isInit() {
        // TODO
        throw new RuntimeException();
    }

    /**
     * Gets a particular {@link Frame} at a given index
     * @param frameNum index for frame array
     * @return resulting {@link Frame}
     */
    public Optional<Frame> getFrame(int frameNum){
        int numOfFrames = this.frames.size();
        if (frameNum > numOfFrames || numOfFrames == 0 || frameNum < 0){
            // Out of bounds
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

    /* START: Getters and Setters */

    /**
     * TODO
     * @return TODO
     */
    public List<Frame> getFrames() {
        return frames;
    }

    /**
     * TODO
     * @param frames TODO
     */
    public void setFrames(List<Frame> frames) {
        this.frames = frames;
    }

    /**
     * Get the number of frames contained in the {@link AnimationFull#frames} list
     * @return int - number of frames currently assigned to this {@link Animation}
     */
    public List<Frame> getActiveFrames(){
        // TODO
        throw new RuntimeException();
    }

    /* END: Getters and Setters */


}
