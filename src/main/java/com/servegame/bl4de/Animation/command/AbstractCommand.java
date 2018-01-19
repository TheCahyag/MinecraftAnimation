package com.servegame.bl4de.Animation.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * File: AbstractCommand.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public abstract class AbstractCommand<T extends CommandSource> implements CommandExecutor {

    protected T src;
    protected CommandContext args;

    public AbstractCommand(T src, CommandContext args) {
        this.src = src;
        this.args = args;
    }

    /**
     * Checks to see if the {@link CommandSource} has permission to run the current command
     * @return true if the src has permission false otherwise
     */
    public abstract boolean checkPermission();

    @Override
    public abstract CommandResult execute(CommandSource src, CommandContext args) throws CommandException;
}
