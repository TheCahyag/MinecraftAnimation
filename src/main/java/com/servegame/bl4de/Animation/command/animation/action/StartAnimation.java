package com.servegame.bl4de.Animation.command.animation.action;

import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.command.AbstractRunnableCommand;
import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.exception.UninitializedException;
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
 * File: StartAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class StartAnimation extends AbstractRunnableCommand<CommandSource> {

    public StartAnimation(CommandSource src, CommandContext args){
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
        Optional<Animation> animationOptional = AnimationController.getAnimation(animationNameOptional.get(), player.getUniqueId());
        if (!animationOptional.isPresent()){
            player.sendMessage(TextResponses.ANIMATION_NOT_FOUND_ERROR);
            return CommandResult.empty();
        }
        Animation animation = animationOptional.get();

        if (!animation.isInitialized()){
            // Animation is not initialized
            player.sendMessage(TextResponses.ANIMATION_NOT_INITIALIZED_ERROR);
            return CommandResult.empty();
        }

        if (animation.getStatus() == Animation.Status.RUNNING){
            // Animation is already running
            player.sendMessage(TextResponses.ANIMATION_ALREADY_RUNNING);
            return CommandResult.empty();
        }

        // Parse flag arguments
        Optional<Integer> frameToStartOnOptional = args.getOne("frame");
        Optional<Integer> tickDelayOptional = args.getOne("delay");
        Optional<Integer> cyclesOptional = args.getOne("cycles");

        if (frameToStartOnOptional.isPresent()) {
            if (!player.hasPermission(Permissions.ANIMATION_SETTING_FRAME)) {
                // Check if the user doesn't have permissions for the -f flag
                player.sendMessage(Text.of(TextResponses.USER_DOESNT_HAVE_PERMISSION_FLAG_F));
                return CommandResult.empty();
            }
        }
        if (frameToStartOnOptional.isPresent()) {
            if (!player.hasPermission(Permissions.ANIMATION_SETTING_DELAY)) {
                // Check if the user doesn't have permissions for the -d flag
                player.sendMessage(Text.of(TextResponses.USER_DOESNT_HAVE_PERMISSION_FLAG_D));
                return CommandResult.empty();
            }
        }
        if (frameToStartOnOptional.isPresent()) {
            if (!player.hasPermission(Permissions.ANIMATION_SETTING_CYCLES)) {
                // Check if the user doesn't have permissions for the -c flag
                player.sendMessage(Text.of(TextResponses.USER_DOESNT_HAVE_PERMISSION_FLAG_C));
                return CommandResult.empty();
            }
        }

        tickDelayOptional.ifPresent(animation::setTickDelay);
        cyclesOptional.ifPresent(animation::setCycles);

        try {
            if (frameToStartOnOptional.isPresent()) {
                animation.start(frameToStartOnOptional.get());
            } else {
                animation.start();
            }

            // Send a start message to the users action bar
            Text startMessage = Text.of(
                    NAME_COLOR, animation.getAnimationName(),
                    PRIMARY_COLOR, " was ",
                    ACTION_COLOR, "started",
                    PRIMARY_COLOR, ".");
            player.sendMessage(ChatTypes.ACTION_BAR, startMessage);

        } catch (UninitializedException ue){
            player.sendMessage(TextResponses.ANIMATION_NOT_INITIALIZED_ERROR);
            return CommandResult.empty();
        }
        return CommandResult.success();

    }
}
