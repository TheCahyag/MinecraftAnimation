package com.servegame.bl4de.Animation.util;

import static com.servegame.bl4de.Animation.util.Util.*;
import org.spongepowered.api.text.Text;

/**
 * File: TextResponses.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class TextResponses {

    // General Responses
    // General Warning Responses
    public static final Text PLAYER_ONLY_COMMAND_WARNING = Text.of(WARNING_COLOR, "This command must be ran by a player in-game.");

    // General Error Responses
    public static final Text GENERAL_ARGUMENTS_INCORRECT = Text.of(ERROR_COLOR, "The arguments supplied are incorrect or were not parsed correctly.");

    // Animation Responses
    // Animation Warning Responses
    // Animation Error Responses
    public static final Text ANIMATION_SAVE_ERROR = Text.of(ERROR_COLOR, "There was a problem saving the animation.");
    public static final Text ANIMATION_DELETE_ERROR = Text.of(ERROR_COLOR, "There was a problem deleting the animation.");
    public static final Text ANIMATION_ALREADY_EXISTS_ERROR = Text.of(ERROR_COLOR, "The specified animation already exists.");
    public static final Text ANIMATION_NOT_FOUND_ERROR = Text.of(ERROR_COLOR, "Animation was not found.");
    public static final Text ANIMATION_NOT_SPECIFIED_ERROR = Text.of(ERROR_COLOR, "Animation name was not parsed correctly/wasn't specified.");
    public static final Text ANIMATION_NOT_INITIALIZED_ERROR = Text.of(ERROR_COLOR, "The animation is currently not fully initialized:");

    // Frame Responses
    // Frame Warning Responses
    // Frame Error Responses
    public static final Text FRAME_NOT_INITIALIZED_ERROR = Text.of(ERROR_COLOR, "The frame is not initialized correctly/completely. (This shouldn't happen)");

    // Other Error Responses
    public static final Text SUBSPACE_NOT_INITIALIZED_ERROR = Text.of(ERROR_COLOR, "The subspace is not fully initialized. (Set the animation corners)");
}
