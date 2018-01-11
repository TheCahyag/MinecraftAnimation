package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.controller.AnimationController;
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
import java.util.Optional;

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
        ArrayList<String> animationsByOwner = AnimationController.getAnimationsByOwner(player.getUniqueId());
        boolean displayedAAnimation = false;
        if (animationsByOwner.size() != 0){
            Text message = Text.builder()
                    .append(Text.of(PRIMARY_COLOR, "Animations:\n"))
                    .build();
            // This may be used when pagation is used for listing animations
            int animationsToList = animationsByOwner.size() > 10 ? 10 : animationsByOwner.size();
            for (int i = 0; i < animationsByOwner.size(); i++) {
                // Add all animations that are owned into a Text object
                Optional<Animation> optionalAnimation = AnimationController.getBareAnimation(animationsByOwner.get(i), player.getUniqueId());
                if (!optionalAnimation.isPresent()){
                    // There are no animations to display
                    if (AnimationPlugin.instance.isDebug()){
                        System.out.println("Not present");
                    }
                    continue;
                }
                displayedAAnimation = true;
                Animation animation = optionalAnimation.get();
                Text animationNameLink = Text.builder()
                        .append(Text.of(COMMAND_STYLE, NAME_COLOR, animationsByOwner.get(i)))
                        .onClick(TextActions.runCommand("/animate " + animationsByOwner.get(i) + " info"))
                        .onHover(TextActions.showText(TextResponses.ANIMATION_C2V_INFO))
                        .build();
                message = message.toBuilder()
                        .append(Text.of(AnimationController.getButtonsForAnimation(animation),
                                animationNameLink, "\n"))
                        .build();
            }
            message = message.toBuilder()
                    .append(Text.of(PRIMARY_COLOR, "Showing ",
                            ACTION_COLOR, animationsByOwner.size(),
                            PRIMARY_COLOR, " out of ",
                            ACTION_COLOR, animationsByOwner.size(),
                            PRIMARY_COLOR, " animations.\n"))
                    .build();
            message = message.toBuilder()
                    .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------\n",
                            AnimationController.getButtonsForList(), "\n",
                            Text.of(SECONDARY_COLOR, "----------------------------------------------------")))
                    .build();
            player.sendMessage(message);
        } else {
            // There are no animations to display
            player.sendMessage(Text.of(PRIMARY_COLOR, "There are no animations to display."));
            Text message = Text.builder()
                    .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------\n",
                            AnimationController.getButtonsForList(), "\n",
                            Text.of(SECONDARY_COLOR, "----------------------------------------------------")))
                    .build();
            player.sendMessage(message);
            return CommandResult.success();
        }
        if (!displayedAAnimation){
            // There were animations to display but weren't
            player.sendMessage(Text.of(PRIMARY_COLOR, "No animations were displayed."));
        }
        return CommandResult.success();
    }
}
