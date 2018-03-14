package com.servegame.bl4de.Animation.command.animation.admin;

import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.command.AbstractRunnableCommand;
import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.TextResponses;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.servegame.bl4de.Animation.util.Util.*;
import static com.servegame.bl4de.Animation.util.Util.PRIMARY_COLOR;
import static com.servegame.bl4de.Animation.util.Util.SECONDARY_COLOR;

/**
 * File: ListAllAnimations.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class ListAllAnimations extends AbstractRunnableCommand<CommandSource> {

    public ListAllAnimations(CommandSource src, CommandContext args){
        super(src, args);
    }

    @Override
    public boolean checkPermission() {
        return this.src.hasPermission(Permissions.LIST_ALL_ANIMAITONS);
    }

    @Override
    public CommandResult runCommand() {
        if (!checkPermission()){
            this.src.sendMessage(TextResponses.USER_DOESNT_HAVE_PERMISSION);
            return CommandResult.empty();
        }
        return super.runCommand();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        // Check if the CommandSource is a player
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        // Get player UUID
        Player player = (Player) src;

        Optional<Map<UUID, ArrayList<String>>> animationMappingOptional = AnimationController.getAllAnimations();
        if (!animationMappingOptional.isPresent()){
            player.sendMessage(TextResponses.ANIMATIONS_FAILED_TO_LOAD);
            return CommandResult.empty();
        }
        Map<UUID, ArrayList<String>> animationMapping = animationMappingOptional.get();

        // This will have to be done later, but I need to iterate threw the entry set and print out the
        // animation listing as normal and additionally will need to tack on the players name at the end
        // So "animation name" (TheCahyag). I should probably remove the buttons with the listing right?
        // Still don't know if admins will have the right to play/edit/view info on other peoples animations

        boolean displayedAnimation = false;
        if (animationMapping.entrySet().size() != 0){
            Text message = Text.builder()
                    .append(Text.of(PRIMARY_COLOR, "All Animations:"))
                    .build();
            for (Map.Entry<UUID, ArrayList<String>> entry :
                    animationMapping.entrySet()) {
                UUID creatorUUID = entry.getKey();
                Optional<User> animationCreatorOptional = Util.getOfflinePlayer(creatorUUID, src);
                String playerName = animationCreatorOptional.map(User::getName).orElse("unknown player");
                ArrayList<String> animationsByPlayerName = entry.getValue();
                for (int i = 0; i < animationsByPlayerName.size(); i++) {
                    Optional<Animation> optionalAnimation = AnimationController.getBareAnimation(animationsByPlayerName.get(i), creatorUUID);
                    if (!optionalAnimation.isPresent()){
                        // There are no animations to display
                        continue;
                    }
                    displayedAnimation = true;
                    Animation animation = optionalAnimation.get();
                    message = message.toBuilder()
                            .append(Text.of(SECONDARY_COLOR, "\n(",
                                    NAME_COLOR, playerName,
                                    SECONDARY_COLOR, ") ",
                                    PRIMARY_COLOR, "| ",
                                    NAME_COLOR, animation.getAnimationName()))
                            .build();
                }
            }
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
        if (!displayedAnimation){
            // There were animations to display but weren't
            player.sendMessage(Text.of(PRIMARY_COLOR, "No animations were displayed."));
        }
        return CommandResult.success();
    }
}