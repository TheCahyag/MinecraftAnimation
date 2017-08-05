package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.models.Animation;
import com.servegame.bl4de.Animation.util.AnimationUtil;
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
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = (Player) src;
        ArrayList<String> animationsByOwner = AnimationUtil.getAnimationsByOwner(player.getUniqueId());

        if (animationsByOwner.size() != 0){
            Text message = Text.builder()
                    .append(Text.of(PRIMARY_COLOR, "Animations:\n"))
                    .build();
            for (int i = 0; i < animationsByOwner.size(); i++) {
                // Add all animations that are owned into a Text object
                Animation animation = AnimationUtil.getAnimation(animationsByOwner.get(i), player.getUniqueId()).get();
                Text animationNameLink = Text.builder()
                        .append(Text.of(COMMAND_STYLE, NAME_COLOR, animationsByOwner.get(i)))
                        .onClick(TextActions.runCommand("/animate " + animationsByOwner.get(i) + " info"))
                        .build();
                message = message.toBuilder()
                        .append(Text.of(AnimationUtil.getButtonsForAnimation(animation),
                                animationNameLink, "\n"))
                        .build();
            }
            message = message.toBuilder()
                    .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------\n",
                            AnimationUtil.getButtonsForList(), "\n",
                            Text.of(SECONDARY_COLOR, "----------------------------------------------------")))
                    .build();
            player.sendMessage(message);
        } else {
            // There are no animations to display
            player.sendMessage(Text.of(PRIMARY_COLOR, "There are no animations to display."));
        }
        return CommandResult.success();
    }
}
