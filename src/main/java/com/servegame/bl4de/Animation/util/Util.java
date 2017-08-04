package com.servegame.bl4de.Animation.util;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.commands.CommandGateKeeper;
import com.servegame.bl4de.Animation.commands.animation.*;
import com.servegame.bl4de.Animation.models.Animation;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.command.CommandManager;
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

import static org.spongepowered.api.command.args.GenericArguments.*;

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

    public static Text getAnimationButtons(Animation animation){
        Text message = Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "FRAMES",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame list"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "PLAY",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " start"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "PAUSE",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " pause"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "STOP",
                                PRIMARY_COLOR, "]"))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " stop"))
                        .build())
                .build();
        return message;
    }

    public static Text getFrameButtons(){
        Text message = Text.builder()
                .append(Text.of())
                .build();
        return message;
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
                .arguments(string(Text.of(Util.NAME_COLOR, "name")))
                .executor(new CreateAnimation())
                .build();

        // /animate delete <name>
        CommandSpec deleteAnimation = CommandSpec.builder()
                .description(Text.of(Util.PRIMARY_COLOR, "Delete an animation"))
                .permission(Permissions.ANIMATION_DELETE)
                //.arguments(string(Text.of(Util.NAME_COLOR, "name")))
                .arguments(flags().flag("f").buildWith(string(Text.of(Util.NAME_COLOR, "name"))))
                .executor(new DeleteAnimation())
                .build();

        // /animate help
        CommandSpec helpAnimation = CommandSpec.builder()
                .description(Text.of(Util.PRIMARY_COLOR, "Get help with the animation commands"))
                .permission(Permissions.ANIMATION_HELP)
                .executor(new HelpAnimation())
                .build();

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
                .arguments(string(Text.of(Util.NAME_COLOR, "name"))) // <name>
                .arguments(flags()
                        .valueFlag(integer(Text.of(Util.FLAG_COLOR, "frame")), "f") // -f<num>
                        .buildWith(none()))
                .arguments(flags()
                        .valueFlag(integer(Text.of(Util.FLAG_COLOR, "delay")), "d") // -d<num>
                        .buildWith(none()))
                .arguments(flags()
                        .valueFlag(integer(Text.of(Util.FLAG_COLOR, "cycles")), "c") // -c<num>
                        .buildWith(none()))
                .executor(new BaseAnimation())
                .build();

        // /animate stop <name>
        CommandSpec stopAnimation = CommandSpec.builder()
                .description(Text.of(Util.PRIMARY_COLOR, "Stop a given animation"))
                .permission(Permissions.ANIMATION_STOP)
                .arguments(string(Text.of(Util.NAME_COLOR, "name")))
                .executor(new StopAnimation())
                .build();

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
                        string(Text.of("animation_name")),
                        firstParsing(
                                // /animate <name> info
                                literal(Text.of("animation_info"), "info"),
                                // /animate <name> frame...
                                literal(Text.of("frame"), "frame")
                        ),
                        optional(
                                firstParsing(
                                        // /animate <name> frame create <name>
                                        seq(
                                                literal(Text.of("create"), "create"),
                                                optional(string(Text.of("frame_name")))
                                        ),
                                        // /animate <name> frame delete <name|num> -f
                                        seq(
                                                literal(Text.of("delete"), "delete"),
                                                string(Text.of("frame_name_num")),
                                                optional(flags().flag("f").buildWith(none()))
                                        ),
                                        // /animate <name> frame display <name|num>
                                        seq(
                                                literal(Text.of("display"), "display"),
                                                string(Text.of("frame_name_num")),
                                                optional(flags().flag("f").buildWith(none()))
                                        ),
                                        // /animate <name> frame duplicate <name|num> [num]
                                        seq(
                                                literal(Text.of("duplicate"), "duplicate"),
                                                string(Text.of("frame_name_num")),
                                                optional(integer(Text.of("num")))
                                        ),
                                        // /animate <name> frame update <name|num> -o
                                        seq(
                                                literal(Text.of("update"), "update"),
                                                string(Text.of("frame_name_num")),
                                                optional(flags().flag("o").buildWith(none()))
                                        ),
                                        // /animate <name> frame list
                                        seq(
                                                literal(Text.of("list"), "list")
                                        ),
                                        // /animate <name> frame <name|num> info, this NEEDS to be last
                                        seq(
                                                string(Text.of("frame_name_num")),
                                                literal(Text.of("frame_info"), "info")
                                        )
                                )
                        )
                )
                .permission(Permissions.ANIMATION_BASE)
                .executor(new CommandGateKeeper())
                .build();

        commandManager.register(plugin, animate, "animate", "animation");
    }
}
