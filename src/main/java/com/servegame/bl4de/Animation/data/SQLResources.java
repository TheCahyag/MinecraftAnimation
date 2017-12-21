package com.servegame.bl4de.Animation.data;

import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.model.SubSpace3D;

/**
 * File: SQLResources.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
class SQLResources {

    /* Animation Table Constants */
    static final String ANIMATION_TABLE                     = "animations";
    static final String COLUMN_ANIMATION_NAME               = "name";
    static final String COLUMN_ANIMATION_OWNER              = "owner";
    static final String COLUMN_ANIMATION_STATUS             = "status";
    static final String COLUMN_ANIMATION_START_FRAME_INDEX  = "startFrameIndex";
    static final String COLUMN_ANIMATION_TICK_DELAY         = "tickDelay";
    static final String COLUMN_ANIMATION_CYCLES             = "cycles";
    static final String COLUMN_ANIMATION_FRAME_NAMES        = "frameNames";

    /* Frame Table Constants */
    static final String COLUMN_FRAME_NAME                   = "frameName";
    static final String COLUMN_FRAME_CREATOR                = "creator";
    static final String COLUMN_FRAME_SUBSPACE_C1            = "subspace_cornerOne";
    static final String COLUMN_FRAME_SUBSPACE_C2            = "subspace_cornerTwo";
    static final String COLUMN_FRAME_SUBSPACE_CONTENTS      = "subspace_contents";

    /* Contents Table Constants */

    static final String[] TABLES = {ANIMATION_TABLE};

    static final String DATABASE = "ANIMATIONS";

    /**
     * Creates the name of a table that will contain the frames associated with a given animation.
     * Result is: animationName_owner_frames, where animationName is the name of the animation
     * and owner is the animation owner's uuid.toString().
     * @param animation {@link Animation} to create the name for a frame table
     * @return String in the format of: animationName_owner_frames
     */
    static String getFrameTableName(Animation animation){
        return animation.getAnimationName() + "_" + animation.getOwner().toString() + "_frames";
    }

    /**
     * Creates the name of a table that will contain the {@link SubSpace3D#contents} of a {@link SubSpace3D}.
     * Result is: animationName_owner_frameName_contents, where animationName is the name of the animation,
     * and owner is the frames owner's uuid.toString() and frameName is the name of the {@link Frame} that
     * encapsulates the {@link SubSpace3D} and subsequently the {@link SubSpace3D#contents}
     * @param animation given {@link Animation} that the {@link Frame} is associated with
     * @param frame given {@link Frame} that contains the {@link SubSpace3D#contents} that will be stored in
     *              the created table
     * @return String in the format of: animationName_owner_frameName_contents
     */
    static String getContentTableName(Animation animation, Frame frame){
        return animation.getAnimationName() + "_" +
                frame.getCreator().toString() + "_" +
                frame.getName() + "_contents";
    }
}
