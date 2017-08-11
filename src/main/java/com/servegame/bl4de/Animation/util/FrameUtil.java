package com.servegame.bl4de.Animation.util;

import com.servegame.bl4de.Animation.models.Animation;
import com.servegame.bl4de.Animation.models.Frame;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import static com.servegame.bl4de.Animation.util.Util.*;
import static org.spongepowered.api.text.format.TextColors.*;

import java.util.List;
import java.util.Random;

/**
 * File: FrameUtil.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class FrameUtil {
    private static int lastNumberForName = 0;

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
     * redundent code the first time it creates a frame name.
     *
     * @param animation the {@link Animation} the {@link Frame} will belong to
     * @return String - name for the frame
     */
    public static String generateFrameName(Animation animation){
        int counter = lastNumberForName;
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
                lastNumberForName = ++counter;
                return nameTrial;
            } else {
                counter++;
            }
        }
        // This line should never execute
        return ((Integer) new Random().nextInt(999999)).toString();
    }

    public static void displayFrame(Frame frame){
        // This should be moved to either the Frame or Animation Class
    }

    public static Text linkToFrameInfo(Frame frame, Animation animation){
        Text message = Text.builder()
                .append(Text.of(NAME_COLOR, COMMAND_STYLE, frame.getName()))
                .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame " + frame.getName() + " info"))
                .build();
        return message;
    }

    public static Text getButtonsForList(Animation animation){
        Text message = Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "CREATE FRAME",
                                PRIMARY_COLOR, "]    "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame create"))
                        .build())
                .build();
        return message;
    }

    public static Text getButtonsForFrameInfo(Frame frame, Animation animation){
        return null;
    }
}
