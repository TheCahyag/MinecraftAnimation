package com.servegame.bl4de.Animation.commands.animation;

import com.servegame.bl4de.Animation.models.Animation;
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
import org.spongepowered.api.text.action.TextActions;

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
        Optional<String> animationNameOptional = args.getOne("name");
        if (!animationNameOptional.isPresent()){
            player.sendMessage(TextResponses.ANIMATION_NOT_SPECIFIED_ERROR);
            return CommandResult.empty();
        }
        String animationName = animationNameOptional.get();
        Optional<Animation> animationOptional = AnimationUtil.getAnimation(animationName, owner);
        if (!animationOptional.isPresent()){
            // Couldn't find the given animation
            player.sendMessage(TextResponses.ANIMATION_NOT_FOUND_ERROR);
            return CommandResult.empty();
        }
        if (!args.hasAny("f")){
            // Check for the -f
            player.sendMessage(Text.of(Util.ERROR_COLOR, "If you sure you want to delete the '",
                    Util.NAME_COLOR, animationName,
                    Util.ERROR_COLOR, "' animation, run",
                    Util.PRIMARY_COLOR, " /animation delete " + animationName,
                    Util.FLAG_COLOR, " -f").toBuilder()
                    .append(Text.of(Util.ERROR_COLOR, Util.COMMAND_STYLE, ", or click this message."))
                    .onClick(TextActions.runCommand("/animate delete " + animationName + " -f"))
                    .onHover(Util.COMMAND_HOVER)
                    .build());
            return CommandResult.success();
        }
        if (AnimationUtil.deleteAnimation(animationOptional.get())){
            // Animation was deleted successfully
            player.sendMessage(Text.of(Util.PRIMARY_COLOR, "Animation ",
                    Util.PRIMARY_COLOR, "'",
                    Util.NAME_COLOR, animationName,
                    Util.PRIMARY_COLOR, "' ",
                    Util.ACTION_COLOR, "deleted ",
                    Util.PRIMARY_COLOR, "successfully."));
            return CommandResult.success();
        } else {
            // Animation was not deleted successfully
            player.sendMessage(TextResponses.ANIMATION_DELETE_ERROR);
            return CommandResult.empty();
        }
    }
}
