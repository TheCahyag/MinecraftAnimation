package com.servegame.bl4de.Animation.command;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

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
            src.sendMessage(Text.of(Util.PRIMARY_COLOR, "Debug has been ", Util.ACTION_COLOR, "set ", Util.SECONDARY_COLOR, "true."));
        } else {
            // Debug was set to false
            src.sendMessage(Text.of(Util.PRIMARY_COLOR, "Debug has been ", Util.ACTION_COLOR, "set ", Util.SECONDARY_COLOR, "false."));
        }
        return CommandResult.success();
    }
}
