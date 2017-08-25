package com.servegame.bl4de.Animation.command.animation.action;

import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.AnimationUtil;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

/**
 * File: StartAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class StartAnimation implements CommandExecutor {
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
        Optional<Animation> animationOptional = AnimationUtil.getAnimation(animationNameOptional.get(), player.getUniqueId());
        if (!animationOptional.isPresent()){
            player.sendMessage(TextResponses.ANIMATION_NOT_FOUND_ERROR);
            return CommandResult.success();
        }
        Animation animation = animationOptional.get();

        if (animation.getStatus() == Animation.Status.RUNNING){
            // Animation is already running
            player.sendMessage(TextResponses.ANIMATION_ALREADY_RUNNING);
            return CommandResult.success();
        }

        // Parse flag arguments
        Optional<Integer> frameToStartOnOptional = args.getOne("frame");
        Optional<Integer> tickDelayOptional = args.getOne("delay");
        Optional<Integer> cyclesOptional = args.getOne("cycles");
        tickDelayOptional.ifPresent(animation::setTickDelay);
        cyclesOptional.ifPresent(animation::setCycles);

        try {
            if (frameToStartOnOptional.isPresent()) {
                animation.start(frameToStartOnOptional.get());
            } else {
                animation.start();
            }
        } catch (UninitializedException ue){
            player.sendMessage(TextResponses.ANIMATION_NOT_INITIALIZED_ERROR);
        }
        return CommandResult.success();

    }
}
