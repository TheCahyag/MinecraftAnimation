package com.servegame.bl4de.Animation.commands.animation;

import com.servegame.bl4de.Animation.models.Animation;
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
        Optional<String> stringOptional = args.getOne("name");
        if (stringOptional.isPresent()){
            return withName(stringOptional.get(), src);
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

    private CommandResult withName(String animationName, CommandSource src){
        if (!(src instanceof Player)){
            src.sendMessage(Text.of(Util.PRIMARY_COLOR, "This command is only meant for in-game players!"));
            return CommandResult.success();
        }
        // TODO
        Player player = (Player) src;
        //Optional<Animation> ;
        return CommandResult.success();
    }
}
