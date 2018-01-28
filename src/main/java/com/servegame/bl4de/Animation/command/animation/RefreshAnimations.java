package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.data.PreparedStatements;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: RefreshAnimations.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class RefreshAnimations implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Map<UUID, ArrayList<String>> animationsMap = PreparedStatements.getAnimations();
        for (Map.Entry<UUID, ArrayList<String>> entry :
                animationsMap.entrySet()) {
                UUID owner = entry.getKey();
            for (String animationName :
                    entry.getValue()) {
                if (AnimationController.refreshAnimation(animationName, owner)) {
                    Text message = Text.of(PRIMARY_COLOR, "'",
                            NAME_COLOR, animationName,
                            PRIMARY_COLOR, "' was ",
                            ACTION_COLOR, "refreshed ",
                            PRIMARY_COLOR, "successfully.");
                    src.sendMessage(message);
                } else {
                    Text message = Text.of(PRIMARY_COLOR, "'",
                            NAME_COLOR, animationName,
                            PRIMARY_COLOR, "' ",
                            ERROR_COLOR, "failed ",
                            PRIMARY_COLOR, "to refresh.");
                    src.sendMessage(message);
                }
            }
        }
        return CommandResult.success();
    }
}
