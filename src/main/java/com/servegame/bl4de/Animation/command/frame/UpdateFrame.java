package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.model.Animation;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

/**
 * File: UpdateFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class UpdateFrame implements CommandExecutor {

    private Animation animation;

    public UpdateFrame(Animation animation){
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
        src.sendMessage(Text.of("Hello from updateframe"));
        return CommandResult.success();    }
}
