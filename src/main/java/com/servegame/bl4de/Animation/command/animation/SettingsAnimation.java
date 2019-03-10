package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.command.AbstractCommand;
import com.servegame.bl4de.Animation.controller.AnimationController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.chat.ChatTypes;

import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: SettingsAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SettingsAnimation extends AbstractCommand<CommandSource> {

    private Animation animation;

    public SettingsAnimation(Animation animation, CommandSource src, CommandContext args) {
        super(src, args);
        this.animation = animation;
    }

    @Override
    public boolean checkPermission() {
        return true;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = ((Player) src);

        boolean delay = (boolean) args.getOne(Text.of("setting_delay")).orElse(false);
        boolean frameIndex = (boolean) args.getOne(Text.of("setting_frame_index")).orElse(false);
        boolean cycles = (boolean) args.getOne(Text.of("setting_cycles")).orElse(false);
        boolean overwriteWorldUUID = (boolean) args.getOne(Text.of("setting_overwrite_world_uuid")).orElse(false);

        if (delay){
            if (!player.hasPermission(Permissions.ANIMATION_SETTING_DELAY)){
                // User doesn't have permission to change this setting
                player.sendMessage(TextResponses.USER_DOESNT_HAVE_PERMISSION_TO_CHANGE_SETTING);
                return CommandResult.empty();
            }

            boolean increment = (boolean) args.getOne(Text.of("setting_delay_increment")).orElse(false);
                int newDelay;
            if (increment) {
                // Get increment value
                int incrementNum = (Integer) args.getOne(Text.of("increment_value")).get();
                newDelay = this.animation.getTickDelay() + incrementNum;
            } else {
                newDelay = (Integer) args.getOne("setting_delay_num").get();
            }
            if (newDelay < 1) {
                // The resulting delay is 0 or less
                player.sendMessage(TextResponses.ANIMATION_SETTING_DELAY_TOO_LOW);
                return CommandResult.empty();
            }
                this.animation.setTickDelay(newDelay);

                // Save animation
                if (AnimationController.saveBareAnimation(this.animation)){
                    Text delayChangedMessage = Text.of(
                            NAME_COLOR, this.animation.getAnimationName(),
                            PRIMARY_COLOR, "'s frame delay was ",
                            ACTION_COLOR, "set ",
                            PRIMARY_COLOR, "to ",
                            SECONDARY_COLOR, this.animation.getTickDelay(),
                            PRIMARY_COLOR, "."
                    );
                    player.sendMessage(ChatTypes.ACTION_BAR, delayChangedMessage);
                } else {
                    // There was a problem saving the animation
                    player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
                    return CommandResult.empty();
                }
        } else if (frameIndex){
            if (!player.hasPermission(Permissions.ANIMATION_SETTING_FRAME)){
                // User doesn't have permission to change this setting
                player.sendMessage(TextResponses.USER_DOESNT_HAVE_PERMISSION_TO_CHANGE_SETTING);
                return CommandResult.empty();
            }

            boolean first = (boolean) args.getOne(Text.of("setting_frame_index_first")).orElse(false);
            boolean last = (boolean) args.getOne(Text.of("setting_frame_index_last")).orElse(false);

            int newIndex;
            if (first){
                // Set it as the first index
                newIndex = 0;
            } else if (last){
                // Set it as the last index
                newIndex = this.animation.getNumOfFrames() - 1;
            } else {
                // A number was specified
                newIndex = (Integer) args.getOne(Text.of("setting_frame_index_num")).get();
            }

            if (this.animation.getNumOfFrames() == 0){
                // There are no frames
                player.sendMessage(TextResponses.ANIMATION_NO_FRAMES_PRESENT);
                return CommandResult.empty();
            }

            if (newIndex >= this.animation.getNumOfFrames()){
                // The index being set is invalid
                player.sendMessage(TextResponses.GENERAL_INDEX_INVALID_ERROR);
                return CommandResult.empty();
            }

            this.animation.setStartFrameIndex(newIndex);

            if (AnimationController.saveBareAnimation(this.animation)){
                Text frameIndexChangedMessage = Text.of(
                        NAME_COLOR, this.animation.getAnimationName(),
                        PRIMARY_COLOR, "'s start frame was ",
                        ACTION_COLOR, "set ",
                        PRIMARY_COLOR, "to ",
                        SECONDARY_COLOR, this.animation.getStartFrameIndex(),
                        PRIMARY_COLOR, "."
                );
                player.sendMessage(ChatTypes.ACTION_BAR, frameIndexChangedMessage);
            } else {
                // There was a problem saving the animation
                player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
                return CommandResult.empty();
            }
        } else if (cycles) {
            if (!player.hasPermission(Permissions.ANIMATION_SETTING_CYCLES)){
                // User doesn't have permission to change this setting
                player.sendMessage(TextResponses.USER_DOESNT_HAVE_PERMISSION_TO_CHANGE_SETTING);
                return CommandResult.empty();
            }

            boolean increment = (boolean) args.getOne(Text.of("setting_cycles_increment")).orElse(false);
            int newCycles;
            if (increment){
                // An increment value was specified
                int num = (Integer) args.getOne(Text.of("increment_value")).get();
                newCycles = num + this.animation.getCycles();
            } else {
                // Just a number was set
                newCycles = (Integer) args.getOne(Text.of("setting_cycles_num")).get();
            }
            if (newCycles < -1){
                // Invalid number
                player.sendMessage(TextResponses.ANIMATION_SETTING_CYCLES_TOO_LOW);
                return CommandResult.empty();
            }

            this.animation.setCycles(newCycles);

            if (AnimationController.saveBareAnimation(this.animation)){
                Text cyclesChangedMessage = Text.of(
                        NAME_COLOR, this.animation.getAnimationName(),
                        PRIMARY_COLOR, "'s cycles was ",
                        ACTION_COLOR, "set ",
                        PRIMARY_COLOR, "to ",
                        SECONDARY_COLOR, this.animation.getCycles(),
                        PRIMARY_COLOR, "."
                );
                player.sendMessage(ChatTypes.ACTION_BAR, cyclesChangedMessage);
            } else {
                // There was a problem saving the animation
                player.sendMessage(TextResponses.ANIMATION_SAVE_ERROR);
                return CommandResult.empty();
            }
        } else if (overwriteWorldUUID) {
            // Overwrite the world UUID of the two corners of the animation
            if (!player.hasPermission(Permissions.ANIMATION_SETTING_OVERWRITE_WORLD_UUID)){
                // User doesn't have permission to change this setting
                player.sendMessage(TextResponses.USER_DOESNT_HAVE_PERMISSION_TO_CHANGE_SETTING);
                return CommandResult.empty();
            }

            boolean fFlag = args.hasAny(Text.of("f"));
            if (!fFlag){
                // User didn't specify overwrite flag
                player.sendMessage(Text.of(ERROR_COLOR, "If you sure you want to overwrite the world UUID of the '",
                        NAME_COLOR, this.animation.getAnimationName(),
                        ERROR_COLOR, "' animation, run",
                        PRIMARY_COLOR, " /animation " + this.animation.getAnimationName() + " setting overwriteworlduuid",
                        FLAG_COLOR, " -f").toBuilder()
                        .append(Text.of(ERROR_COLOR, COMMAND_STYLE, ", or click this message."))
                        .onClick(TextActions.runCommand("/animate " + this.animation.getAnimationName() + " setting overwriteworlduuid -f"))
                        .onHover(COMMAND_HOVER)
                        .build());
                return CommandResult.success();
            }
            if (AnimationController.overwriteWorldUUID(this.animation, player.getWorld().getUniqueId())){
                // The world uuid was overwritten
                // TODO
            } else {
                // The world uuid has failed to be overwritten
                // TODO
            }
        } else {
            // Show settings buttons
            Text settingsMessage = Text.builder()
                    .append(getSettingsButtons(this.animation))
                    .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------"))
                    .build();
            player.sendMessage(settingsMessage);
        }

        return CommandResult.success();
    }
}
