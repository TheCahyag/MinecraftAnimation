package com.servegame.bl4de.Animation.command.animation.action;

import com.servegame.bl4de.Animation.command.AbstractRunnableCommand;
import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;

import java.util.Optional;

import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: PauseAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class PauseAnimation extends AbstractRunnableCommand<CommandSource> {

    public PauseAnimation(CommandSource src, CommandContext args) {
        super(src, args);
    }

    @Override
    public void run() {
        this.execute(this.src, this.args);
    }

    @Override
    public boolean checkPermission() {
        return false; // Not used
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
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
        Optional<Animation> animationOptional = AnimationController.getBareAnimation(animationNameOptional.get(), player.getUniqueId());
        if (!animationOptional.isPresent()){
            player.sendMessage(TextResponses.ANIMATION_NOT_FOUND_ERROR);
            return CommandResult.success();
        }
        Animation animation = animationOptional.get();

        if (animation.getStatus() == Animation.Status.PAUSED){
            // Animation is already paused
            player.sendMessage(TextResponses.ANIMATION_ALREADY_PAUSED);
            return CommandResult.success();
        } else if (animation.getStatus() == Animation.Status.STOPPED){
            // Animation isn't playing
            player.sendMessage(TextResponses.ANIMATION_ISNT_RUNNING);
            return CommandResult.success();
        }
        animation.pause();

        // Send a pause message to the users action bar
        Text pauseMessage = Text.of(
                NAME_COLOR, animation.getAnimationName(),
                PRIMARY_COLOR, " was ",
                ACTION_COLOR, "paused",
                PRIMARY_COLOR, ".");
        player.sendMessage(ChatTypes.ACTION_BAR, pauseMessage);

        return CommandResult.success();
    }
}
