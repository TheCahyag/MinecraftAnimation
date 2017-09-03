package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.AnimationUtil;
import com.servegame.bl4de.Animation.util.TextResponses;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;
import java.util.UUID;

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

        // See if the animation exists already
        Optional<Animation> animationOptional = AnimationUtil.getAnimation(animationName, owner);
        if (animationOptional.isPresent()){
            // The animation already exists
            player.sendMessage(TextResponses.ANIMATION_ALREADY_EXISTS_ERROR);
            return CommandResult.success();
        }

        // Make the new animation
        Animation animation = new Animation(owner, animationName);
        if (AnimationUtil.createAnimation(animation)){
            // Animation was created and saved successfully
            player.sendMessage(Text.of(Util.PRIMARY_COLOR, "Animation ",
                    Util.PRIMARY_COLOR, "'",
                    Util.NAME_COLOR, animationName,
                    Util.PRIMARY_COLOR, "' ",
                    Util.ACTION_COLOR, "created ",
                    Util.PRIMARY_COLOR, "successfully."));
            return CommandResult.success();
        } else {
            // There was a problem creating the animation
            player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
            return CommandResult.empty();
        }
    }
}
