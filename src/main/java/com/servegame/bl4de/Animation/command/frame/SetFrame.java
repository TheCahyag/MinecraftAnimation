package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

/**
 * File: SetFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SetFrame implements CommandExecutor {

    private Animation animation;

    public SetFrame(Animation animation){
        this.animation = animation;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = ((Player) src);
        Optional<String> frameNameOptional = args.getOne("frame_name");
        Frame frame;

        if (this.animation.isRunning()){
            player.sendMessage(TextResponses.ANIMATION_CANT_BE_RUNNING);
            return CommandResult.success();
        }

        player.sendMessage(Text.of("Hello from setframe"));
        return CommandResult.success();
    }
}
