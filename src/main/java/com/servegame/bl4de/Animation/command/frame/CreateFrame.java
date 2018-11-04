package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.command.AbstractRunnableCommand;
import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.Optional;

import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: CreateFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class CreateFrame extends AbstractRunnableCommand<CommandSource> {

    private Animation animation;

    public CreateFrame(Animation animation, CommandSource src, CommandContext args){
        super(src, args);
        this.animation = AnimationController.getAnimation(animation.getAnimationName(), animation.getOwner()).get();
    }

    @Override
    public void run() {
        this.execute(this.src, this.args);
    }

    @Override
    public String getPermission() {
        return Permissions.FRAME_CREATE;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }

        Player player = ((Player) src);
        Optional<String> frameNameOptional = args.getOne("frame_name");
        Frame frame;

        if (this.animation.isRunning()){
            player.sendMessage(TextResponses.ANIMATION_CANT_BE_RUNNING);
            return CommandResult.success();
        }

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
        if (AnimationController.saveAnimation(this.animation)){
            // Animation was updated and saved
            Text message = Text.builder()
                    .append(Text.of(PRIMARY_COLOR, "Frame ",
                            PRIMARY_COLOR, "'"))
                    .append(Text.builder()
                            .append(Text.of(NAME_COLOR, COMMAND_STYLE, frame.getName()))
                            .onClick(TextActions.runCommand("/animate " + this.animation.getAnimationName() + " frame " + frame.getName() + " info"))
                            .onHover(TextActions.showText(TextResponses.FRAME_C2V_INFO))
                            .build())
                    .append(Text.of(PRIMARY_COLOR, "' ",
                            ACTION_COLOR, "created ",
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
