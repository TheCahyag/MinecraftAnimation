package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.controller.FrameController;
import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: DuplicateFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class DuplicateFrame implements CommandExecutor {

    private Animation animation;

    public DuplicateFrame(Animation animation){
        this.animation = AnimationController.getAnimation(animation.getAnimationName(), animation.getOwner()).get();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = ((Player) src);
        if (this.animation.isRunning()){
            player.sendMessage(TextResponses.ANIMATION_CANT_BE_RUNNING);
            return CommandResult.success();
        }

        Optional<String> frameNameOptional = args.getOne("frame_name_num");
        if (!frameNameOptional.isPresent()){
            // Frame not specified
            player.sendMessage(TextResponses.FRAME_NOT_SPECIFIED_ERROR);
            return CommandResult.empty();
        }
        String frameName = frameNameOptional.get();
        Optional<Frame> frameOptional;

        if (isNumeric(frameName)){
            // User specified a frame number 0, 1, ..., n
            frameOptional = FrameController.getFrameWithContents(this.animation, Integer.parseInt(frameName));
        } else {
            // User specified a frame name
            frameOptional = FrameController.getFrameWithContents(this.animation, frameName);
        }

        if (!frameOptional.isPresent()){
            // Couldn't find a frame by the given name
            player.sendMessage(TextResponses.FRAME_NOT_FOUND_ERROR);
            return CommandResult.empty();
        }
        Frame theFrame = frameOptional.get(), newFrame = null;
        boolean frameInserted = false;
        int insertIndex = 0;
        try {
            newFrame = (Frame) frameOptional.get().clone();
            newFrame.setName(FrameController.generateFrameName(this.animation));
            Optional<String> optionalNum = args.getOne("num");
            if (optionalNum.isPresent()){
                // The user specified where to put the frame
                insertIndex = Integer.parseInt(optionalNum.get());
                this.animation.addFrame(newFrame, insertIndex);
                frameInserted = true;
            } else {
                // The user didn't specify a location to put the cloned frame
                int indexOfCloneSource = this.animation.getFrames().indexOf(theFrame);
                insertIndex = indexOfCloneSource + 1;
                this.animation.addFrame(newFrame, insertIndex);
                frameInserted = true;
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            player.sendMessage(Text.of(ERROR_COLOR, "Something has gone horribly wrong..."));
        } catch (UninitializedException e){
            player.sendMessage(TextResponses.FRAME_NOT_INITIALIZED_ERROR);
        } catch (NumberFormatException e){
            player.sendMessage(TextResponses.NUMBER_FORMAT_EXCEPTION);
        }
        if (frameInserted){
            // Frame went in just fine
            if (AnimationController.saveAnimation(this.animation)){
                Text message = Text.of(
                        PRIMARY_COLOR, "Frame '",
                        NAME_COLOR, theFrame.getName(),
                        PRIMARY_COLOR, "' ",
                        ACTION_COLOR, "duplicated",
                        PRIMARY_COLOR, " successfully. ",
                        ACTION_COLOR, "Inserted ",
                        PRIMARY_COLOR, "'",
                        NAME_COLOR, newFrame.getName(),
                        PRIMARY_COLOR, "' at index " + insertIndex + "."
                );
                player.sendMessage(message);
            } else {
                // The animation failed to save
                player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
                return CommandResult.empty();
            }
        } else {
            // The frame for whatever reason was never inserted
            player.sendMessage(TextResponses.FRAME_FAILED_DUPLICATION_ERROR);
            return CommandResult.empty();
        }
        return CommandResult.success();
    }
}
