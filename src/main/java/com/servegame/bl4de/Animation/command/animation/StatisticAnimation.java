package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.command.AbstractRunnableCommand;
import com.servegame.bl4de.Animation.model.Animation;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;

/**
 * File: StatisticAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class StatisticAnimation extends AbstractRunnableCommand<CommandSource> {

    private Animation animation;

    public StatisticAnimation(CommandSource src, CommandContext args, Animation animation) {
        super(src, args);
        this.animation = animation;
    }

    @Override
    public boolean checkPermission() {
        return false;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        return null;
    }
}
