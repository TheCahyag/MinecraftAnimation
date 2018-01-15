package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.TextResponses;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.Optional;
import java.util.UUID;

import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: CreateAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class CreateAnimation implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        // Check if the CommandSource is a player
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        // Get player UUID
        Player player = (Player) src;
        UUID owner = player.getUniqueId();

        // Argument parsing
        Optional<String> animationNameOptional = args.getOne("animation_name");
        if (!animationNameOptional.isPresent()){
            player.sendMessage(TextResponses.ANIMATION_NOT_SPECIFIED_ERROR);
            return CommandResult.success();
        }
        String animationName = animationNameOptional.get();

        // The length of the string must be <= 255 inorder to comply with the table
        if (animationName.length() > 255){
            player.sendMessage(TextResponses.GENERAL_NAME_TOO_LONG);
            return CommandResult.success();
        }

        // See if the animation exists already
        Optional<Animation> animationOptional = AnimationController.getAnimation(animationName, owner);
        if (animationOptional.isPresent()){
            // The animation already exists
            player.sendMessage(TextResponses.ANIMATION_ALREADY_EXISTS_ERROR);
            return CommandResult.success();
        }

        // Make the new animation
        Animation animation = new Animation(owner, animationName);
        if (AnimationController.createAnimation(animation)){
            // Animation was created and saved successfully
            Text message = Text.builder()
                    .append(Text.of(PRIMARY_COLOR, "Animation ",
                            PRIMARY_COLOR, "'"))
                    .append(Text.builder()
                            .append(Text.of(NAME_COLOR, COMMAND_STYLE, animationName))
                            .onClick(TextActions.runCommand("/animate " + animationName + " info"))
                            .onHover(TextActions.showText(TextResponses.ANIMATION_C2V_INFO))
                            .build())
                    .append(Text.of(PRIMARY_COLOR, "' ",
                            ACTION_COLOR, "created ",
                            PRIMARY_COLOR, "successfully."))
                    .build();
            player.sendMessage(message);
            return CommandResult.success();
        } else {
            // There was a problem creating the animation
            player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
            return CommandResult.empty();
        }
    }
}
