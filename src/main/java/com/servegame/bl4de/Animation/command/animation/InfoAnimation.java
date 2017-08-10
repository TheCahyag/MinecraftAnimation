package com.servegame.bl4de.Animation.command.animation;

import com.servegame.bl4de.Animation.models.Animation;
import com.servegame.bl4de.Animation.util.TextResponses;
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

import static com.servegame.bl4de.Animation.util.Util.*;
import static org.spongepowered.api.text.format.TextColors.*;

import javax.annotation.Nullable;
import java.util.Optional;

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
        cornerOne = cornerOneOptional.map(Location::toString).orElse("nil");
        cornerTwo = cornerTwoOptional.map(Location::toString).orElse("nil");

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
                        SECONDARY_COLOR, animation.getTickDelay() + "\n",
                        PRIMARY_COLOR, "Start Frame",
                        WHITE, ": ",
                        SECONDARY_COLOR, this.animation.getFrameIndex() + "\n",
                        PRIMARY_COLOR, "Cycles to Complete",
                        WHITE, ": ",
                        SECONDARY_COLOR, this.animation.getCycles() + "\n"))
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------\n"))
                .append(getAnimationButtons(this.animation))
                .append(Text.of(SECONDARY_COLOR, "----------------------------------------------------"))
                .build();
        src.sendMessage(message);
        return CommandResult.success();
    }
}