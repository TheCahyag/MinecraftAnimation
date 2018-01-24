package com.servegame.bl4de.Animation.controller;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.data.PreparedStatements;
import com.servegame.bl4de.Animation.model.Animation;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: AnimationController.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class AnimationController {

    /**
     * Stops all animations, usually ran when the server is shutting down.
     */
    public static void stopAllAnimations(){
        AnimationPlugin.taskManager.stopAllAnimations();
    }

    /**
     * TODO
     * @param animation
     * @return
     */
    public static Text linkToAnimationInfo(Animation animation){
        return Text.builder()
                .append(Text.of(NAME_COLOR, COMMAND_STYLE, animation.getAnimationName()))
                .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " info"))
                .build();
    }

    /**
     * TODO
     * @param animation
     * @return
     */
    public static Text getButtonsForAnimation(Animation animation){
        return Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "DELETE",
                                PRIMARY_COLOR, "] "))
                        .onClick(TextActions.runCommand("/animate delete " + animation.getAnimationName()))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "CREATE FRAME",
                                PRIMARY_COLOR, "] "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame create"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "LIST FRAMES",
                                PRIMARY_COLOR, "] | "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame list"))
                        .build())
                .build();
    }

    /**
     * Creates the buttons that appear at the bottom of /animate list
     * @return Text representing a button that allows for creating animations
     */
    public static Text getButtonsForList(){
        return Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "CREATE",
                                PRIMARY_COLOR, "]"))
                        .onClick(TextActions.suggestCommand("/animate create <name>"))
                        .build())
                .build();
    }

    // Most of these methods are just calls to the PreparedStatements class

    public static boolean createAnimation(Animation animation){
        return PreparedStatements.createAnimation(animation);
    }

    public static Optional<Animation> getAnimation(String name, UUID owner){
        long startTime = System.currentTimeMillis();
        Optional<Animation> optionalAnimation = PreparedStatements.getAnimation(name, owner);
        long endTime = System.currentTimeMillis();
        AnimationPlugin.logger.info("Get full (" + name + "): " + (endTime - startTime));
        return optionalAnimation;
    }

    public static Optional<Animation> getBareAnimation(String name, UUID owner){
        long startTime = System.currentTimeMillis();
        Optional<Animation> optionalAnimation = PreparedStatements.getBareAnimation(name, owner);
        long endTime = System.currentTimeMillis();
        AnimationPlugin.logger.info("Get bare (" + name + "): " + (endTime - startTime));
        return optionalAnimation;
    }

    public static ArrayList<String> getAnimationsByOwner(UUID owner) {
        return PreparedStatements.getAnimationsByOwner(owner);
    }

    public static boolean updateAnimationStatus(Animation animation, Animation.Status status){
        return PreparedStatements.updateAnimationStatus(animation, status);
    }

    public static boolean saveAnimation(Animation animation){
        return PreparedStatements.saveAnimation(animation);
    }

    public static boolean saveBareAnimation(Animation animation){
        return PreparedStatements.saveBareAnimation(animation);
    }

    public static boolean deleteAnimation(Animation animation){
        return PreparedStatements.deleteAnimation(animation);
    }

    public static boolean refreshAnimation(String name, UUID owner){
        return PreparedStatements.refreshAnimation(name, owner);
    }

    public static boolean renameAnimation(Animation animation){
        return PreparedStatements.renameAnimation(animation);
    }
}
