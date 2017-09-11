package com.servegame.bl4de.Animation.command.frame;

import com.servegame.bl4de.Animation.controller.FrameController;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

import static com.servegame.bl4de.Animation.util.Util.*;
import static org.spongepowered.api.text.format.TextColors.*;


/**
 * File: InfoFrame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class InfoFrame implements CommandExecutor {

    private Animation animation;

    public InfoFrame(Animation animation){
        this.animation = animation;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)){
            src.sendMessage(TextResponses.PLAYER_ONLY_COMMAND_WARNING);
            return CommandResult.success();
        }
        Player player = ((Player) src);
        Optional<String> frameNameOptional = args.getOne("frame_name_num");
        if (!frameNameOptional.isPresent()){
            // Frame not specified
            player.sendMessage(TextResponses.FRAME_NOT_SPECIFIED_ERROR);
            return CommandResult.empty();
        }
        String frameName = frameNameOptional.get();
        Optional<Frame> frameOptional;

        if (isNumeric(frameName)){
            // User specified a frame number 0, 1, ..., n
            frameOptional = this.animation.getFrame(Integer.parseInt(frameName));
        } else {
            // User specified a frame name
            frameOptional = this.animation.getFrame(frameName);
        }

        if (!frameOptional.isPresent()){
            // Couldn't find a frame by the given name
            player.sendMessage(TextResponses.FRAME_NOT_FOUND_ERROR);
            return CommandResult.empty();
        }
        Frame frame = frameOptional.get();

        String notAirBlocks;

        Optional<BlockSnapshot[][][]> blockSnapshotsOptional = frame.getContents();

        notAirBlocks = blockSnapshotsOptional
                .map(blockSnapshots -> FrameController.calculateNotAirBlocks(blockSnapshots).toString())
                .orElse("nil");

        Text message = Text.builder()
                .append(Text.of(PRIMARY_COLOR, "Name",
                        WHITE, ": ",
                        NAME_COLOR, frame.getName() + "\n",
                        PRIMARY_COLOR, "Not air blocks",
                        WHITE, ": ",
                        SECONDARY_COLOR, notAirBlocks + "\n",
                        PRIMARY_COLOR, "Volume",
                        WHITE, ": ",
                        SECONDARY_COLOR, calculateVolume(frame) + "\n",
                        SECONDARY_COLOR, "----------------------------------------------------\n"
                        ))
                .append(FrameController.getButtonsForFrameInfo(frame, this.animation))
                .append(Text.of(SECONDARY_COLOR, "\n----------------------------------------------------"))
                .build();
        player.sendMessage(message);
        return CommandResult.success();
    }
}
