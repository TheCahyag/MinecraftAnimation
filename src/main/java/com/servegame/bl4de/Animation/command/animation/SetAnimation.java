package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.controller.FrameController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.util.TextResponses;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

import static com.servegame.bl4de.Animation.util.Util.*;

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
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = ((Player) src);

        // Parse arguments
        boolean setPos1 = (boolean) args.getOne("pos1").orElse(false);
        boolean setPos2 = (boolean) args.getOne("pos2").orElse(false);
        boolean setPos = setPos1 || setPos2;
        boolean setName = (boolean) args.getOne("set_name").orElse(false);

        // Potential flags
        boolean override = args.hasAny("f"); // -f flag

        if (this.animation.isRunning()){
            player.sendMessage(TextResponses.ANIMATION_CANT_BE_RUNNING);
            return CommandResult.success();
        }

        if (setPos){
            // The player is setting one of the animations corners
            if (!player.hasPermission(Permissions.ANIMATION_SET_POS)){
                // The player doesn't have permissions to run this command
                player.sendMessage(TextResponses.USER_DOESNT_HAVE_PERMISSION);
                return CommandResult.empty();
            }

            // Get location of the corner
            Location<World> newLocation = player.getLocation();

            // Get the other corner to calculate the volume of the subspace
            Optional<Location<World>> optionalOtherLocation;
            if (setPos1){
                optionalOtherLocation = this.animation.getSubSpace().getCornerTwo();
            } else {
                optionalOtherLocation = this.animation.getSubSpace().getCornerOne();
            }

            if (!override) {
                if (optionalOtherLocation.isPresent()) {
                    if (!this.checkVolume(src, newLocation, optionalOtherLocation.get())) {
                        // The max volume has been exceeded, the message get's sent in checkVolume()
                        return CommandResult.success();
                    }
                }
            }

            if (this.animation.getSubSpace().getCornerOne().isPresent()
                    && this.animation.getSubSpace().getCornerTwo().isPresent()
                    && this.animation.getFrames().size() > 0){
                // The player is trying to override the corners of an animation that has frames
                boolean singleFrameWithContents = false;

                for (Frame frame :
                        this.animation.getFrames()) {
                    Frame fullFrame = FrameController.getFrameWithContents(this.animation, frame.getName()).get();
                    if (fullFrame.getSubspace().getContents().isPresent()){
                        singleFrameWithContents = true;
                    }
                }
                if (singleFrameWithContents) {
                    // Warn the player that they can not change the corners of the animation if they have frames
                    src.sendMessage(Text.builder()
                            .append(Text.of(WARNING_COLOR, "This animation already has its corners set. " +
                                            "Resetting the bounds of this animation could lead to data lose in " +
                                            "the already created frames. Please remove frames before resetting the corners."))
                            .build()
                    );
                    return CommandResult.success();
                }
            }
            // No overriding is happening and we can set the corners normally
            if (setPos1) {
                // Set the first position for the subspace
                this.animation.getSubSpace().setCornerOne(newLocation);
            } else {
                // Set the second position for the subspace
                this.animation.getSubSpace().setCornerTwo(newLocation);
            }

            for (Frame frame :
                    this.animation.getFrames()) {
                frame.setCornerOne(this.animation.getSubSpace().getCornerOne().get());
                frame.setCornerTwo(this.animation.getSubSpace().getCornerTwo().get());
            }

            if (AnimationController.saveAnimation(this.animation)){
                String oneOrTwo = setPos1 ? "1" : "2";
                player.sendMessage(Text.of(PRIMARY_COLOR, "Position " + oneOrTwo + " set."));
                return CommandResult.success();
            } else {
                player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
                return CommandResult.empty();
            }
        } else if (setName){
            if (!player.hasPermission(Permissions.ANIMATION_SET_NAME)){
                // The player doesn't have permissions to run this command
                player.sendMessage(TextResponses.USER_DOESNT_HAVE_PERMISSION);
                return CommandResult.empty();
            }

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
                return CommandResult.empty();
            }
            if (AnimationController.getAnimationsByOwner(player.getUniqueId()).contains(newName)){
                // Name already exists
                player.sendMessage(TextResponses.ANIMATION_ALREADY_EXISTS_ERROR);
                return CommandResult.empty();
            }
            // Rename animation
            if (AnimationController.renameAnimation(this.animation, newName)){
                // Animation changed and saved
                player.sendMessage(TextResponses.ANIMATION_SUCCESSFULLY_ALTERED);
            } else {
                // Animation wasn't saved
                player.sendMessage(TextResponses.ANIMATION_FAILED_TO_RENAME);
                return CommandResult.empty();
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
