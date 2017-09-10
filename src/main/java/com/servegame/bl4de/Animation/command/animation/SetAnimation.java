package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.util.TextResponses;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import static com.servegame.bl4de.Animation.util.Util.*;

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

        // Potential flags
        boolean override = args.hasAny("f"); // -f flag

        if (this.animation.isRunning()){
            player.sendMessage(TextResponses.ANIMATION_CANT_BE_RUNNING);
            return CommandResult.success();
        }

        if (setPos1){
            // Get location
            Location<World> newLocation = player.getLocation();
            Optional<Location<World>> optionalOtherLocation = this.animation.getSubSpace().getCornerTwo();
            if (!override) {
                if (optionalOtherLocation.isPresent()) {
                    if (!this.checkVolume(src, newLocation, optionalOtherLocation.get())) {
                        // The max volume has been exceeded
                        return CommandResult.success();
                    }
                }
            }
            // Set the first position for the subspace
            this.animation.getSubSpace().setCornerOne(newLocation);
            if (AnimationController.saveAnimation(this.animation)){
                player.sendMessage(Text.of(PRIMARY_COLOR, "Position 1 set."));
                return CommandResult.success();
            } else {
                player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
                return CommandResult.empty();
            }
        } else if (setPos2){
            // Get location
            Location<World> newLocation = player.getLocation();
            Optional<Location<World>> optionalOtherLocation = this.animation.getSubSpace().getCornerOne();
            if (override) {
                if (optionalOtherLocation.isPresent()) {
                    if (!this.checkVolume(src, newLocation, optionalOtherLocation.get())) {
                        // The max volume has been exceeded
                        return CommandResult.success();
                    }
                }
            }
            // Set the second position for the subspace
            this.animation.getSubSpace().setCornerTwo(newLocation);
            if (AnimationController.saveAnimation(this.animation)){
                player.sendMessage(Text.of(PRIMARY_COLOR, "Position 2 set."));
                return CommandResult.success();
            } else {
                player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
                return CommandResult.empty();
            }
        } else if (setName){
            // Set the name of the animation
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
            if (AnimationController.getAnimationsByOwner(player.getUniqueId()).contains(newName)){
                // Name already exists
                player.sendMessage(TextResponses.ANIMATION_ALREADY_EXISTS_ERROR);
                return CommandResult.success();
            }
            // Make change and save animation
            this.animation.setAnimationName(newName);
            if (AnimationController.saveAnimation(this.animation)){
                // Animation changed and saved
                player.sendMessage(TextResponses.ANIMATION_SUCCESSFULLY_ALTERED);
            } else {
                // Animation wasn't saved
                player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
                return CommandResult.empty();
            }
            if (!AnimationController.deleteAnimation(this.animation)){
                // Couldn't delete the animation
                player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
                new Throwable().printStackTrace();
            }
        }
        return CommandResult.success();
    }

    /**
     * Checks the volume of a 3d subspace indicated by two opposite corners of the subspace.
     * If the volume exceeds the {@link Util#WARNING_VOLUME} it will tell the user that their
     * subspace is large, but will return true indicating the subspace is considered valid. If
     * the volume exceeds the {@link Util#MAX_VOLUME} if will tell the user their subspace is
     * too big and will return false indicating to not alter the animation.
     * @param src {@link Player} calling the command
     * @param location1 {@link Location}1
     * @param location2 {@link Location}2
     * @return true if the given subspace is valid, false if the given subspace is invalid
     */
    private boolean checkVolume(CommandSource src, Location location1, Location location2){
        if (calculateVolume(location1, location2) >= WARNING_VOLUME){
            if (calculateVolume(location1, location2) >= MAX_VOLUME){
                src.sendMessage(TextResponses.EXCEED_MAX_VOLUME);
                return false;
            }
            src.sendMessage(TextResponses.LARGE_VOLUME_WARNING);
        }
        return true;
    }
}
