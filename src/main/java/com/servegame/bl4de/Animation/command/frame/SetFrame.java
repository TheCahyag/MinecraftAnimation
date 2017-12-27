package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
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

import static com.servegame.bl4de.Animation.util.Util.NAME_COLOR;
import static com.servegame.bl4de.Animation.util.Util.PRIMARY_COLOR;

/**
 * File: SetFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SetFrame implements CommandExecutor {

    private Animation animation;

    public SetFrame(Animation animation){
        this.animation = animation;
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

        // Get the frame
        Optional<String> frameNameOptional = args.getOne("frame_name_num");
        if (!frameNameOptional.isPresent()){
            // No frame name
            player.sendMessage(TextResponses.FRAME_NOT_SPECIFIED_ERROR);
            return CommandResult.empty();
        }
        Optional<Frame> frameOptional;
        String frameNameNum = frameNameOptional.get();
        if (Util.isNumeric(frameNameNum)){
            frameOptional = this.animation.getFrame(Integer.parseInt(frameNameNum));
        } else {
            frameOptional = this.animation.getFrame(frameNameNum);
        }

        if (!frameOptional.isPresent()){
            // The frame wasn't found in this animation
            player.sendMessage(TextResponses.FRAME_NOT_FOUND_ERROR);
            return CommandResult.empty();
        }

        Frame frame = frameOptional.get();

        // Parse arguments
        boolean setName = (boolean) args.getOne("name").orElse(false);

        if (setName){
            // Get new frame name
            Optional<String> newFrameNameOptional = args.getOne("new_name");
            if (!newFrameNameOptional.isPresent()){
                player.sendMessage(TextResponses.GENERAL_ARGUMENTS_INCORRECT);
                return CommandResult.empty();
            }
            // Modify the frame and save the frame that is being changed
            frame.setName(newFrameNameOptional.get());
            if (AnimationController.saveAnimation(this.animation)){
                Text message = Text.of(
                        PRIMARY_COLOR, "Frame has been renamed and is now known as '",
                        NAME_COLOR, frame.getName(),
                        PRIMARY_COLOR, "'",
                        PRIMARY_COLOR, ". "
                );
                player.sendMessage(message);
                return CommandResult.success();
            } else {
                player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
                return CommandResult.empty();
            }
        }
        return CommandResult.success();
    }
}
