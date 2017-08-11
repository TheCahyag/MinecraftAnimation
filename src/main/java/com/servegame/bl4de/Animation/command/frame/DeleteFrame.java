package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.util.AnimationUtil;
import com.servegame.bl4de.Animation.util.TextResponses;
import com.servegame.bl4de.Animation.util.Util;
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

/**
 * File: DeleteFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class DeleteFrame implements CommandExecutor {

    private Animation animation;

    public DeleteFrame(Animation animation){
        this.animation = animation;
    }

    /**
     * Takes a string and will parse the arguments
     * @param rawString
     * @return
     */
    public static CommandContext parseArguments(String rawString){
        return null;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = ((Player) src);

        // Get animation
        Optional<String> animationNameOptional = args.getOne("animation_name");
        if (!animationNameOptional.isPresent()){
            player.sendMessage(TextResponses.ANIMATION_NOT_SPECIFIED_ERROR);
            return CommandResult.empty();
        }
        Optional<Animation> animationOptional = AnimationUtil.getAnimation(animationNameOptional.get(), player.getUniqueId());
        if (!animationOptional.isPresent()){
            player.sendMessage(TextResponses.ANIMATION_NOT_FOUND_ERROR);
            return CommandResult.success();
        }
        Animation animation = animationOptional.get();

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
            Integer frameNum = Integer.valueOf(frameName);
            frameOptional = animation.getFrame(frameNum);
        } else {
            // They gave the frame name
            frameOptional = animation.getFrame(frameName);
        }

        if (!frameOptional.isPresent()){
            player.sendMessage(TextResponses.FRAME_NOT_FOUND_ERROR);
            return CommandResult.success();
        }
        Frame frameToDelete = frameOptional.get();

        // Parse arguments
        if (!args.hasAny("f")){
            // Check for the -f
            player.sendMessage(Text.of(Util.ERROR_COLOR, "If you sure you want to delete the '",
                    Util.NAME_COLOR, frameName,
                    Util.ERROR_COLOR, "' frame, run",
                    Util.PRIMARY_COLOR, " /animation " + animation.getAnimationName() + " frame delete " + frameName + " -f",
                    Util.FLAG_COLOR, " -f").toBuilder()
                    .append(Text.of(Util.ERROR_COLOR, Util.COMMAND_STYLE, ", or click this message."))
                    .onClick(TextActions.runCommand("/animation " + animation.getAnimationName() + " frame delete " + frameName + " -f"))
                    .onHover(Util.COMMAND_HOVER)
                    .build());
            return CommandResult.success();
        }

        animation.deleteFrame(frameToDelete);
        AnimationUtil.saveAnimation(animation);
        return CommandResult.success();
    }
}
