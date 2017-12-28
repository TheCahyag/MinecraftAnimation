package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.Optional;

import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: DeleteFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class DeleteFrame implements CommandExecutor {

    private Animation animation;

    public DeleteFrame(Animation animation){
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

        // Get Frame
        Optional<String> frameNameOptional = args.getOne("frame_name_num");
        if (!frameNameOptional.isPresent()){
            // Argument wasn't parsed correctly
            player.sendMessage(TextResponses.FRAME_NOT_SPECIFIED_ERROR);
            return CommandResult.success();
        }
        String frameName = frameNameOptional.get();
        Optional<Frame> frameOptional;
        if (StringUtils.isNumeric(frameName)){
            // They gave the frame number
            frameOptional = this.animation.getFrame(Integer.valueOf(frameName));
        } else {
            // They gave the frame name
            frameOptional = this.animation.getFrame(frameName);
        }

        if (!frameOptional.isPresent()){
            player.sendMessage(TextResponses.FRAME_NOT_FOUND_ERROR);
            return CommandResult.success();
        }
        Frame frameToDelete = frameOptional.get();

        // Parse flag argument
        if (!args.hasAny("f")){
            // Check for the -f
            player.sendMessage(Text.of(ERROR_COLOR, "If you sure you want to delete the '",
                    NAME_COLOR, frameName,
                    ERROR_COLOR, "' frame, run",
                    PRIMARY_COLOR, " /animation " + animation.getAnimationName() + " frame delete " + frameName,
                    FLAG_COLOR, " -f").toBuilder()
                    .append(Text.of(ERROR_COLOR, COMMAND_STYLE, ", or click this message."))
                    .onClick(TextActions.runCommand("/animation " + animation.getAnimationName() + " frame delete_frame " + frameName + " -f"))
                    .onHover(COMMAND_HOVER)
                    .build());
            return CommandResult.success();
        }

        this.animation.deleteFrame(frameToDelete);
        if (AnimationController.saveAnimation(this.animation)){
            // Animation changed and saved
            Text message = Text.builder()
                    .append(Text.of(PRIMARY_COLOR, "Frame '",
                            NAME_COLOR, frameToDelete.getName(),
                            PRIMARY_COLOR, "' ",
                            ACTION_COLOR, "deleted ",
                            PRIMARY_COLOR, "successfully."))
                    .build();
            player.sendMessage(message);
        } else {
            // Animation wasn't saved
            player.sendMessage(TextResponses.FRAME_DELETE_ERROR);
            return CommandResult.empty();
        }
        return CommandResult.success();
    }
}
