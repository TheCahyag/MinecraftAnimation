package com.servegame.bl4de.Animation.commands.animation;

import com.servegame.bl4de.Animation.models.Animation;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;

/**
 * File: InfoAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class InfoAnimation implements CommandExecutor {

    private Animation animation;

    public InfoAnimation(Animation animation){
        this.animation = animation;
    }

    @Override
    public CommandResult execute(CommandSource src, @Nullable CommandContext args) throws CommandException {
        Text message = Text.builder().build();
        src.sendMessage(Text.of("This animation name is: ", this.animation.getAnimationName()));
        return CommandResult.success();
    }
}
