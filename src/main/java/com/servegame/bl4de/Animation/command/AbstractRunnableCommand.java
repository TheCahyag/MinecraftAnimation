package com.servegame.bl4de.Animation.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

/**
 * File: AbstractRunnableCommand.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public abstract class AbstractRunnableCommand<T extends CommandSource> implements CommandExecutor, Runnable {

    protected T src;
    protected CommandContext args;

    public AbstractRunnableCommand(T src, CommandContext args){
        this.src = src;
        this.args = args;
    }

    @Override
    public abstract void run();

    @Override
    public abstract CommandResult execute(CommandSource src, CommandContext args) throws CommandException;

}
