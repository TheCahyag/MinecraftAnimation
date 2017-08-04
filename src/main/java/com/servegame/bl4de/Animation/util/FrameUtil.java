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
