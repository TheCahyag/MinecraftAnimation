package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.command.AbstractRunnableCommand;
import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.controller.FrameController;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.List;

import static com.servegame.bl4de.Animation.util.Util.*;
import static org.spongepowered.api.text.format.TextColors.*;

/**
 * File: ListFrames.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class ListFrames extends AbstractRunnableCommand<CommandSource> {

    private Animation animation;

    public ListFrames(Animation animation, CommandSource src, CommandContext args){
        super(src, args);
        this.animation = animation;
    }

    @Override
    public void run() {
        this.execute(this.src, this.args);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {

        // Created header for the frame list
        Text message = Text.builder()
                .append(Text.of(PRIMARY_COLOR, "Animation",
                        WHITE, ": "))
                .append(AnimationController.linkToAnimationInfo(animation))
                .append(Text.of(PRIMARY_COLOR, "\n# of frames",
                        WHITE, ": ",
                        SECONDARY_COLOR, this.animation.getNumOfFrames() + "\n",
                        PRIMARY_COLOR, "Frames",
                        WHITE, ":\n"))
                .build();

        // List all the frames
        List<Frame> frames = this.animation.getFrames();

        if (AnimationPlugin.instance.isDebug()){
            System.out.println("Animation from frame: ---------------------");
            System.out.println(this.animation);
            for (Frame f :
                    frames) {
                System.out.println("Frame: " + f);
            }
        }

        for (int i = 0; i < this.animation.getNumOfFrames(); i++) {
            message = message.toBuilder()
                    .append(Text.of(PRIMARY_COLOR, i,
                            WHITE, ": "))
                    .append(FrameController.linkToFrameInfo(frames.get(i), this.animation))
                    .append(Text.of("\n"))
                    .build();
        }

        // Add buttons for the display
        message = message.toBuilder()
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------\n"))
                .append(FrameController.getButtonsForList(animation))
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------"))
                .build();

        src.sendMessage(message);
        return CommandResult.success();
    }
}
