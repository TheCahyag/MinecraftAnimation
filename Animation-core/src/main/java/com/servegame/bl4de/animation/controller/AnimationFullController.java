package com.servegame.bl4de.animation.controller;

import com.servegame.bl4de.animation.exception.UninitializedException;
import com.servegame.bl4de.animation.model.Frame;
import com.servegame.bl4de.animation.model.type.Animation;
import com.servegame.bl4de.animation.model.type.AnimationFull;

import java.util.UUID;

/**
 * File: AnimationFullController.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public abstract class AnimationFullController {

    /**
     * TODO
     * @param animation TODO
     * @param owner TODO
     * @return TODO
     * @throws UninitializedException TODO
     */
    public static Frame getBlankFrame(AnimationFull animation, UUID owner, String name) throws UninitializedException {
        // TODO
        return null;
    }

    /**
     * TODO
     * @param animation TODO
     * @param owner TODO
     * @return TODO
     * @throws UninitializedException TODO
     */
    public static Frame getBlankFrame(AnimationFull animation, UUID owner) throws UninitializedException {
        // TODO
        return null;
    }

    /**
     * TODO
     * @param animation TODO
     * @param frame TODO
     * @param index TODO
     * @throws UninitializedException TODO
     */
    public static void addFrame(AnimationFull animation, Frame frame, int index) throws UninitializedException {
        // TODO
        throw new RuntimeException();
    }

    /**
     * TODO
     * @param animation TODO
     * @param frame TODO
     * @throws UninitializedException TODO
     */
    public static void addFrame(AnimationFull animation, Frame frame) throws UninitializedException {
        // TODO
        throw new RuntimeException();
    }

    /* START: Action Methods */

    /**
     * TODO
     * @param animation TODO
     * @param frame TODO
     * @throws UninitializedException TODO
     */
    public static boolean start(AnimationFull animation, int frame) throws UninitializedException {
        // TODO
        throw new RuntimeException();
    }

    /**
     * TODO
     * @param animation TODO
     * @throws UninitializedException TODO
     */
    public static boolean start(AnimationFull animation) throws UninitializedException {
        // TODO
        throw new RuntimeException();
    }

    /**
     * TODO
     */
    public static boolean stop(){
        // TODO
        throw new RuntimeException();
    }

    /**
     * TODO
     */
    public static boolean pause(){
        // TODO
        throw new RuntimeException();
    }

    /* END: Action Methods */

    /* START: Frame Manipulation */

    /**
     * TODO
     * @param animation TODO
     * @param frame TODO
     */
    public void deleteFrame(AnimationFull animation, Frame frame){
        // TODO
        throw new RuntimeException();
    }

}
