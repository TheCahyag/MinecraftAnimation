package com.servegame.bl4de.Animation.commands.animation;

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

import java.util.ArrayList;

/**
 * File: ListAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class ListAnimation implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        // Check if the CommandSource is a player
        if (!(src instanceof Player)){
            src.sendMessage(Text.of(TextColors.DARK_RED, "This command must be ran by a player in-game."));
            return CommandResult.success();
        }
        Player player = (Player) src;
        ArrayList<String> animationsByOwner = AnimationUtil.getAnimationsByOwner(player.getUniqueId());
        Text message = Text.builder()
                .append(Text.of(Util.PRIMARY_COLOR, "Animations:\n"))
                .build();
        for (int i = 0; i < animationsByOwner.size(); i++) {
            // Add all animations that are owned into a Text object
            message = message.toBuilder()
                    .append(Text.of(Util.NAME_COLOR, animationsByOwner.get(i) + "\n"))
                    .build();
        }
        player.sendMessage(message);
        return CommandResult.success();
    }
}
