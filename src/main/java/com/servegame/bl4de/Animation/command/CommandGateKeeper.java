package com.servegame.bl4de.Animation.command;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.command.animation.BaseAnimation;
import com.servegame.bl4de.Animation.command.animation.InfoAnimation;
import com.servegame.bl4de.Animation.command.animation.SetAnimation;
import com.servegame.bl4de.Animation.command.animation.SettingsAnimation;
import com.servegame.bl4de.Animation.command.frame.*;
import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.scheduler.Task;

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
        boolean animationSetting = (boolean) args.getOne("animation_setting").orElse(false);
        boolean frame = (boolean) args.getOne("frame").orElse(false);

        if (!animationNameOptional.isPresent()){
            // Animation name wasn't specified
            new BaseAnimation().execute(src, args);
            return CommandResult.success();
        }
        String animationName = animationNameOptional.get();
        Optional<Animation> animationOptional = AnimationController.getBareAnimation(animationName, player.getUniqueId());
        if (!animationOptional.isPresent()){
            // Animation wasn't found
            player.sendMessage(TextResponses.ANIMATION_NOT_FOUND_ERROR);
            return CommandResult.success();
        }
        Animation animation = animationOptional.get();

        // Create a task for each frame giving proper delay times
        Task.Builder taskBuilder = Task.builder()
                .async();

        if (animationInfo){
            return new InfoAnimation(animation, src, args).runCommand();
        } else if (animationSet){
            return new SetAnimation(animation).execute(src, args);
        } else if (animationSetting) {
            return new SettingsAnimation(animation, src, args).runCommand();
        } else if (frame){
            boolean create      = (boolean) args.getOne("create").orElse(false);
            boolean delete      = (boolean) args.getOne("delete_frame").orElse(false);
            boolean display     = (boolean) args.getOne("display").orElse(false);
            boolean duplicate   = (boolean) args.getOne("duplicate").orElse(false);
            boolean update      = (boolean) args.getOne("update").orElse(false);
            boolean list        = (boolean) args.getOne("list").orElse(false);
            boolean frameInfo   = (boolean) args.getOne("frame_info").orElse(false);
            boolean setFrame    = (boolean) args.getOne("set").orElse(false);

            if (create)
                taskBuilder = taskBuilder.execute(new CreateFrame(animation, src, args));
            else if (delete)
                taskBuilder = taskBuilder.execute(new DeleteFrame(animation, src, args));
            else if (display)
                // This can't be ran async like the rest of the commands because it has block changes
                return new DisplayFrame(animation, src, args).runCommand();
            else if (duplicate)
                taskBuilder = taskBuilder.execute(new DuplicateFrame(animation, src, args));
            else if (frameInfo)
                taskBuilder = taskBuilder.execute(new InfoFrame(animation, src, args));
            else if (list)
                taskBuilder = taskBuilder.execute(new ListFrames(animation, src, args));
            else if (setFrame)
                taskBuilder = taskBuilder.execute(new SetFrame(animation, src, args));
            else if (update)
                taskBuilder = taskBuilder.execute(new UpdateFrame(animation, src, args));
        }
        taskBuilder.submit(AnimationPlugin.plugin);
        return CommandResult.success();
    }
}
