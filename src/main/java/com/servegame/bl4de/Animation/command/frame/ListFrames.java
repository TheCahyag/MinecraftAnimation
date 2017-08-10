package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.models.Animation;
import com.servegame.bl4de.Animation.models.Frame;
import com.servegame.bl4de.Animation.util.AnimationUtil;
import com.servegame.bl4de.Animation.util.FrameUtil;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;

import static com.servegame.bl4de.Animation.util.Util.*;
import static org.spongepowered.api.text.format.TextColors.*;

/**
 * File: ListFrames.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class ListFrames implements CommandExecutor {

    private Animation animation;

    public ListFrames(Animation animation){
        this.animation = animation;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        // Created header for the frame list
        Text message = Text.builder()
                .append(Text.of(PRIMARY_COLOR, "Animation",
                        WHITE, ": "))
                .append(AnimationUtil.linkToAnimationInfo(animation))
                .append(Text.of(PRIMARY_COLOR, "\n# of frames",
                        WHITE, ": ",
                        SECONDARY_COLOR, this.animation.getNumOfFrames() + "\n",
                        PRIMARY_COLOR, "Frames",
                        WHITE, ":\n"))
                .build();

        // List all the frames
        ArrayList<Frame> frames = this.animation.getFrames();
        for (int i = 0; i < this.animation.getNumOfFrames(); i++) {
            message = Text.builder()
                    .append(Text.of(PRIMARY_COLOR, i,
                            WHITE, ": "))
                    .append(FrameUtil.linkToFrameInfo(frames.get(i), animation))
                    .append(Text.of("\n"))
                    .build();
        }

        // Add buttons for the display
        message = message.toBuilder()
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------\n"))
                .append(FrameUtil.getButtonsForList(animation))
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------"))
                .build();

        src.sendMessage(message);
        return CommandResult.success();
    }
}
