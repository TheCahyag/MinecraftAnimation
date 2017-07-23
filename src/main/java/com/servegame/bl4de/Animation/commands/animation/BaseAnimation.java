package com.servegame.bl4de.Animation.commands.animation;

import com.servegame.bl4de.Animation.util.UTIL;
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
        Optional<String> stringOptional = args.getOne("name");
        if (stringOptional.isPresent()){
            return withName(stringOptional.get(), src);
        }
        Text message = Text.builder()
                .append(Text.of(UTIL.SECONDARY_COLOR, "----------------------------------------------------\n",
                        UTIL.PRIMARY_COLOR, "Animation\n",
                        TextColors.BLUE, "Author(s)",
                        TextColors.WHITE, ": ",
                        UTIL.PRIMARY_COLOR, "TheCahyag\n",
                        UTIL.ACTION_COLOR, "Commands",
                        TextColors.WHITE, ": /"))
                .append(Text.builder()
                        .append(Text.of(UTIL.PRIMARY_COLOR, UTIL.COMMAND_STYLE, "animate help"))
                        .onClick(TextActions.runCommand("/animate help"))
                        .onHover(UTIL.COMMAND_HOVER)
                        .build())
                .append(Text.of(UTIL.SECONDARY_COLOR, "----------------------------------------------------"))
                .build();
        src.sendMessage(message);
        return CommandResult.success();
    }

    private CommandResult withName(String animationName, CommandSource src){
        if (!(src instanceof Player)){
            src.sendMessage(Text.of(UTIL.PRIMARY_COLOR, "This command is only meant for in-game players!"));
            return CommandResult.success();
        }
        // TODO
        Player player = (Player) src;
        player.sendMessage(Text.of(UTIL.PRIMARY_COLOR, "Name: ", UTIL.NAME_COLOR, animationName));
        return CommandResult.success();
    }
}
