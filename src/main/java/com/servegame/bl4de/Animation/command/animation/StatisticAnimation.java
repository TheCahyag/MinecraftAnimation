package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

/**
 * File: StatisticAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class StatisticAnimation implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        int numOfAnimation, animationsPlaying;


        src.sendMessage(Text.of(Util.ERROR_COLOR, "This feature is currently not implemented."));
        Text message = Text.builder()
                .build();

        return CommandResult.success();
    }
}
