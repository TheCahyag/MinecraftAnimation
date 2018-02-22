package com.servegame.bl4de.Animation.command.animation.action.admin;

import com.servegame.bl4de.Animation.command.AbstractRunnableCommand;
import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: StopAllAnimations.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class StopAllAnimations extends AbstractRunnableCommand<CommandSource> {

    public StopAllAnimations(CommandSource src, CommandContext args) {
        super(src, args);
    }

    @Override
    public void run() {
        this.execute(this.src, this.args);
    }

    @Override
    public boolean checkPermission() {
        return false; // Not used
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (src instanceof Player){
            src.sendMessage(TextResponses.CONSOLE_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }

        if (!args.hasAny("f")){
            // Check for the -f
            src.sendMessage(Text.of(ERROR_COLOR, "Are you sure you want to stop all the animations? If so type",
                    PRIMARY_COLOR, " /animation stopall",
                    FLAG_COLOR, " -f").toBuilder()
                    .build());
            return CommandResult.success();
        }

        AnimationController.stopAllAnimations();
        src.sendMessage(TextResponses.ALL_ANIMATIONS_STOPPED);
        return CommandResult.success();
    }
}
