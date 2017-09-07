package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import static com.servegame.bl4de.Animation.util.Util.*;

import java.util.Optional;
import java.util.UUID;

/**
 * File: DeleteAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class DeleteAnimation implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = (Player) src;
        UUID owner = player.getUniqueId();
        Optional<String> animationNameOptional = args.getOne("animation_name");
        if (!animationNameOptional.isPresent()){
            player.sendMessage(TextResponses.ANIMATION_NOT_SPECIFIED_ERROR);
            return CommandResult.empty();
        }
        String animationName = animationNameOptional.get();
        Optional<Animation> animationOptional = AnimationController.getAnimation(animationName, owner);
        if (!animationOptional.isPresent()){
            // Couldn't find the given animation
            player.sendMessage(TextResponses.ANIMATION_NOT_FOUND_ERROR);
            return CommandResult.empty();
        }
        if (!args.hasAny("f")){
            // Check for the -f
            player.sendMessage(Text.of(ERROR_COLOR, "If you sure you want to delete the '",
                    NAME_COLOR, animationName,
                    ERROR_COLOR, "' animation, run",
                    PRIMARY_COLOR, " /animation delete " + animationName,
                    FLAG_COLOR, " -f").toBuilder()
                    .append(Text.of(ERROR_COLOR, COMMAND_STYLE, ", or click this message."))
                    .onClick(TextActions.runCommand("/animate delete " + animationName + " -f"))
                    .onHover(COMMAND_HOVER)
                    .build());
            return CommandResult.success();
        }

        Animation animation = animationOptional.get();
        if (animation.isRunning()){
            player.sendMessage(TextResponses.ANIMATION_CANT_BE_RUNNING);
            return CommandResult.success();
        }

        if (AnimationController.deleteAnimation(animation)){
            // Animation was deleted successfully
            player.sendMessage(Text.of(PRIMARY_COLOR, "Animation ",
                    PRIMARY_COLOR, "'",
                    NAME_COLOR, animationName,
                    PRIMARY_COLOR, "' ",
                    ACTION_COLOR, "deleted ",
                    PRIMARY_COLOR, "successfully."));
            return CommandResult.success();
        } else {
            // Animation was not deleted successfully
            player.sendMessage(TextResponses.ANIMATION_DELETE_ERROR);
            return CommandResult.empty();
        }
    }
}
