package com.servegame.bl4de.Animation.util;

import com.servegame.bl4de.Animation.models.Animation;
import com.servegame.bl4de.Animation.models.Frame;

import java.util.List;
import java.util.Random;

/**
 * File: FrameUtil.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class FrameUtil {

    /**
     * Generates a name for a frame by starting with the string frame0,
     * checking if that exists and if so will check for frame1 and so
     * on until there is an availability
     *
     * TODO:
     * May need to refactor this to remember the last number it was
     * on to reduce redundant code execution
     * @param animation the {@link Animation} the {@link Frame} will belong to
     * @return String - name for the frame
     */
    public static String generateFrameName(Animation animation){
        int counter = 0;
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
                return nameTrial;
            } else {
                counter++;
            }
        }
        // This line should never execute
        return ((Integer) new Random().nextInt(999999)).toString();
    }
}
