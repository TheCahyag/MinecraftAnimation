package com.servegame.bl4de.Animation.util;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.command.AbstractRunnableCommand;
import com.servegame.bl4de.Animation.command.CommandGateKeeper;
import com.servegame.bl4de.Animation.command.DebugToggle;
import com.servegame.bl4de.Animation.command.animation.*;
import com.servegame.bl4de.Animation.command.animation.action.PauseAnimation;
import com.servegame.bl4de.Animation.command.animation.action.StartAnimation;
import com.servegame.bl4de.Animation.command.animation.action.admin.StopAllAnimations;
import com.servegame.bl4de.Animation.command.animation.action.StopAnimation;
import com.servegame.bl4de.Animation.command.animation.admin.ListAllAnimations;
import com.servegame.bl4de.Animation.command.animation.admin.RefreshAnimations;
import com.servegame.bl4de.Animation.command.animation.admin.StatisticAnimation;
import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.SubSpace3D;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.scheduler.Task;
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
    public static final HoverAction.ShowText CORNER_HOVER = TextActions.showText(Text.of("Click here to show the corner."));
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
                    BlockSnapshot snapshot = new Location<>(
                        corner1.getExtent(), x + xLow, y + yLow, z + zLow)
                        .createSnapshot();
                    if (!snapshot.getState().getType().getName().equalsIgnoreCase("minecraft:air")){
                        subSpace[x][y][z] = snapshot;
                    }
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

        /* Get corner to start the copy at by getting the coordinates of the
         numerically lowest coordinates of the two corners
         (may result in a corner different from the parameters)*/
        int xLow = corner1.getBlockX() <= corner2.getBlockX() ? corner1.getBlockX() : corner2.getBlockX();
        int yLow = corner1.getBlockY() <= corner2.getBlockY() ? corner1.getBlockY() : corner2.getBlockY();
        int zLow = corner1.getBlockZ() <= corner2.getBlockZ() ? corner1.getBlockZ() : corner2.getBlockZ();

        int xLength = subSpaceSnapShot.length;
        int yLength = subSpaceSnapShot[0].length;
        int zLength = subSpaceSnapShot[0][0].length;

        // Y
        for (int x = 0; x < xLength; x++) {
            // Z
            for (int y = 0; y < yLength; y++) {
                // X
                for (int z = 0; z < zLength; z++) {
                    BlockState state;
                    BlockSnapshot snapshot = subSpaceSnapShot[x][y][z];
                    if (snapshot == null){
                        // If it's null in the array, it represents air
                        state = BlockState.builder().blockType(BlockTypes.AIR).build();
                    } else {
                        state = snapshot.getState();
                    }
                    Location loc = new Location<>(corner1.getExtent(), x + xLow, y + yLow, z + zLow);
                    loc.setBlock(state);
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
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate stop " + animation.getAnimationName()))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "SETTINGS",
                                PRIMARY_COLOR, "]"))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting"))
                        .build())
                .build();
        return message;
    }

    public static Text getSettingsButtons(Animation animation){
        return Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "ANIMATION",
                                PRIMARY_COLOR, "]  "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " info"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "FRAME DELAY",
                                PRIMARY_COLOR, "]  "))
                        .onClick(TextActions.executeCallback(commandSource -> commandSource.sendMessage(settingDelayButtons(animation))))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "CYCLES",
                                PRIMARY_COLOR, "]  "))
                        .onClick(TextActions.executeCallback(commandSource -> commandSource.sendMessage(settingCyclesButtons(animation))))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "START FRAME",
                                PRIMARY_COLOR, "]  "))
                        .onClick(TextActions.executeCallback(commandSource -> commandSource.sendMessage(settingStartFrameButtons(animation))))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "OTHER",
                                PRIMARY_COLOR, "]\n"))
                        .onClick(TextActions.executeCallback(commandSource -> commandSource.sendMessage(settingOtherButtons(animation))))
                        .build())
                .build();
    }

    public static Text settingDelayButtons(Animation animation){
        return Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "FRAME DELAY",
                        TextColors.WHITE, ":"))
                        .onHover(COMMAND_HOVER)
                        .onClick(TextActions.suggestCommand("/animate " + animation.getAnimationName() + " setting delay "))
                        .build())
                .append(Text.of("    "))
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, "-10",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting delay increment -10"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, "-1",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting delay increment -1"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, "20",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting delay 20"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, "+1",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting delay increment 1"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, "+10",
                                PRIMARY_COLOR, "]\n"))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting delay increment 10"))
                        .build())
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------"))
                .build();
    }

    public static Text settingCyclesButtons(Animation animation){
        return Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "CYCLES",
                                TextColors.WHITE, ":"))
                        .onHover(COMMAND_HOVER)
                        .onClick(TextActions.suggestCommand("/animate " + animation.getAnimationName() + " setting cycles "))
                        .build())
                .append(Text.of("           "))
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, "-5",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting cycles increment -5"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, "-1",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting cycles increment -1"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                SECONDARY_COLOR, "\"",
                                ACTION_COLOR, "-1",
                                SECONDARY_COLOR, "\"",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting cycles -1"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, "+1",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting cycles increment 1"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, "+5",
                                PRIMARY_COLOR, "]\n"))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting cycles increment 5"))
                        .build())
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------"))
                .build();
    }

    public static Text settingStartFrameButtons(Animation animation){
        return Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "START FRAME",
                                TextColors.WHITE, ":"))
                        .onHover(COMMAND_HOVER)
                        .onClick(TextActions.suggestCommand("/animate " + animation.getAnimationName() + " setting frame_index "))
                        .build())
                .append(Text.of("    "))
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, "FIRST",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting frame_index first"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, "LAST",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting frame_index last"))
                        .build())
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------"))
                .build();
    }

    public static Text settingOtherButtons(Animation animation){
        return Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "OVERWRITE WORLD UUID",
                                PRIMARY_COLOR, "]\n"))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " setting overwriteworlduuid"))
                        .build())
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------"))
                .build();
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
        long xLen = Math.abs(Math.abs(corner1.getBlockX()) - Math.abs(corner2.getBlockX())) + 1;
        long yLen = Math.abs(Math.abs(corner1.getBlockY()) - Math.abs(corner2.getBlockY())) + 1;
        long zLen = Math.abs(Math.abs(corner1.getBlockZ()) - Math.abs(corner2.getBlockZ())) + 1;
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

    public static class StringContinuation {
        private String modifiedString;
        private StringContinuation nextString;

        public StringContinuation(String toModify){
            this.nextString = null;
            this.modifiedString = encapColonsContinuation(toModify);

        }

        public String getString(){
            String next = this.nextString == null ? "" : this.nextString.getString();
            return this.modifiedString + next;
        }

        private String encapColonsContinuation(String string) {
            String data = string;
            if (data.contains(":")) {
                int colonIndex = data.indexOf(":"), equalIndex = 0, endIndex = 0;

                // Find equal sign
                for (int i = colonIndex; i > 0; i--) {
                    if (data.charAt(i) == '=') {
                        equalIndex = i;
                        break;
                    }
                }

                int level = 0;
                // Find end symbol (either ',' or '}'
                OUTER:
                for (int i = colonIndex; i < data.length(); i++) {
                    if (level == 0) {
                        switch (data.charAt(i)) {
                            case ',':
                            case '}':
                                endIndex = i;
                                break OUTER;
                            case '[':
                            case '{':
                                level++;
                        }
                    } else {
                        if (data.charAt(i) == ']' || data.charAt(i) == '}') {
                            level--;
                        }
                    }
                }
                assert equalIndex != 0 && endIndex != 0 && equalIndex < endIndex;
                String before, colonContaining, after;
                // Check if there is a " right after the equals
                if (data.charAt(equalIndex + 1) == '"') {
                    before = data.substring(0, equalIndex + 1);
                    colonContaining = data.substring(equalIndex + 1, endIndex);
                    after = data.substring(endIndex, data.length());
                } else {
                    // Insert '"' marks at the beginning and end
                    before = data.substring(0, equalIndex + 1);
                    colonContaining = "\"" + data.substring(equalIndex + 1, endIndex) + "\"";
                    after = data.substring(endIndex, data.length());
                }
                this.nextString = new StringContinuation(after);

                data = before + colonContaining;
            }
            return data;
        }
    }

    /**
     * TODO
     * @param string
     * @return
     */
    public static String encapColons(String string){
        StringContinuation sc = new StringContinuation(string);
        return sc.getString();
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
     * Creates a {@link Task.Builder#async()} {@link Task} that executes the action command being ran
     * @param commandInstance instance of a {@link AbstractRunnableCommand}
     * @return {@link CommandResult#success()}
     */
    public static CommandResult executeRunnableCommand(AbstractRunnableCommand commandInstance){
        Task.Builder taskBuilder = Task.builder().async().execute(commandInstance);
        taskBuilder.submit(AnimationPlugin.plugin);
        return CommandResult.success();
    }

    /**
     * Register command with the Sponge {@link CommandManager}
     * @param plugin {@link AnimationPlugin} plugin instance
     */
    public static void registerCommands(AnimationPlugin plugin){
        CommandManager commandManager = Sponge.getCommandManager();
        AnimationPlugin.logger.info("Registering commands...");
        // /animate
        CommandSpec baseAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Info about the Animation Plugin"))
                .executor(new BaseAnimation())
                .build();

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
                        firstParsing(
                                flags()
                                        .valueFlag(integer(Text.of(FLAG_COLOR, "frame")), "f") // -f <num>
                                        .buildWith(none()),
                                flags()
                                        .valueFlag(integer(Text.of(FLAG_COLOR, "delay")), "d") // -d <num>
                                        .buildWith(none()),
                                flags()
                                        .valueFlag(integer(Text.of(FLAG_COLOR, "cycles")), "c") // -c <num>
                                        .buildWith(none())
                        )
                )
                .executor((src, args) -> executeRunnableCommand(new StartAnimation(src, args)))
                .build();

        // /animate stop <name>
        CommandSpec stopAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Stop a given animation"))
                .permission(Permissions.ANIMATION_STOP)
                .arguments(string(Text.of(NAME_COLOR, "animation_name")))
                .executor((src, args) -> new StopAnimation(src, args).runCommand())
                .build();

        // /animate pause <name>
        CommandSpec pauseAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Pause a given animation"))
                .permission(Permissions.ANIMATION_PAUSE)
                .arguments(string(Text.of("animation_name")))
                .executor((src, args) -> executeRunnableCommand(new PauseAnimation(src, args)))
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

        // /animate stopall -f
        CommandSpec stopAllAnimation = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Stop all animations that are currently playing"))
                .permission(Permissions.STOP_ALL_ANIMATIONS)
                .arguments(optional(flags().flag("f").buildWith(none()))) // -f
                .executor((src, args) -> executeRunnableCommand(new StopAllAnimations(src, args)))
                .build();

        // /animate refreshAnimations
        CommandSpec refreshAnimations = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Refreshes all animations in the database (see wiki for more details)"))
                .permission(Permissions.REFRESH_ALL_ANIMATIONS)
                .executor(new RefreshAnimations())
                .build();

        // /animate listall
        CommandSpec listAllAnimations = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Lists all animations that have been created by all users"))
                .permission(Permissions.LIST_ALL_ANIMAITONS)
                .executor(((src, args) -> executeRunnableCommand(new ListAllAnimations(src, args))))
                .build();

        // /animate
        CommandSpec animate = CommandSpec.builder()
                .description(Text.of(PRIMARY_COLOR, "Base animation command"))
                .child(baseAnimation)
                .child(createAnimation, "create")
                .child(deleteAnimation, "delete")
                .child(helpAnimation, "help", "?")
                .child(listAnimation, "list", "l")
                .child(startAnimation, "start")
                .child(stopAnimation, "stop")
                .child(pauseAnimation, "pause")
                .child(statsAnimation, "stats", "statistics")
                .child(debugAnimation, "debug")
                .child(stopAllAnimation, "stopall")
                .child(refreshAnimations, "refreshAnimations")
                .child(listAllAnimations, "listall", "la")
                .arguments(
                        string(Text.of("animation_name")),
                        firstParsing(
                                // /animate <name> info
                                literal(Text.of("animation_info"), "info"),
                                // /animate <name> set...
                                literal(Text.of("animation_set"), "set"),
                                // /animate <name> frame...
                                literal(Text.of("frame"), "frame"),
                                // /animate <name> setting...
                                literal(Text.of("animation_setting"), "setting")
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
                                        ),
                                        // /animate <name> setting ...
                                        seq(
                                                optional(
                                                        firstParsing(
                                                                // /animate <name> setting delay...
                                                                seq(
                                                                        literal(Text.of("setting_delay"), "delay"),
                                                                        firstParsing(
                                                                                // /animate <name> setting delay #
                                                                                integer(Text.of("setting_delay_num")),
                                                                                // /animate <name> setting delay increment #
                                                                                seq(
                                                                                        literal(Text.of("setting_delay_increment"), "increment"),
                                                                                        integer(Text.of("increment_value"))
                                                                                )
                                                                        )
                                                                ),
                                                                // /animate <name> setting frame_index...
                                                                seq(
                                                                        literal(Text.of("setting_frame_index"), "frame_index"),
                                                                        firstParsing(
                                                                                // /animate <name> setting frame_index #
                                                                                integer(Text.of("setting_frame_index_num")),
                                                                                // /animate <name> setting frame_index first
                                                                                literal(Text.of("setting_frame_index_first"), "first"),
                                                                                // /animate <name> setting frame_index last
                                                                                literal(Text.of("setting_frame_index_last"), "last")
                                                                        )
                                                                ),
                                                                // /animate <name> setting cycles...
                                                                seq(
                                                                        literal(Text.of("setting_cycles"), "cycles"),
                                                                        firstParsing(
                                                                                // /animate <name> setting cycles #
                                                                                integer(Text.of("setting_cycles_num")),
                                                                                // /animate <name> setting cycles increment #
                                                                                seq(
                                                                                        literal(Text.of("setting_cycles_increment"), "increment"),
                                                                                        integer(Text.of("increment_value"))
                                                                                )
                                                                        )
                                                                ),
                                                                // /animate <name> setting overwriteworlduuid
                                                                seq(
                                                                        literal(Text.of("setting_overwrite_world_uuid"), "overwriteworlduuid"),
                                                                        optional(flags().flag("f").buildWith(none()))
                                                                )
                                                        )
                                                )
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
                                                literal(Text.of("delete_frame"), "delete"),
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
        AnimationPlugin.logger.info("...commands registered");
    }
}
