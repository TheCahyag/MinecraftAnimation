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
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;

import java.util.Optional;
import java.util.UUID;

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
    public boolean checkPermission() {
        boolean hasPermission = src.hasPermission(Permissions.ANIMATION_START);
        if (this.args.hasAny("admin_override")) {
            hasPermission = hasPermission && this.src.hasPermission(Permissions.ANIMATION_ADMIN_START);
        }
        return hasPermission;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof ConsoleSource){
            return this.executeAsConsole(src, args);
        } else if (!(src instanceof Player)){
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

        Optional<Animation> animationOptional;
        if (args.getOne("admin_override").isPresent()){
            // An admin is playing this without the owner
            if (!player.hasPermission(Permissions.ANIMATION_ADMIN_START)){
                // User doesn't have permission to play another persons animation
                player.sendMessage(TextResponses.USER_DOESNT_HAVE_PERMISSION_TO_INTERACT_WITH_ANIMATION);
                return CommandResult.success();
            }
            animationOptional = AnimationController.getAnimation(animationNameOptional.get(), (UUID) args.getOne("admin_override").get());
        } else {
            // Animation is being played regularly by the owner
            animationOptional = AnimationController.getAnimation(animationNameOptional.get(), player.getUniqueId());
        }

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
            player.sendMessage(TextResponses.ANIMATION_NOT_INITIALIZED_ERROR.concat(TextResponses.GENERIC_ERROR(ue.getMessage())));
            return CommandResult.empty();
        }
        return CommandResult.success();
    }

    private CommandResult executeAsConsole(CommandSource src, CommandContext args){
        // Get animation
        Optional<String> animationNameOptional = args.getOne("animation_name");
        Optional<String> animationOwnerOptional = args.getOne("animation_owner");
        if (!animationNameOptional.isPresent()){
            src.sendMessage(TextResponses.ANIMATION_NOT_SPECIFIED_ERROR);
            return CommandResult.empty();
        }

        if (!animationOwnerOptional.isPresent()){
            src.sendMessage(TextResponses.ANIMATION_OWNER_NOT_SPECIFIED_ERROR);
            return CommandResult.empty();
        }

        Optional<Animation> animationOptional = AnimationController.getAnimation(animationNameOptional.get(), UUID.fromString(animationOwnerOptional.get()));

        if (!animationOptional.isPresent()){
            src.sendMessage(TextResponses.ANIMATION_NOT_FOUND_ERROR);
            return CommandResult.empty();
        }
        final Animation animation = animationOptional.get();
        try {
            animation.start();

            // Send a start message to the users action bar
            Text startMessage = Text.of(
                    NAME_COLOR, animation.getAnimationName(),
                    PRIMARY_COLOR, " by ",
                    NAME_COLOR, animation.getOwner(),
                    PRIMARY_COLOR, " was ",
                    ACTION_COLOR, "started",
                    PRIMARY_COLOR, ".");
            src.sendMessage(startMessage);

        } catch (UninitializedException ue){
            src.sendMessage(TextResponses.ANIMATION_NOT_INITIALIZED_ERROR.concat(TextResponses.GENERIC_ERROR(ue.getMessage())));
            return CommandResult.empty();
        }

        return CommandResult.success();
    }

}
