package com.servegame.bl4de.animation.model.type;

import com.servegame.bl4de.animation.model.DataSerializable;
import com.servegame.bl4de.animation.model.MasterSubSpace3D;

import java.util.UUID;

/**
 * File: Animation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public abstract class Animation implements DataSerializable {

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

}
