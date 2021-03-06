package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.Permissions;
import com.servegame.bl4de.Animation.command.AbstractCommand;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.util.TextResponses;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

import static com.servegame.bl4de.Animation.util.Util.*;
import static org.spongepowered.api.text.format.TextColors.WHITE;

/**
 * File: InfoAnimation.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class InfoAnimation extends AbstractCommand<CommandSource> {

    private Animation animation;

    public InfoAnimation(Animation animation, CommandSource src, CommandContext args){
        super(src, args);
        this.animation = animation;
    }

    @Override
    public boolean checkPermission() {
        return this.src.hasPermission(Permissions.ANIMATION_INFO);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }

        if (AnimationPlugin.instance.isDebug()){
            System.out.println(this.animation.toString());
        }

        // Get offline player
        Optional<UserStorageService> userStorageServiceOptional = Sponge.getServiceManager().provide(UserStorageService.class);
        if (!userStorageServiceOptional.isPresent()){
            src.sendMessage(Text.of(ERROR_COLOR, "Uh oh, the UserStorageService.class couldn't be obtained..."));
            return CommandResult.success();
        }
        UserStorageService userStorageService = userStorageServiceOptional.get();
        Optional<Location<World>> cornerOneOptional = this.animation.getSubSpace().getCornerOne();
        Optional<Location<World>> cornerTwoOptional = this.animation.getSubSpace().getCornerTwo();

        // Get corner strings
        String cornerOne, cornerTwo;
        cornerOne = cornerOneOptional.map(Util::locationToString).orElse("nil");
        cornerTwo = cornerTwoOptional.map(Util::locationToString).orElse("nil");

        // The unit for the tick delay (either tick or ticks and not tick(s) because that looks bad)
        String tickUnit = this.animation.getTickDelay() == 1 ? "tick" : "ticks";

        // Extra information for cycles information (currently only adding one for -1)
        String cycleInfo = this.animation.getCycles() == -1 ? "(infinite)" : "";

        // Start frame name
        String startFrameName = "nil";
        Optional<Frame> frameOptional = this.animation.getFrame(this.animation.getStartFrameIndex());
        if (frameOptional.isPresent()){
            startFrameName = frameOptional.get().getName();
        }

        Text message = Text.builder()
                .append(Text.of(PRIMARY_COLOR, "Name",
                        WHITE, ": ",
                        NAME_COLOR, this.animation.getAnimationName() + "\n",
                        PRIMARY_COLOR, "Owner",
                        WHITE, ": ",
                        NAME_COLOR, userStorageService.get(this.animation.getOwner()).get().getName() + "\n",
                        PRIMARY_COLOR, "# of Frames",
                        WHITE, ": ",
                        SECONDARY_COLOR, this.animation.getNumOfFrames() + "\n",
                        PRIMARY_COLOR, "Status",
                        WHITE, ": ",
                        SECONDARY_COLOR, this.animation.getStatus() + "\n"))
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "Corner 1"))
                        .onHover(CORNER_HOVER)
                        .onClick(TextActions.executeCallback(commandSource -> showVirtualCorner(commandSource, this.animation.getSubSpace().getCornerOne().orElse(null))))
                        .build())
                .append(Text.of(WHITE, ": ",
                        SECONDARY_COLOR, cornerOne + "\n"))
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "Corner 2"))
                        .onHover(CORNER_HOVER)
                        .onClick(TextActions.executeCallback(commandSource -> showVirtualCorner(commandSource, this.animation.getSubSpace().getCornerTwo().orElse(null))))
                        .build())
                .append(Text.of(WHITE, ": ",
                        SECONDARY_COLOR, cornerTwo + "\n",
                        PRIMARY_COLOR, "Frame Delay",
                        WHITE, ": ",
                        SECONDARY_COLOR, this.animation.getTickDelay() + " " + tickUnit + "\n",
                        PRIMARY_COLOR, "Start Frame",
                        WHITE, ": ",
                        SECONDARY_COLOR, this.animation.getStartFrameIndex() + " (" + startFrameName + ")\n",
                        PRIMARY_COLOR, "Cycles to Complete",
                        WHITE, ": ",
                        SECONDARY_COLOR, this.animation.getCycles() + " " + cycleInfo + "\n"))
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------\n"))
                .append(getAnimationButtons(this.animation))
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------"))
                .build();
        src.sendMessage(message);
        return CommandResult.success();
    }

    private void showVirtualCorner(CommandSource src, Location<World> location){
        if (location == null){
            src.sendMessage(TextResponses.ANIMATION_CORNER_NOT_SET);
            return;
        }
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return;
        }
        Player player = ((Player) src);
        player.sendBlockChange(location.getBlockPosition(), BlockState.builder().blockType(BlockTypes.BEDROCK).build());
    }
}
