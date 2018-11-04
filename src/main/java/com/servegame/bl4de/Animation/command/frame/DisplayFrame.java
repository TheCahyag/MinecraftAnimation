package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.command.AbstractCommand;
import com.servegame.bl4de.Animation.controller.FrameController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: DisplayFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class DisplayFrame extends AbstractCommand<CommandSource> {

    private Animation animation;

    public DisplayFrame(Animation animation, CommandSource src, CommandContext args){
        super(src, args);
        this.animation = animation;
    }

    @Override
    public String getPermission() {
        return Permissions.FRAME_DISPLAY;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = ((Player) src);
        if (this.animation.isRunning()){
            // A frame can't be displayed if the animation is running
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


        Frame frame = frameOptional.get();

        if (!frame.getContents().isPresent()) {
            // The frame has no contents
            player.sendMessage(TextResponses.FRAME_HAS_NO_CONTENT);
            return CommandResult.success();
        }

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
