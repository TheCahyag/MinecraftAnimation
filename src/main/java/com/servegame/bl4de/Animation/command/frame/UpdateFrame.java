package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.command.AbstractRunnableCommand;
import com.servegame.bl4de.Animation.controller.FrameController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.util.TextResponses;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.Optional;

import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: UpdateFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class UpdateFrame extends AbstractRunnableCommand<CommandSource> {

    private Animation animation;

    public UpdateFrame(Animation animation, CommandSource src, CommandContext args){
        super(src, args);
        this.animation = animation;
    }

    @Override
    public void run() {
        this.execute(this.src, this.args);
    }

    @Override
    public String getPermission() {
        return Permissions.FRAME_UPDATE;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
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

        frame.setContents(Util.copyWorldToSubSpace(frame.getCornerOne().get(), frame.getCornerTwo().get()));

        // Save the Animation
        if (FrameController.saveFrame(this.animation, frame)){
            // Animation was saved successfully
            Text message = Text.builder()
                    .append(Text.of(PRIMARY_COLOR, "Frame ",
                            PRIMARY_COLOR, "'"))
                    .append(Text.builder()
                            .append(Text.of(NAME_COLOR, COMMAND_STYLE, frame.getName()))
                            .onClick(TextActions.runCommand("/animate " + this.animation.getAnimationName() + " frame " + frame.getName() + " info"))
                            .onHover(TextActions.showText(TextResponses.FRAME_C2V_INFO))
                            .build())
                    .append(Text.of(PRIMARY_COLOR, "' ",
                            ACTION_COLOR, "updated ",
                            PRIMARY_COLOR, "successfully."))
                    .build();
            player.sendMessage(message);
            return CommandResult.success();
        } else {
            // There was a problem saving the animation
            player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
            return CommandResult.empty();
        }
    }
}
