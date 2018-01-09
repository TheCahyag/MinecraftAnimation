package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.util.TextResponses;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

import static com.servegame.bl4de.Animation.util.Util.*;
import static org.spongepowered.api.text.format.TextColors.WHITE;

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
                        SECONDARY_COLOR, this.animation.getStatus() + "\n",
                        PRIMARY_COLOR, "Corner 1",
                        WHITE, ": ",
                        SECONDARY_COLOR, cornerOne + "\n",
                        PRIMARY_COLOR, "Corner 2",
                        WHITE, ": ",
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
}
