package com.servegame.bl4de.Animation.command.animation;

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

import java.util.Optional;

/**
 * File: SetAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SetAnimation implements CommandExecutor {

    private Animation animation;

    public SetAnimation(Animation animation){
        this.animation = animation;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = ((Player) src);

        // Parse arguments
        boolean setPos1 = (boolean) args.getOne("pos1").orElse(false);
        boolean setPos2 = (boolean) args.getOne("pos2").orElse(false);
        boolean setName = (boolean) args.getOne("set_name").orElse(false);

        if (setPos1){
            this.animation.getSubSpace().setCornerOne(player.getLocation());
            if (AnimationUtil.saveAnimation(this.animation)){
                player.sendMessage(Text.of(Util.PRIMARY_COLOR, "Position 1 set."));
                return CommandResult.success();
            } else {
                player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
                return CommandResult.empty();
            }
        } else if (setPos2){
            this.animation.getSubSpace().setCornerTwo(player.getLocation());
            if (AnimationUtil.saveAnimation(this.animation)){
                player.sendMessage(Text.of(Util.PRIMARY_COLOR, "Position 2 set."));
                return CommandResult.success();
            } else {
                player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
                return CommandResult.empty();
            }
        } else if (setName){
            Optional<String> newNameOptional = args.getOne("new_name");
            if (!newNameOptional.isPresent()){
                // No name was found in the arguments
                player.sendMessage(TextResponses.GENERAL_NO_NAME_ERROR);
                return CommandResult.empty();
            }
            String newName = newNameOptional.get();
            if (this.animation.getAnimationName().equals(newName)){
                // The name is the same as current
                player.sendMessage(TextResponses.GENERAL_INDIFFERENT_NAME_ERROR);
                return CommandResult.success();
            }
            if (AnimationUtil.getAnimationsByOwner(player.getUniqueId()).contains(newName)){
                // Name already exists
                player.sendMessage(TextResponses.ANIMATION_ALREADY_EXISTS_ERROR);
                return CommandResult.success();
            }
            // Make change and save animation
            this.animation.setAnimationName(newName);
            if (AnimationUtil.saveAnimation(this.animation)){
                // Animation changed and saved
                player.sendMessage(TextResponses.ANIMATION_SUCCESSFULLY_ALTERED);
            } else {
                // Animation wasn't saved
                player.sendMessage(TextResponses.GENERAL_PROBLEM_RUNNING_COMMAND_ERROR);
                return CommandResult.empty();
            }
            if (!AnimationUtil.deleteAnimation(this.animation)){
                // Couldn't delete the animation
                player.sendMessage(TextResponses.GENERAL_PROBLEM_RUNNING_COMMAND_ERROR);
                new Throwable().printStackTrace();
            }
        }
        return CommandResult.success();
    }
}
