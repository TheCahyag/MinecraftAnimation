package com.servegame.bl4de.Animation.controller;

import com.servegame.bl4de.Animation.data.PreparedStatements;
import com.servegame.bl4de.Animation.model.Animation;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.servegame.bl4de.Animation.data.PreparedStatements.getAnimations;
import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: AnimationController.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class AnimationController {

    private static final String CONFIG_DIR = "./config/animation";
    private static final String ANIMATION_DATA_DIR = CONFIG_DIR + "/animations";

    /**
     * Stops all animations, usually ran when the server is shutting down.
     */
    public static void stopAllAnimations(){
        Map<UUID, ArrayList<String>> animations = getAnimations();
        for (Map.Entry<UUID, ArrayList<String>> entry :
                animations.entrySet()) {
            ArrayList<String> animationNames = entry.getValue();
            UUID uuid = entry.getKey();
            for (int i = 0; i < animationNames.size(); i++) {
                getAnimation(animationNames.get(i), uuid).get().stop();
            }
        }
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
     * TODO
     * @return
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

    public static boolean createAnimation(Animation animation){
        return PreparedStatements.createAnimation(animation);
    }

    public static Optional<Animation> getAnimation(String name, UUID owner){
        return PreparedStatements.getAnimation(name, owner);
    }

    public static ArrayList<String> getAnimationsByOwner(UUID owner) {
        return PreparedStatements.getAnimationsByOwner(owner);
    }

    public static boolean saveAnimation(Animation animation){
        return PreparedStatements.saveAnimation(animation);
    }

    public static boolean deleteAnimation(Animation animation){
        return PreparedStatements.deleteAnimation(animation);
    }
}
