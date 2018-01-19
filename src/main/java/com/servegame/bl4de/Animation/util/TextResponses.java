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
    public static final Text ALL_ANIMATIONS_STOPPED = Text.of(PRIMARY_COLOR, "All animations have been stopped.");
    // General Warning Responses
    public static final Text PLAYER_ONLY_COMMAND_WARNING = Text.of(WARNING_COLOR, "This command must be ran by a player in-game.");
    public static final Text CONSOLE_ONLY_COMMAND_WARNING = Text.of(WARNING_COLOR, "This command must be ran in the console.");
    public static final Text LARGE_VOLUME_WARNING = Text.of(WARNING_COLOR, "Warning: Large animation volume. May cause lag.");

    // General Error Responses
    public static final Text GENERAL_ARGUMENTS_INCORRECT = Text.of(ERROR_COLOR, "The arguments supplied are incorrect or were not parsed correctly.");
    public static final Text GENERAL_NO_NAME_ERROR = Text.of(ERROR_COLOR, "The name provided was not parsed correctly/wasn't specified.");
    public static final Text GENERAL_NAME_TOO_LONG = Text.of(ERROR_COLOR, "The name provided was too long. (must be <= 255 characters long)");
    public static final Text GENERAL_INDIFFERENT_NAME_ERROR = Text.of(ERROR_COLOR, "The name provided is indifferent to the current name.");
    public static final Text GENERAL_PROBLEM_RUNNING_COMMAND_ERROR = Text.of(ERROR_COLOR, "There was a problem running this command... check console for stacktrace.");
    public static final Text EXCEED_MAX_VOLUME = Text.of(ERROR_COLOR, "The maximum volume for an animation has been exceeded. (Add the -f flag at the end of the command to override)");
    public static final Text NUMBER_FORMAT_EXCEPTION = Text.of(ERROR_COLOR, "The number entered couldn't be parsed properly. (maybe it wasn't a number)");
    public static final Text GENERAL_INDEX_INVALID_ERROR = Text.of(ERROR_COLOR, "The index entered was not a valid index for this animation.");
    public static final Text USER_DOESNT_HAVE_PERMISSION = Text.of(ERROR_COLOR, "You do not have permission to run this command");

    // Debug Change Responses
    public static final Text DEBUG_SET_TRUE = Text.of(PRIMARY_COLOR, "Debug has been ", ACTION_COLOR, "set ", PRIMARY_COLOR, "to ", SECONDARY_COLOR, "true.");
    public static final Text DEBUG_SET_FALSE = Text.of(PRIMARY_COLOR, "Debug has been ", ACTION_COLOR, "set ", PRIMARY_COLOR, "to ", SECONDARY_COLOR, "false.");

    // Animation Responses
    public static final Text ANIMATION_SUCCESSFULLY_ALTERED = Text.of(PRIMARY_COLOR, "Animation successfully altered.");
    // Animation Warning Responses
    // Animation Error Responses
    public static final Text ANIMATION_SAVE_ERROR = Text.of(ERROR_COLOR, "There was a problem saving the animation.");
    public static final Text ANIMATION_DELETE_ERROR = Text.of(ERROR_COLOR, "There was a problem deleting the animation.");
    public static final Text ANIMATION_ALREADY_EXISTS_ERROR = Text.of(ERROR_COLOR, "The specified animation name already exists.");
    public static final Text ANIMATION_NOT_FOUND_ERROR = Text.of(ERROR_COLOR, "Animation was not found.");
    public static final Text ANIMATION_NOT_SPECIFIED_ERROR = Text.of(ERROR_COLOR, "Animation name was not parsed correctly/wasn't specified.");
    public static final Text ANIMATION_NOT_INITIALIZED_ERROR = Text.of(ERROR_COLOR, "The animation is currently not fully initialized.");
    public static final Text ANIMATION_ALREADY_RUNNING = Text.of(ERROR_COLOR, "The animation is already running.");
    public static final Text ANIMATION_ALREADY_PAUSED = Text.of(ERROR_COLOR, "The animation is already paused.");
    public static final Text ANIMATION_ALREADY_STOPPED = Text.of(ERROR_COLOR, "The animation is already stopped.");
    public static final Text ANIMATION_CANT_BE_RUNNING = Text.of(ERROR_COLOR, "The action could not be performed because the specified animation is running.");
    public static final Text ANIMATION_ISNT_RUNNING = Text.of(ERROR_COLOR, "The animation isn't running.");
    public static final Text ANIMATION_SETTING_DELAY_TOO_LOW = Text.of(ERROR_COLOR, "The resulting delay must be > 0.");
    public static final Text ANIMATION_SETTING_CYCLES_TOO_LOW = Text.of(ERROR_COLOR, "The cycles can not be < -1.");
    public static final Text ANIMATION_SETTING_CYCLES_NOT_ZERO = Text.of(ERROR_COLOR, "The cycles can not be 0.");
    public static final Text ANIMATION_NO_FRAMES_PRESENT = Text.of(ERROR_COLOR, "There are no frames for this animation.");
    public static final Text ANIMATION_CORNER_NOT_SET = Text.of(ERROR_COLOR, "That corner hasn't been set yet.");


    // Frame Responses
    public static final Text FRAME_SUCCESSFULLY_DELETED = Text.of(PRIMARY_COLOR, "The frame was successfully deleted.");
    // Frame Warning Responses
    // Frame Error Responses
    public static final Text FRAME_NOT_INITIALIZED_ERROR = Text.of(ERROR_COLOR, "The frame is not initialized correctly/completely. (This shouldn't happen)");
    public static final Text FRAME_NOT_SPECIFIED_ERROR = Text.of(ERROR_COLOR, "Frame name/num was not parsed correctly or wasn't specified.");
    public static final Text FRAME_NOT_FOUND_ERROR = Text.of(ERROR_COLOR, "Frame was not found.");
    public static final Text FRAME_DELETE_ERROR = Text.of(ERROR_COLOR, "There was a problem deleting the frame.");
    public static final Text FRAME_NOT_DISPLAYED_ERROR = Text.of(ERROR_COLOR, "There was a problem displaying the frame.");
    public static final Text FRAME_FAILED_DUPLICATION_ERROR = Text.of(ERROR_COLOR, "There was a problem duplicating the frame.");
    public static final Text FRAME_HAS_NO_CONTENT = Text.of(ERROR_COLOR, "The frame does not have any content.");

    // Other Error Responses
    public static final Text SUBSPACE_NOT_INITIALIZED_ERROR = Text.of(ERROR_COLOR, "The subspace is not fully initialized. (Set the animation corners with /animate <name> set <pos1|pos2>)");

    // Hover Messages

    public static final Text ANIMATION_C2V_INFO = Text.of("Click here to see the animation's info.");
    public static final Text FRAME_C2V_INFO = Text.of("Click here to see the frame's info.");
}
