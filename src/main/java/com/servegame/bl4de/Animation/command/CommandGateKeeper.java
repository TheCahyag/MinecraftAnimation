package com.servegame.bl4de.Animation.command;

import com.servegame.bl4de.Animation.command.animation.InfoAnimation;
import com.servegame.bl4de.Animation.command.animation.SetAnimation;
import com.servegame.bl4de.Animation.command.frame.*;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.AnimationUtil;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Optional;

/**
 * File: CommandGateKeeper.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class CommandGateKeeper implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = (Player) src;

        // Get essential arguments
        Optional<String> animationNameOptional = args.getOne("animation_name");
        boolean animationInfo = (boolean) args.getOne("animation_info").orElse(false);
        boolean animationSet = (boolean) args.getOne("animation_set").orElse(false);
        boolean frame = (boolean) args.getOne("frame").orElse(false);

        if (!animationNameOptional.isPresent()){
            // Animation name wasn't parsed correctly
            player.sendMessage(TextResponses.ANIMATION_NOT_SPECIFIED_ERROR);
            return CommandResult.empty();
        }
        String animationName = animationNameOptional.get();
        Optional<Animation> animationOptional = AnimationUtil.getAnimation(animationName, player.getUniqueId());
        if (!animationOptional.isPresent()){
            // Animation wasn't found
            player.sendMessage(TextResponses.ANIMATION_NOT_FOUND_ERROR);
            return CommandResult.success();
        }
        Animation animation = animationOptional.get();
        if (animationInfo){
            return new InfoAnimation(animation).execute(src, args);
        } else if (animationSet){
            return new SetAnimation(animation).execute(src, args);
        } else if (frame){
            boolean create      = (boolean) args.getOne("create").orElse(false);
            boolean delete      = (boolean) args.getOne("delete").orElse(false);
            boolean display     = (boolean) args.getOne("display").orElse(false);
            boolean duplicate   = (boolean) args.getOne("duplicate").orElse(false);
            boolean update      = (boolean) args.getOne("update").orElse(false);
            boolean list        = (boolean) args.getOne("list").orElse(false);
            boolean frameInfo   = (boolean) args.getOne("frame_info").orElse(false);

            if (create)
                return new CreateFrame(animation).execute(src, args);
            else if (delete)
                return new DeleteFrame(animation).execute(src, args);
            else if (display)
                return new DisplayFrame(animation).execute(src, args);
            else if (duplicate)
                return new DuplicateFrame(animation).execute(src, args);
            else if (update)
                return new UpdateFrame(animation).execute(src, args);
            else if (list)
                return new ListFrames(animation).execute(src, args);
            else if (frameInfo)
                return new InfoFrame(animation).execute(src, args);
        }
        return CommandResult.success();
    }
}
