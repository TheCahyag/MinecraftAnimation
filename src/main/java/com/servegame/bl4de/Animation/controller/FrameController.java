package com.servegame.bl4de.Animation.controller;

import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import static com.servegame.bl4de.Animation.util.Util.*;

import java.util.List;
import java.util.Random;

/**
 * File: FrameController.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class FrameController {
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
                valid = true;
            }
        }
        // This line should never execute
        return ((Integer) new Random().nextInt(999999)).toString();
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
        // TODO
        return null;
    }
}
