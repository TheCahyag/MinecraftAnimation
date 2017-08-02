package com.servegame.bl4de.Animation.util;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.Text;

/**
 * File: TextResponses.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class TextResponses {

    // General Responses
    // General Warning Responses
    public static final Text PLAYER_ONLY_COMMAND_WARNING = Text.of(Util.WARNING_COLOR, "This command must be ran by a player in-game.");

    // General Error Responses


    // Animation Responses
    // Animation Warning Responses
    // Animation Error Responses
    public static final Text ANIMATION_SAVE_ERROR = Text.of(Util.ERROR_COLOR, "There was a problem saving the animation.");
    public static final Text ANIMATION_ALREADY_EXSITS_ERROR = Text.of(Util.ERROR_COLOR, "The specified animation already exists.");
    public static final Text ANIMATION_NOT_FOUND_ERROR = Text.of(Util.ERROR_COLOR, "Animation was not found.");
    public static final Text ANIMATION_NOT_SPECIFIED_ERROR = Text.of(Util.ERROR_COLOR, "Animation name was not parsed correctly/wasn't specified.");



    // Frame Responses
    // Frame Warning Responses
    // Frame Error Responses

    /**
     * TODO
     * @param src
     * @param message
     */
    public static void sendResponse(CommandSource src, Text message){
        src.sendMessage(message);
    }


}