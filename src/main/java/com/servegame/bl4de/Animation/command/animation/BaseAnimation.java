package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

/**
 * File: BaseAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class BaseAnimation implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        /*
            ----------------------------------------------------
            Animation
            Author(s): TheCahyag
            Commands: /animate help
            ----------------------------------------------------
        */
        Text message = Text.builder()
                .append(Text.of(Util.SECONDARY_COLOR, "----------------------------------------------------\n",
                        Util.PRIMARY_COLOR, "Animation\n",
                        TextColors.BLUE, "Author(s)",
                        TextColors.WHITE, ": ",
                        Util.PRIMARY_COLOR, "TheCahyag\n",
                        Util.ACTION_COLOR, "Commands",
                        TextColors.WHITE, ": /"))
                .append(Text.builder()
                        .append(Text.of(Util.PRIMARY_COLOR, Util.COMMAND_STYLE, "animate help\n"))
                        .onClick(TextActions.runCommand("/animate help"))
                        .onHover(Util.COMMAND_HOVER)
                        .build())
                .append(Text.of(Util.SECONDARY_COLOR, "----------------------------------------------------"))
                .build();
        src.sendMessage(message);
        return CommandResult.success();
    }
}
