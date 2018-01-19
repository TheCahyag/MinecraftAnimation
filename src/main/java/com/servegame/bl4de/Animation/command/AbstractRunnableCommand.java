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
public abstract class AbstractRunnableCommand<T extends CommandSource> extends AbstractCommand<T> implements Runnable {

    public AbstractRunnableCommand(T src, CommandContext args){
        super(src, args);
    }

    @Override
    public abstract void run();

}
