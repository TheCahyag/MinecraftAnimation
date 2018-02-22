package com.servegame.bl4de.Animation.command.animation.admin;

import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.command.AbstractCommand;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

/**
 * File: ListAllAnimations.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class ListAllAnimations extends AbstractCommand<CommandSource> {

    public ListAllAnimations(CommandSource src, CommandContext args){
        super(src, args);
    }

    @Override
    public boolean checkPermission() {
        return this.src.hasPermission(Permissions.LIST_ALL_ANIMAITONS);
    }

    @Override
    public CommandResult runCommand() {
        if (!checkPermission()){
            this.src.sendMessage(TextResponses.USER_DOESNT_HAVE_PERMISSION);
            return CommandResult.empty();
        }
        return super.runCommand();
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        return null;
    }
}
