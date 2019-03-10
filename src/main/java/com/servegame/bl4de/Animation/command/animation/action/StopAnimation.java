package com.servegame.bl4de.Animation.command.animation.action;

import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.command.AbstractRunnableCommand;
import com.servegame.bl4de.Animation.controller.AnimationController;
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
 * File: StopAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class StopAnimation extends AbstractRunnableCommand<CommandSource> {

    public StopAnimation(CommandSource src, CommandContext args) {
        super(src, args);
    }

    @Override
    public boolean checkPermission() {
        boolean hasPermission = src.hasPermission(Permissions.ANIMATION_STOP);
        if (this.args.hasAny("admin_override")) {
            hasPermission = hasPermission && this.src.hasPermission(Permissions.ANIMATION_ADMIN_STOP);
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
            if (!player.hasPermission(Permissions.ANIMATION_ADMIN_STOP)){
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
            return CommandResult.success();
        }

        Animation animation = animationOptional.get();

        if (animation.getStatus() == Animation.Status.STOPPED){
            // Animation is already paused
            player.sendMessage(TextResponses.ANIMATION_ALREADY_STOPPED);
            return CommandResult.success();
        }

        animation.stop();

        // Send a stop message to the users action bar
        Text stopMessage = Text.of(
                NAME_COLOR, animation.getAnimationName(),
                PRIMARY_COLOR, " was ",
                ACTION_COLOR, "stopped",
                PRIMARY_COLOR, ".");
        player.sendMessage(ChatTypes.ACTION_BAR, stopMessage);

        return CommandResult.success();
    }


    /**
     * Method to
     * @param src
     * @param args
     * @return
     */
    private CommandResult executeAsConsole(CommandSource src, CommandContext args){
        src.sendMessage(Text.of("This is ran as the console :)"));
        return CommandResult.success();
    }
}
