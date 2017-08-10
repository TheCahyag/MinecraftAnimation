package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.models.Animation;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

/**
 * File: DisplayFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class DisplayFrame implements CommandExecutor {

    private Animation animation;

    public DisplayFrame(Animation animation){
        this.animation = animation;
    }

    /**
     * Takes a string and will parse the arguments
     * @param rawString
     * @return
     */
    public static CommandContext parseArguments(String rawString){
        return null;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Text.of("Hello from displayframe"));
        return CommandResult.success();
    }
}