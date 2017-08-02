package com.servegame.bl4de.Animation.commands.animation;

import com.servegame.bl4de.Animation.commands.frame.*;
import com.servegame.bl4de.Animation.models.Animation;
import com.servegame.bl4de.Animation.util.AnimationUtil;
import com.servegame.bl4de.Animation.util.TextResponses;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

/**
 * File: BaseAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class BaseAnimation implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> stringOptional = args.getOne("animation_name");
        if (stringOptional.isPresent()){
            return withName(stringOptional.get(), src, args);
        }
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

    /**
     * When the /animate command was ran, it contained a animation_name instead of the name of a child command
     * @param animationName the given {@link Animation}
     * @param src {@link CommandSource}
     * @param args {@link CommandContext}
     * @return {@link CommandResult}
     */
    private CommandResult withName(String animationName, CommandSource src, CommandContext args){
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = (Player) src;
        Optional<Animation> animationOptional = AnimationUtil.getAnimation(animationName, player.getUniqueId());
        Optional<String> argumentsOptional = args.getOne("remaining_arguments");
        if (!animationOptional.isPresent()){
            // Animation wasn't found
            player.sendMessage(TextResponses.ANIMATION_NOT_FOUND_ERROR);
            return CommandResult.success();
        }
        if (!argumentsOptional.isPresent()){
            // Command doesn't have enough arguments
            player.sendMessage(TextResponses.GENERAL_ARGUMENTS_INCORRECT);
            return CommandResult.empty();
        }
        Animation animation = animationOptional.get();
        String remainingArguments = argumentsOptional.get();

        // Parse remaining command arguments
        String[] tokens = remainingArguments.split(" ");

        if (tokens.length == 0 || "info".equals(tokens[0].toLowerCase())){
            // /animate <name>
            // If the user doesn't specify a command after the name it defaults to info
            CommandResult result = CommandResult.empty();
            try {
                result = new InfoAnimation(animation).execute(src, null);
            } catch (CommandException e){
                // What do I do with this? TODO
                // When does this happen?
                e.printStackTrace();
            }
            return result;
        } else if (!"frame".equals(tokens[0].toLowerCase())){
            // The command is not /animate <name> frame ... and therefore doesn't match any other commands
            player.sendMessage(TextResponses.GENERAL_ARGUMENTS_INCORRECT);
            return CommandResult.empty();
        }

        // Parse arguments after the frame sub-command
        String childCommand = tokens[1];
        String argumentsToTheCommand = "";
        for (int i = 2; i < tokens.length; i++) {
            argumentsToTheCommand += tokens + " ";
        }
        try {
            switch (childCommand.toLowerCase()) {
                case "create":
                    // /animate <name> frame create <name> -h
                    return new CreateFrame(animation).execute(src, CreateFrame.parseArguments(argumentsToTheCommand));
                case "delete":
                    // /animate <name> frame delete <name|num> -f
                    return new DeleteFrame(animation).execute(src, DeleteFrame.parseArguments(argumentsToTheCommand));
                case "display":
                    // /animate <name> frame display <name|num>
                    return new DisplayFrame(animation).execute(src, DisplayFrame.parseArguments(argumentsToTheCommand));
                case "duplicate":
                    // /animate <name> frame duplicate <name|num> [num]
                    return new DuplicateFrame(animation).execute(src, DisplayFrame.parseArguments(argumentsToTheCommand));
                case "list":
                    // /animate <name> frame list
                    return new ListFrames(animation).execute(src, DisplayFrame.parseArguments(argumentsToTheCommand));
                case "update":
                    // /animate <name> frame <name|num> -o
                    return new UpdateFrame(animation).execute(src, DisplayFrame.parseArguments(argumentsToTheCommand));
                default:
                    player.sendMessage(TextResponses.GENERAL_ARGUMENTS_INCORRECT);
                    return CommandResult.empty();
            }
        } catch (CommandException e){
            // Again, what do I do with this? TODO
            e.printStackTrace();
            return CommandResult.empty();
        }
    }
}
