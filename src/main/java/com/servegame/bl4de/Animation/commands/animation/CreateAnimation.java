package com.servegame.bl4de.Animation.commands.animation;

import com.servegame.bl4de.Animation.models.Animation;
import com.servegame.bl4de.Animation.util.AnimationUtil;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

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
            src.sendMessage(Text.of(TextColors.DARK_RED, "This command must be ran by a player in-game."));
            return CommandResult.success();
        }
        // Get player UUID
        Player player = (Player) src;
        UUID owner = player.getUniqueId();

        // Argument parsing
        Optional<String> animationNameOptional = args.getOne("name");
        if (!animationNameOptional.isPresent()){
            player.sendMessage(Text.of(TextColors.DARK_RED, "Animation name was not parsed correctly/wasn't specified."));
            return CommandResult.empty();
        }
        String animationName = animationNameOptional.get();

        // See if the animation exists already
        Optional<Animation> animationOptional = AnimationUtil.getAnimation(animationName, owner);
        if (animationOptional.isPresent()){
            // The animation already exists
            player.sendMessage(Text.of(TextColors.DARK_RED, "The specified animation already exists."));
            return CommandResult.success();
        }

        // Make the new animation
        Animation animation = new Animation(owner, animationName);
        if (AnimationUtil.saveAnimation(animation)){
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
            player.sendMessage(Text.of(TextColors.DARK_RED, "There was a problem saving the animation."));
            return CommandResult.empty();
        }
    }
}
