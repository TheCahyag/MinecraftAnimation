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

import java.util.Collection;
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
        if (!animationOptional.isPresent()){
            // Animation wasn't found
            player.sendMessage(TextResponses.ANIMATION_NOT_FOUND_ERROR);
            return CommandResult.success();
        }
        Animation animation = animationOptional.get();

        Optional<String> infoOptional = args.getOne("info");
        Optional<String> frameOptional = args.getOne("frame");
        if (infoOptional.isPresent()){
            player.sendMessage(Text.of("we got the info", infoOptional.get()));
        }
        if (frameOptional.isPresent()){
            player.sendMessage(Text.of("we got the frame", frameOptional.get()));
        }
        player.sendMessage(Text.of("Le fin"));
        // Parsing needed
        return CommandResult.success();
    }
}
