package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.models.Animation;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

/**
 * File: InfoFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class InfoFrame implements CommandExecutor {

    private Animation animation;

    public InfoFrame(Animation animation){
        this.animation = animation;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        src.sendMessage(Text.of("hello from info frame"));
        return CommandResult.success();
    }
}
