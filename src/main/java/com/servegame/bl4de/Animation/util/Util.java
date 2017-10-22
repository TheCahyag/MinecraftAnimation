package com.servegame.bl4de.Animation.util;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.command.CommandGateKeeper;
import com.servegame.bl4de.Animation.command.DebugToggle;
import com.servegame.bl4de.Animation.command.animation.*;
import com.servegame.bl4de.Animation.command.animation.action.PauseAnimation;
import com.servegame.bl4de.Animation.command.animation.action.StartAnimation;
import com.servegame.bl4de.Animation.command.animation.action.StopAnimation;
import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.SubSpace3D;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;
import java.util.UUID;

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
    public static final long MAX_VOLUME             = 30000L;
    public static final long WARNING_VOLUME         = 2500L;

    /**
     * copyWorldToSubSpace will get a {@link BlockSnapshot} of each
     * block in a 3d subspace indicated by the two opposite corners
     * @param corner1 corner one
     * @param corner2 corner two
     * @return 3d BlockSnapshot primitive array
     */
    public static BlockSnapshot[][][] copyWorldToSubSpace(Location<World> corner1, Location<World> corner2){
        // Get absolute length of sub space dimensions
        int xLen = Math.abs(Math.abs(corner1.getBlockX()) - Math.abs(corner2.getBlockX()));
        int yLen = Math.abs(Math.abs(corner1.getBlockY()) - Math.abs(corner2.getBlockY()));
        int zLen = Math.abs(Math.abs(corner1.getBlockZ()) - Math.abs(corner2.getBlockZ()));
        BlockSnapshot[][][] subSpace = new BlockSnapshot[xLen + 1][yLen + 1][zLen + 1];

        /* Get corner to start the copy at by getting the coordinates of the
         numerically lowest coordinates of the two corners
         (may result in a corner different from the parameters)*/
        int xLow = corner1.getBlockX() <= corner2.getBlockX() ? corner1.getBlockX() : corner2.getBlockX();
        int yLow = corner1.getBlockY() <= corner2.getBlockY() ? corner1.getBlockY() : corner2.getBlockY();
        int zLow = corner1.getBlockZ() <= corner2.getBlockZ() ? corner1.getBlockZ() : corner2.getBlockZ();

        // Y
        for (int y = 0; y <= yLen; y++) {
            // Z
            for (int z = 0; z <= zLen; z++) {
                // X
                for (int x = 0; x <= xLen; x++) {
                    subSpace[x][y][z] = new Location<>(
                            corner1.getExtent(), x + xLow, y + yLow, z + zLow
                    ).createSnapshot();
                }
            }
        }
        return subSpace;
    }

    /**
     * TODO
     * @param subSpace
     * @throws UninitializedException
     */
    public static void copySubSpaceToWorld(SubSpace3D subSpace) throws UninitializedException {
        if (!subSpace.isInitialized()){
            throw new UninitializedException("Subspace is not fully initialized.");
        }
        Location<World> corner1 = subSpace.getCornerOne().get();
        Location<World> corner2 = subSpace.getCornerTwo().get();
        BlockSnapshot[][][] subSpaceSnapShot = subSpace.getContents().get();

        // Get absolute length of sub space dimensions
        int xLen = Math.abs(Math.abs(corner1.getBlockX()) - Math.abs(corner2.getBlockX()));
        int yLen = Math.abs(Math.abs(corner1.getBlockY()) - Math.abs(corner2.getBlockY()));
        int zLen = Math.abs(Math.abs(corner1.getBlockZ()) - Math.abs(corner2.getBlockZ()));

        /* Get corner to start the copy at by getting the coordinates of the
         numerically lowest coordinates of the two corners
         (may result in a corner different from the parameters)*/
        int xLow = corner1.getBlockX() <= corner2.getBlockX() ? corner1.getBlockX() : corner2.getBlockX();
        int yLow = corner1.getBlockY() <= corner2.getBlockY() ? corner1.getBlockY() : corner2.getBlockY();
        int zLow = corner1.getBlockZ() <= corner2.getBlockZ() ? corner1.getBlockZ() : corner2.getBlockZ();

        // Y
        for (int y = 0; y <= yLen; y++) {
            // Z
            for (int z = 0; z <= zLen; z++) {
                // X
                for (int x = 0; x <= xLen; x++) {
                    BlockSnapshot snapshot = subSpaceSnapShot[x][y][z];
                    new Location<>(corner1.getExtent(), x + xLow, y + yLow, z + zLow)
                            .setBlock(snapshot.getState(), Cause.source(AnimationPlugin.plugin).build());
                }
            }
        }
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
                        .onClick(TextActions.runCommand("/animate start " + animation.getAnimationName()))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "PAUSE",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate pause " + animation.getAnimationName()))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "STOP",
                                PRIMARY_COLOR, "]"))
                        .onClick(TextActions.runCommand("/animate stop " + animation.getAnimationName()))
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
     * TODO should this instead get the user?
     * @param uuid
     * @param src
     * @return
     */
    public static Optional<User> getOfflinePlayer(UUID uuid, CommandSource src){
        // Get offline user
        Optional<UserStorageService> userStorageServiceOptional = Sponge.getServiceManager().provide(UserStorageService.class);
        if (!userStorageServiceOptional.isPresent()){
            if (src == null){
                // Send a message to console
                Sponge.getServer().getConsole().sendMessage(Text.of(ERROR_COLOR, "Uh oh, the UserStorageService.class couldn't be obtained..."));
            } else {
                // Send a message to the player
                src.sendMessage(Text.of(ERROR_COLOR, "Uh oh, the UserStorageService.class couldn't be obtained..."));
            }
            return Optional.empty();
        }
        UserStorageService userStorageService = userStorageServiceOptional.get();
        return userStorageService.get(uuid);
    }

    /**
     * Calculate the volume of a 3D subspace represented by two {@link Location}s
     * @param corner1 First {@link Location}
     * @param corner2 Second {@link Location}
     * @return long - the number of blocks that are within the two locations
     */
    public static long calculateVolume(Location corner1, Location corner2){
        // Get absolute length of sub space dimensions
        long xLen = Math.abs(Math.abs(corner1.getBlockX()) - Math.abs(corner2.getBlockX()));
        long yLen = Math.abs(Math.abs(corner1.getBlockY()) - Math.abs(corner2.getBlockY()));
        long zLen = Math.abs(Math.abs(corner1.getBlockZ()) - Math.abs(corner2.getBlockZ()));
        return xLen * yLen * zLen;
    }

    /**
     * Overloaded method for {@link #calculateVolume(Location, Location)}
     * Get's the locations from the given {@link SubSpace3D}. If the {@link SubSpace3D}
     * is not initialized then 0 is returned
     *
     * @param subSpace3D given {@link SubSpace3D}
     * @return 0 if the {@link SubSpace3D} is not initialized, else will return the volume
     */
    public static long calculateVolume(SubSpace3D subSpace3D){
        if (subSpace3D.isInitialized()){
            return calculateVolume(subSpace3D.getCornerOne().get(), subSpace3D.getCornerTwo().get());
        }
        return 0;
    }

    /**
     * Converts a {@link Location} into a clean string.
     * @param location the {@link Location}
     * @return a String representation of the Location following this format:
     *         (X, Y, Z) in worldName[dimensionNumber]
     */
    public static String locationToString(Location location){
        UUID world = location.getExtent().getUniqueId();
        World theWorld = Sponge.getServer().getWorld(world).get();
        return "(" + location.getBlockX() + ", " +
                location.getBlockY() + ", " +
                location.getBlockZ() + ") in " +
                theWorld.getName() + " (" +
                theWorld.getDimension().getType().getId() + ")";
    }

    /**
     * TODO
     * @param string
     * @return
     */
    public static String encapColons(String string){
        String data = string;
        if (data.contains(":")) {
            int colonIndex = data.indexOf(":"), equalIndex = 0, endIndex = 0;

            // Find equal sign
            for (int i = colonIndex; i > 0; i--) {
                if (data.charAt(i) == '='){
                    equalIndex = i;
                    break;
                }
            }

            // Find end symbol (either ',' or '}'
            for (int i = colonIndex; i < data.length(); i++) {
                if (data.charAt(i) == ',' || data.charAt(i) == '}'){
                    endIndex = i;
                    break;
                }
            }
            assert equalIndex != 0 && endIndex != 0 && equalIndex < endIndex;
            // Insert '"' marks at the beginning and end
            String before = data.substring(0, equalIndex + 1),
                    colonContaining = "\"" + data.substring(equalIndex + 1, endIndex) + "\"",
                    after = data.substring(endIndex, data.length());
            if (after.contains(":")){
                after = encapColons(after);
            }
            data = before + colonContaining + after;
        }
        return data;
    }

    /**
     * TODO no longer used
     * @param string
     * @return
     */
    public static String replaceNewLineWithCommaSometimes(String string){
        char[] array = string.toCharArray();
        int len = array.length;
        for (int i = 0; i < len; i++) {
            // For each char...
            if (array[i] == '\n'){
                // ..find new lines

                // The default is to replace \n with ',' but in the following cases
                // the replace should be nothing (which is represented as a space)
                char replace = ',';
                if (array[i - 1] == '{'){
                    replace = ' ';
                } else if ((i + 1 < len) && array[i + 1] == '}'){
                    replace = ' ';
                } else if (array[i - 1] == '['){
                    replace = ' ';
                } else if ((i + 1 < len) && array[i + 1] == ']'){
                    replace = ' ';
                } else if (array[i - 1] == ','){
                    replace = ' ';
                } else if (i == len - 1){
                    replace = ' ';
                }
                array[i] = replace;
            }
        }
        return String.valueOf(array);
    }

    /**
     * TODO
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Register command with the Sponge {@link CommandManager}
     * @param plugin {@link AnimationPlugin} plugin instance
     */
    public static void registerCommands(AnimationPlugin plugin){
        CommandManager commandManager = Sponge.getCommandManager();

        // /animate create <name>
        CommandSpec createAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Create a new animation"))
                .permission(Permissions.ANIMATION_CREATE)
                .arguments(string(Text.of(NAME_COLOR, "animation_name")))
                .executor(new CreateAnimation())
                .build();

        // /animate delete <name>
        CommandSpec deleteAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Delete an animation"))
                .permission(Permissions.ANIMATION_DELETE)
                .arguments(string(Text.of(NAME_COLOR, "animation_name")),
                        optional(flags().flag("f").buildWith(none())))
                .executor(new DeleteAnimation())
                .build();

        // /animate help
        CommandSpec helpAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Get help with the animation command"))
                .permission(Permissions.ANIMATION_HELP)
                .executor(new HelpAnimation())
                .build();

        // /animate list
        CommandSpec listAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Get a list of your animations"))
                .permission(Permissions.ANIMATION_LIST)
                .executor(new ListAnimation())
                .build();

        // /animate start <name> [-f<num>] [-d<num>] [-c<num>]
        CommandSpec startAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Start a given animation"))
                .permission(Permissions.ANIMATION_START)
                .arguments(string(Text.of(NAME_COLOR, "animation_name")),
                        optional(flags()
                                .valueFlag(integer(Text.of(FLAG_COLOR, "frame")), "f") // -f<num>
                                .buildWith(none())),
                        optional(flags()
                                .valueFlag(integer(Text.of(FLAG_COLOR, "delay")), "d") // -d<num>
                                .buildWith(none())),
                        optional(flags()
                                .valueFlag(integer(Text.of(FLAG_COLOR, "cycles")), "c") // -c<num>
                                .buildWith(none()))
                        )
                .executor(new StartAnimation())
                .build();

        // /animate stop <name>
        CommandSpec stopAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Stop a given animation"))
                .permission(Permissions.ANIMATION_STOP)
                .arguments(string(Text.of(NAME_COLOR, "animation_name")))
                .executor(new StopAnimation())
                .build();

        // /animate pause <name>
        CommandSpec pauseAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Pause a given animation"))
                .permission(Permissions.ANIMATION_PAUSE)
                .arguments(string(Text.of("animation_name")))
                .executor(new PauseAnimation())
                .build();

        // /animate stats
        CommandSpec statsAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Show stats about the current animations"))
                .permission(Permissions.ANIMATION_STATS)
                .executor(new StatisticAnimation())
                .build();

        // /animate debug
        CommandSpec debugAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Toggle the debug flag"))
                .permission(Permissions.TOGGLE_DEBUG)
                .executor(new DebugToggle())
                .build();

        // /animate
        CommandSpec animate = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Base animation command"))
                .child(createAnimation, "create")
                .child(deleteAnimation, "delete")
                .child(helpAnimation, "help", "?")
                .child(listAnimation, "list")
                .child(startAnimation, "start")
                .child(stopAnimation, "stop")
                .child(pauseAnimation, "pause")
                .child(statsAnimation, "stats", "statistics")
                .child(debugAnimation, "debug")
                .arguments(
                        string(Text.of("animation_name")),
                        firstParsing(
                                // /animate <name> info
                                literal(Text.of("animation_info"), "info"),
                                // /animate <name> set...
                                literal(Text.of("animation_set"), "set"),
                                // /animate <name> frame...
                                literal(Text.of("frame"), "frame")
                        ),
                        optional(
                                firstParsing(
                                        // /animate <name> set pos1
                                        seq(
                                                literal(Text.of("pos1"), "pos1"),
                                                optional(flags().flag("f").buildWith(none()))
                                        ),
                                        // /animate <name> set pos2
                                        seq(
                                                literal(Text.of("pos2"), "pos2"),
                                                optional(flags().flag("f").buildWith(none()))
                                        ),
                                        // /animate <name> set name <new_name>
                                        seq(
                                                literal(Text.of("set_name"), "name"),
                                                string(Text.of("new_name"))
                                        )
                                )
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
                                                literal(Text.of("delete_frame"), "delete_frame"),
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
// Having this command breaks /animate ... frame <name> info, not sure why yet
//                                        // /animate <name> frame <name|num> set...
//                                        seq(
//                                                string(Text.of("frame_name_num")),
//                                                literal(Text.of("set"), "set"),
//                                                firstParsing(
//                                                        // /animate <name> frame <name|num> set name <new_name>
//                                                        seq(
//                                                                literal(Text.of("name"), "name"),
//                                                                string(Text.of("new_name"))
//                                                        )
//                                                )
//                                        ),
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
