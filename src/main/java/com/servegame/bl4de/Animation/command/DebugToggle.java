package com.servegame.bl4de.Animation.command;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * File: DebugToggle.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class DebugToggle implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        boolean oldDebug = AnimationPlugin.instance.isDebug();
        AnimationPlugin.instance.setDebug(!oldDebug);
        boolean currentDebug = !oldDebug;
        if (currentDebug){
            // Debug was set to true
            src.sendMessage(TextResponses.DEBUG_SET_TRUE);
        } else {
            // Debug was set to false
            src.sendMessage(TextResponses.DEBUG_SET_FALSE);
        }
        return CommandResult.success();
    }
}
