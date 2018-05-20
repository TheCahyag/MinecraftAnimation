package com.servegame.bl4de.Animation.command.animation.admin;

import com.servegame.bl4de.Animation.command.AbstractRunnableCommand;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

/**
 * File: StatisticAnimations.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class StatisticAnimations extends AbstractRunnableCommand<CommandSource> {

    public StatisticAnimations(CommandSource src, CommandContext args) {
        super(src, args);
    }

    @Override
    public boolean checkPermission() {
        return false;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        return null;
    }
}
