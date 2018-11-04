package com.servegame.bl4de.Animation.controller;

import com.servegame.bl4de.Animation.data.PreparedStatements;
import com.servegame.bl4de.Animation.data.SQLResources;
import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.model.SubSpace3D;
import org.apache.commons.lang3.CharUtils;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.servegame.bl4de.Animation.util.Util.*;

/**
 * File: FrameController.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class FrameController {

    /**
     * Generates a name for a frame by starting with the string frame0,
     * checking if that exists and if not it will check for frame1 and so
     * on until there is an availability
     *
     * TODO:
     * May need to refactor this to remember the last number it was
     * on to reduce redundant code execution
     * This might be fixed now, it won't persist after a restart, but if it 
     * does persist throughout the life of a server it will only be running 
     * redundant code the first time it creates a frame name.
     *
     * @param animation the {@link Animation} the {@link Frame} will belong to
     * @return String - name for the frame
     */
    public static String generateFrameName(Animation animation){
        int counter = animation.lastNumberForFrameName;
        List<Frame> frames = animation.getFrames();
        boolean valid = true;
        while (valid){
            String nameTrial = "frame" + counter;
            for (Frame frame : frames) {
                if (frame.getName().equals(nameTrial)) {
                    valid = false;
                }
            }
            if (valid){
                animation.lastNumberForFrameName = ++counter;
                return nameTrial;
            } else {
                counter++;
                valid = true;
            }
        }
        // This line <i>should</i> never execute
        return "frame" + ((Integer) new Random().nextInt(999999)).toString();
    }

    /**
     *
     * @param animation
     * @param index
     * @return
     */
    public static Optional<Frame> getFrameWithContents(Animation animation, int index){
        String frameName = animation.getFrame(index).get().getName();
        return PreparedStatements.getFrame(
                frameName,
                SQLResources.getFrameTableName(animation),
                SQLResources.getContentTableName(animation, animation.getFrame(index).get()),
                true
        );
    }

    public static Optional<Frame> getFrameWithContents(Animation animation, String name){
        return PreparedStatements.getFrame(
                name,
                SQLResources.getFrameTableName(animation),
                SQLResources.getContentTableName(animation, animation.getFrame(name).get()),
                true
        );
    }

    public static boolean saveFrame(Animation animation, Frame frame){
        return PreparedStatements.saveFrame(animation, frame);
    }

    public static boolean createFrame(Animation animation, Frame frame){
        return PreparedStatements.createFrame(animation, frame);
    }

    public static boolean deleteFrame(Animation animation, Frame frame){
        animation.deleteFrame(frame);
        return PreparedStatements.deleteFrame(animation, frame);
    }

    /**
     * TODO
     * @param name
     * @return
     */
    public static boolean isValidName(String name){
        if (CharUtils.isAsciiNumeric(name.charAt(0))){
            return false;
        } //else if (name.("\\/\"'.,?:"))
        return true;
    }

    /**
     * TODO
     * @param subSpace3D
     * @return
     */
    public static boolean displayContents(SubSpace3D subSpace3D){
        try {
            copySubSpaceToWorld(subSpace3D);
        } catch (UninitializedException e) {
            return false;
        }
        return true;
    }

    /**
     * Calculates the number of blocks that aren't minecraft:air blocks.
     * @param snapshots 3D {@link BlockSnapshot} array
     *                  representing the 3D subspace in game
     * @return int - the number of blocks that aren't air in a given subspace
     */
    public static Integer calculateNotAirBlocks(BlockSnapshot[][][] snapshots){
        int xLen, yLen, zLen;
        xLen = snapshots.length;
        yLen = snapshots[0].length;
        zLen = snapshots[0][0].length;

        int count = 0; // Counter for not air blocks

        // Y
        for (int i = 0; i < yLen; i++) {
            // Z
            for (int j = 0; j < zLen; j++) {
                // X
                for (int k = 0; k < xLen; k++) {
                    if (snapshots[k][i][j] != null){
                        count++;
                    }
                }
            }
        }
        return count;
    }


    /**
     * Create {@link Text} that, when clicked, will run the frame info command on a specific frame
     * @param frame the given {@link Frame}
     * @param animation the {@link Animation} that the {@link Frame} is associated with
     * @return the resulting {@link Text}
     */
    public static Text linkToFrameInfo(Frame frame, Animation animation){
        return Text.builder()
                .append(Text.of(NAME_COLOR, COMMAND_STYLE, frame.getName()))
                .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame " + frame.getName() + " info"))
                .build();
    }

    /**
     * TODO
     * @param animation
     * @return
     */
    public static Text getButtonsForList(Animation animation){
        return Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "CREATE FRAME",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame create"))
                        .build())
                .build();
    }

    /**
     * TODO
     * @param frame
     * @param animation
     * @return
     */
    public static Text getButtonsForFrameInfo(Frame frame, Animation animation){
        return Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "ANIMATION",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " info"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "DISPLAY",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame display " + frame.getName()))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "DUPLICATE",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame duplicate " + frame.getName()))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "UPDATE",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame update " + frame.getName()))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "DELETE",
                                PRIMARY_COLOR, "]"))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame delete " + frame.getName()))
                        .build())
                .build();
    }
}
