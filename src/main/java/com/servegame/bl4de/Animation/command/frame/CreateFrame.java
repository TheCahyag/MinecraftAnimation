package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.models.Animation;
import com.servegame.bl4de.Animation.models.Frame;
import com.servegame.bl4de.Animation.util.AnimationUtil;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import static com.servegame.bl4de.Animation.util.Util.*;

import java.util.Optional;

/**
 * File: CreateFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class CreateFrame implements CommandExecutor {

    private Animation animation;

    public CreateFrame(Animation animation){
        this.animation = animation;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = ((Player) src);
        Optional<String> frameNameOptional = args.getOne("frame_name");
        Frame frame;
        try {
            // Make the new frame and insert it into the animation
            if (frameNameOptional.isPresent()){
                frame = this.animation.getBlankFrame(player.getUniqueId(), frameNameOptional.get());
                this.animation.addFrame(frame);
            } else {
                frame = this.animation.getBlankFrame(player.getUniqueId());
                this.animation.addFrame(frame);
            }
        } catch (UninitializedException uie){
            player.sendMessage(TextResponses.ANIMATION_NOT_INITIALIZED_ERROR);
            player.sendMessage(Text.of("    ", uie.getDetailText()));
            return CommandResult.success();
        }
        if (AnimationUtil.saveAnimation(this.animation)){
            // Animation was updated and saved
            player.sendMessage(Text.of(PRIMARY_COLOR, "Frame ",
                    PRIMARY_COLOR, "'",
                    NAME_COLOR, frame.getName(),
                    PRIMARY_COLOR, "' ",
                    ACTION_COLOR, "created ",
                    PRIMARY_COLOR, "successfully."));
            return CommandResult.success();
        } else {
            // There was a problem saving the animation
            player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
            return CommandResult.empty();
        }
    }
}
