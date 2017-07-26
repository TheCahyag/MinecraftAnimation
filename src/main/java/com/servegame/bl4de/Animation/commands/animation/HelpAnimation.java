package com.servegame.bl4de.Animation.commands.animation;

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
 * File: HelpAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class HelpAnimation implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        /*
            ----------------------------------------------------
            /animate
            /animate create <name>
            /animate delete <name>
            /animate start <name> -f<num> -d<num> -c<num>
            /animate stop <name>
            /animate list
            /animate <name> frame create <name> -h
            /animate <name> frame duplicate <name|num> [num]
            /animate <name> frame delete <name|num> -f
            /animate <name> frame display <name|num>
            /animate <name> frame update <name|num> -o
            /animate <name> frame list
            ----------------------------------------------------
        */
        Text message = Text.builder()
                .append(Text.of(Util.SECONDARY_COLOR, "----------------------------------------------------\n"))
                .append(Text.of(TextColors.WHITE, "/"))
                .append(Text.builder()
                        .append(Text.of(Util.PRIMARY_COLOR, Util.COMMAND_STYLE, "animate\n"))
                        .onClick(TextActions.runCommand("/animate"))
                        .onHover(Util.COMMAND_HOVER)
                        .build())
                .append(Text.of(TextColors.WHITE, "/",
                        Util.PRIMARY_COLOR, "animate",
                        Util.ACTION_COLOR, " create",
                        Util.NAME_COLOR, " <name>\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        Util.PRIMARY_COLOR, "animate",
                        Util.ACTION_COLOR, " delete",
                        Util.NAME_COLOR, " <name>\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        Util.PRIMARY_COLOR, "animate",
                        Util.ACTION_COLOR, " start",
                        Util.NAME_COLOR, " <name>",
                        Util.PRIMARY_COLOR, " -f<num> -d<num> -c<num>\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        Util.PRIMARY_COLOR, "animate",
                        Util.ACTION_COLOR, " stop",
                        Util.NAME_COLOR, " <name>\n"))
                .append(Text.of(TextColors.WHITE, "/"))
                .append(Text.builder()
                        .append(Text.of(Util.PRIMARY_COLOR, Util.COMMAND_STYLE, "animate", Util.ACTION_COLOR, " list\n"))
                        .onClick(TextActions.runCommand("/animate list"))
                        .onHover(Util.COMMAND_HOVER)
                        .build())
                .append(Text.of(TextColors.WHITE, "/",
                        Util.PRIMARY_COLOR, "animate",
                        Util.NAME_COLOR, " <name>",
                        Util.PRIMARY_COLOR, " frame",
                        Util.ACTION_COLOR, " create",
                        Util.NAME_COLOR, " <name>",
                        Util.PRIMARY_COLOR, " -h\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        Util.PRIMARY_COLOR, "animate",
                        Util.NAME_COLOR, " <name>",
                        Util.PRIMARY_COLOR, " frame",
                        Util.ACTION_COLOR, " delete",
                        Util.NAME_COLOR, " <name|num>",
                        Util.PRIMARY_COLOR, " -f\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        Util.PRIMARY_COLOR, "animate",
                        Util.NAME_COLOR, " <name>",
                        Util.PRIMARY_COLOR, " frame",
                        Util.ACTION_COLOR, " display",
                        Util.NAME_COLOR, " <name|num>\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        Util.PRIMARY_COLOR, "animate",
                        Util.NAME_COLOR, " <name>",
                        Util.PRIMARY_COLOR, " frame",
                        Util.ACTION_COLOR, " duplicate",
                        Util.NAME_COLOR, " <name|num>",
                        Util.PRIMARY_COLOR, " [num]\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        Util.PRIMARY_COLOR, "animate",
                        Util.NAME_COLOR, " <name>",
                        Util.PRIMARY_COLOR, " frame",
                        Util.ACTION_COLOR, " update",
                        Util.NAME_COLOR, " <name|num>",
                        Util.PRIMARY_COLOR, " -o\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        Util.PRIMARY_COLOR, "animate",
                        Util.NAME_COLOR, " <name>",
                        Util.PRIMARY_COLOR, " frame",
                        Util.ACTION_COLOR, " list\n"))
                .append(Text.of(Util.SECONDARY_COLOR, "----------------------------------------------------"))
                .build();
        src.sendMessage(message);
        return CommandResult.success();
    }
}
