package com.servegame.bl4de.Animation.util;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.commands.animation.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * File: Util.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class Util {

    public static final TextColor PRIMARY_COLOR     = TextColors.GOLD;
    public static final TextColor SECONDARY_COLOR   = TextColors.YELLOW;
    public static final TextColor ACTION_COLOR      = TextColors.AQUA;
    public static final TextColor NAME_COLOR        = TextColors.DARK_AQUA;
    public static final TextColor FLAG_COLOR        = TextColors.RED;
    public static final TextColor ERROR_COLOR       = TextColors.DARK_RED;
    public static final TextColor WARNING_COLOR     = TextColors.YELLOW;
    public static final TextStyle COMMAND_STYLE     = TextStyles.ITALIC;
    public static final HoverAction.ShowText COMMAND_HOVER = TextActions.showText(Text.of("Click here to suggest this command."));

    /**
     * copyWorldToSubSpace will get a {@link BlockSnapshot} of each
     * block in a 3d subspace indicated by the two opposite corners
     * @param corner1 corner one
     * @param corner2 corner two
     * @return 3d BlockSnapshot primitive array
     */
    public static BlockSnapshot[][][] copyWorldToSubSpace(Location<World> corner1, Location<World> corner2){
        // Make sure two corners have the same Extent
//        if (!corner1.getExtent().getDimension().equals(corner2.getExtent().getDimension())){
//            // todo instead of checking here, the location should be checked by one of the command classes
//        }

        // Get absolute length of sub space dimensions
        int xLen = Math.abs(Math.abs(corner1.getBlockX()) - Math.abs(corner2.getBlockX()));
        int yLen = Math.abs(Math.abs(corner1.getBlockY()) - Math.abs(corner2.getBlockY()));
        int zLen = Math.abs(Math.abs(corner1.getBlockZ()) - Math.abs(corner2.getBlockZ()));
        BlockSnapshot[][][] subSpace = new BlockSnapshot[xLen][yLen][zLen];

        /* Get corner to start the copy at by getting the coordinates of the
         numerically lowest coordinates of the two corners
         (may result in a corner different from the parameters)*/
        int xLow = corner1.getBlockX() <= corner2.getBlockX() ? corner1.getBlockX() : corner2.getBlockX();
        int yLow = corner1.getBlockY() <= corner2.getBlockY() ? corner1.getBlockY() : corner2.getBlockY();
        int zLow = corner1.getBlockZ() <= corner2.getBlockZ() ? corner1.getBlockZ() : corner2.getBlockZ();

        // Y
        for (int y = 0; y < yLen; y++) {
            // Z
            for (int z = 0; z < zLen; z++) {
                // X
                for (int x = 0; x < xLen; x++) {
                    subSpace[x + xLow][y + yLow][z + zLow] = new Location<>(
                            corner1.getExtent(), x + xLow, y + yLow, z + zLow
                    ).createSnapshot();
                }
            }
        }
        return subSpace;
    }

    /**
     * Register commands with the Sponge {@link CommandManager}
     * @param plugin {@link AnimationPlugin} plugin instance
     */
    public static void registerCommands(AnimationPlugin plugin){
        CommandManager commandManager = Sponge.getCommandManager();

        // /animate create <name>
        CommandSpec createAnimation = CommandSpec.builder()
                .description(Text.of(Util.PRIMARY_COLOR, "Create a new animation"))
                .permission(Permissions.ANIMATION_CREATE)
                .arguments(GenericArguments.string(Text.of(Util.NAME_COLOR, "name")))
                .executor(new CreateAnimation())
                .build();

        // /animate delete <name>
        CommandSpec deleteAnimation = CommandSpec.builder()
                .description(Text.of(Util.PRIMARY_COLOR, "Delete an animation"))
                .permission(Permissions.ANIMATION_DELETE)
                //.arguments(GenericArguments.string(Text.of(Util.NAME_COLOR, "name")))
                .arguments(GenericArguments.flags().flag("f").buildWith(GenericArguments.string(Text.of(Util.NAME_COLOR, "name"))))
                .executor(new DeleteAnimation())
                .build();

        // /animate help
        CommandSpec helpAnimation = CommandSpec.builder()
                .description(Text.of(Util.PRIMARY_COLOR, "Get help with the animation commands"))
                .permission(Permissions.ANIMATION_HELP)
                .executor(new HelpAnimation())
                .build();

// This will probably be deleted TODO
//        // /animate <name> info
//        CommandSpec infoAnimation = CommandSpec.builder()
//                .description(Text.of(Util.PRIMARY_COLOR, "Get info on a given animation"))
//                .permission(Permissions.ANIMATION_INFO)
//                .executor(new InfoAnimation())
//                .build();

        // /animate list
        CommandSpec listAnimation = CommandSpec.builder()
                .description(Text.of(Util.PRIMARY_COLOR, "Get a list of your animations"))
                .permission(Permissions.ANIMATION_LIST)
                .executor(new ListAnimation())
                .build();

        // /animate start <name> -f<num> -d<num> -c<num>
        CommandSpec startAnimation = CommandSpec.builder()
                .description(Text.of(Util.PRIMARY_COLOR, "Start a given animation"))
                .permission(Permissions.ANIMATION_START)
                .arguments(GenericArguments.string(Text.of(Util.NAME_COLOR, "name"))) // <name>
                .arguments(GenericArguments.flags()
                        .valueFlag(GenericArguments.integer(Text.of(Util.FLAG_COLOR, "frame")), "f") // -f<num>
                        .buildWith(GenericArguments.none()))
                .arguments(GenericArguments.flags()
                        .valueFlag(GenericArguments.integer(Text.of(Util.FLAG_COLOR, "delay")), "d") // -d<num>
                        .buildWith(GenericArguments.none()))
                .arguments(GenericArguments.flags()
                        .valueFlag(GenericArguments.integer(Text.of(Util.FLAG_COLOR, "cycles")), "c") // -c<num>
                        .buildWith(GenericArguments.none()))
                .executor(new BaseAnimation())
                .build();

        // /animate stop <name>
        CommandSpec stopAnimation = CommandSpec.builder()
                .description(Text.of(Util.PRIMARY_COLOR, "Stop a given animation"))
                .permission(Permissions.ANIMATION_STOP)
                .arguments(GenericArguments.string(Text.of(Util.NAME_COLOR, "name")))
                .executor(new StopAnimation())
                .build();

        // /animate


        Map<String, CommandElement> commandMapping = new HashMap<>();
        commandMapping.put("sub_command", GenericArguments.seq(
                GenericArguments.string(Text.of("animation_name")),
                GenericArguments.string(Text.of("sub_command"))
        ));


        // /animate
        CommandSpec animate = CommandSpec.builder()
                .description(Text.of(Util.PRIMARY_COLOR, "Base animation command"))
                .child(createAnimation, "create")
                .child(deleteAnimation, "delete")
                .child(helpAnimation, "help", "?")
                .child(listAnimation, "list")
                .child(startAnimation, "start")
                .child(stopAnimation, "stop")
                .arguments(
                        GenericArguments.optional(
                                GenericArguments.string(Text.of("animation_name"))
                        ),
                        GenericArguments.remainingRawJoinedStrings(Text.of("remaining_arguments"))
                )
                .permission(Permissions.ANIMATION_BASE)
                .executor(new BaseAnimation())
                .build();

        commandManager.register(plugin, animate, "animate");
    }
}
