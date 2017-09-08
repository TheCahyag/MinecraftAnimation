package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.controller.FrameController;
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
 * File: DisplayFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class DisplayFrame implements CommandExecutor {

    private Animation animation;

    public DisplayFrame(Animation animation){
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
            frameOptional = this.animation.getFrame(Integer.parseInt(frameName));
        } else {
            // User specified a frame name
            frameOptional = this.animation.getFrame(frameName);
        }

        if (!frameOptional.isPresent()){
            // Couldn't find a frame by the given name
            player.sendMessage(TextResponses.FRAME_NOT_FOUND_ERROR);
            return CommandResult.empty();
        }


        Frame frame = frameOptional.get();

        if (FrameController.displayContents(frame)){
            Text message = Text.of(
                    PRIMARY_COLOR, "Frame '",
                    NAME_COLOR, frame.getName(),
                    PRIMARY_COLOR, "' has been ",
                    ACTION_COLOR, "displayed",
                    PRIMARY_COLOR, "."
            );
            player.sendMessage(message);
        } else {
            player.sendMessage(TextResponses.FRAME_NOT_DISPLAYED_ERROR);
            return CommandResult.empty();
        }
        return CommandResult.success();
    }
}
