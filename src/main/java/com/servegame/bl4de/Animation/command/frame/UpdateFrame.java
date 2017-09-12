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

/**
 * File: UpdateFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class UpdateFrame implements CommandExecutor {

    private Animation animation;

    public UpdateFrame(Animation animation){
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

//        int frameIndex = this.animation.getIndexOfFrame(frame);

        frame.setContents(Util.copyWorldToSubSpace(frame.getCornerOne().get(), frame.getCornerTwo().get()));

        System.out.println(this.animation.getFrame(frame.getName()));

        // Save the Animation
        if (AnimationController.saveAnimation(this.animation)){
            // Animation was created and saved successfully
            player.sendMessage(Text.of(Util.PRIMARY_COLOR, "Frame ",
                    Util.PRIMARY_COLOR, "'",
                    Util.NAME_COLOR, frame.getName(),
                    Util.PRIMARY_COLOR, "' ",
                    Util.ACTION_COLOR, "updated ",
                    Util.PRIMARY_COLOR, "successfully."));
            return CommandResult.success();
        } else {
            // There was a problem creating the animation
            player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
            return CommandResult.empty();
        }
    }
}
