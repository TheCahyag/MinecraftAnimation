package com.servegame.bl4de.Animation.command.animation.action;

import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

/**
 * File: StopAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class StopAnimation implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = ((Player) src);

        // Get animation
        Optional<String> animationNameOptional = args.getOne("animation_name");
        if (!animationNameOptional.isPresent()){
            player.sendMessage(TextResponses.ANIMATION_NOT_SPECIFIED_ERROR);
            return CommandResult.empty();
        }
        Optional<Animation> animationOptional = AnimationController.getAnimation(animationNameOptional.get(), player.getUniqueId());
        if (!animationOptional.isPresent()){
            player.sendMessage(TextResponses.ANIMATION_NOT_FOUND_ERROR);
            return CommandResult.success();
        }
        Animation animation = animationOptional.get();

        if (animation.getStatus() == Animation.Status.STOPPED){
            // Animation is already paused
            player.sendMessage(TextResponses.ANIMATION_ALREADY_STOPPED);
            return CommandResult.success();
        }

        animation.stop();

        return CommandResult.success();
    }
}
