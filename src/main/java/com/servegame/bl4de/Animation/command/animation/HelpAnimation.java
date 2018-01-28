package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.util.Resource;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.net.MalformedURLException;
import java.net.URL;

import static com.servegame.bl4de.Animation.util.Util.*;

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
            /animate delete <name> -f
            /animate start <name> -f <num> -d <num> -c <num>
            /animate stop <name>
            /animate list
            /animate <name> info
            /animate <name> set <pos1|pos2> -f
            /animate <name> frame create <name>
            /animate <name> frame delete <name|num> -f
            /animate <name> frame display <name|num>
            /animate <name> frame duplicate <name|num> [num]
            /animate <name> frame <name|num> info
            /animate <name> frame list
            /animate <name> frame update <name|num> -o
            ----------------------------------------------------
        */
        URL url = null;
        try {
            url = new URL(Resource.COMMAND_URL);
        } catch (MalformedURLException e){}

        Text message = Text.builder()
//                .append(Text.of(TextColors.WHITE, "/"))
//                .append(Text.builder()
//                        .append(Text.of(Util.PRIMARY_COLOR, Util.COMMAND_STYLE, "animate\n"))
//                        .onClick(TextActions.runCommand("/animate"))
//                        .onHover(Util.COMMAND_HOVER)
//                        .build()) // Can't actually call this TODO
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, COMMAND_STYLE, "For a full list of commands visit our wiki!\n"))
                        .onClick(TextActions.openUrl(url))
                        .onHover(TextActions.showText(Text.of("Click here to visit the wiki")))
                        .build())
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        ACTION_COLOR, " create",
                        NAME_COLOR, " <name>\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        ACTION_COLOR, " delete",
                        NAME_COLOR, " <name>",
                        FLAG_COLOR, " -f\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        ACTION_COLOR, " start",
                        NAME_COLOR, " <name>",
                        FLAG_COLOR, " -f <num> -d <num> -c <num>\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        ACTION_COLOR, " stop",
                        NAME_COLOR, " <name>\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        ACTION_COLOR, " pause",
                        NAME_COLOR, " <name>\n"))
                .append(Text.of(TextColors.WHITE, "/"))
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, COMMAND_STYLE, "animate", ACTION_COLOR, " list\n"))
                        .onClick(TextActions.runCommand("/animate list"))
                        .onHover(COMMAND_HOVER)
                        .build())
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        NAME_COLOR, " <name>",
                        ACTION_COLOR, " info\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        NAME_COLOR, " <name>",
                        ACTION_COLOR, " set",
                        PRIMARY_COLOR, " <pos1|pos2> ",
                        FLAG_COLOR, "-f\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        NAME_COLOR, " <name>",
                        ACTION_COLOR, " set name",
                        NAME_COLOR, " <new_name> (Not impl.)\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        NAME_COLOR, " <name>",
                        PRIMARY_COLOR, " frame",
                        ACTION_COLOR, " create",
                        NAME_COLOR, " <name>",
                        FLAG_COLOR, " -h\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        NAME_COLOR, " <name>",
                        PRIMARY_COLOR, " frame",
                        ACTION_COLOR, " delete",
                        NAME_COLOR, " <name|num>",
                        FLAG_COLOR, " -f\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        NAME_COLOR, " <name>",
                        PRIMARY_COLOR, " frame",
                        ACTION_COLOR, " display",
                        NAME_COLOR, " <name|num>\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        NAME_COLOR, " <name>",
                        PRIMARY_COLOR, " frame",
                        ACTION_COLOR, " duplicate",
                        NAME_COLOR, " <name|num>",
                        PRIMARY_COLOR, " [num]\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        NAME_COLOR, " <name>",
                        PRIMARY_COLOR, " frame",
                        ACTION_COLOR, " list\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        NAME_COLOR, " <name>",
                        PRIMARY_COLOR, " frame",
                        ACTION_COLOR, " update",
                        NAME_COLOR, " <name|num>",
                        FLAG_COLOR, " -o\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        NAME_COLOR, " <name>",
                        PRIMARY_COLOR, " frame",
                        NAME_COLOR, " <name|num>",
                        ACTION_COLOR, " info\n"))
                .append(Text.of(TextColors.WHITE, "/",
                        PRIMARY_COLOR, "animate",
                        NAME_COLOR, " <name>",
                        PRIMARY_COLOR, " frame",
                        NAME_COLOR, " <name|num>",
                        ACTION_COLOR, " set name",
                        NAME_COLOR, " <new_name> (not impl)\n"))
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------"))
                .build();
        src.sendMessage(message);
        return CommandResult.success();
    }
}
