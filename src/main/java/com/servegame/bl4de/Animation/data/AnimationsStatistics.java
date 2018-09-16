package com.servegame.bl4de.Animation.data;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.controller.AnimationController;

import java.util.ArrayList;
import java.util.List;

/**
 * File: AnimationStatistics.java
 *
 * Class representing statistics regarding ALL animations. This is used in an admin command.
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class AnimationsStatistics {
    private int runningAnimations;
    private int totalAnimations;

    public AnimationsStatistics(){
        setRunningAnimations();
        setTotalAnimations();
    }

    private void setRunningAnimations() {
        this.runningAnimations = AnimationPlugin.taskManager
                .getRunningAnimations()
                .values()
                .stream()
                .mapToInt(List::size)
                .sum();
    }

    private void setTotalAnimations() {
        this.totalAnimations = AnimationController.getAllAnimations().get()
                .values()
                .stream()
                .mapToInt(ArrayList::size)
                .sum();
    }

    public int getRunningAnimations() {
        return runningAnimations;
    }

    public int getTotalAnimations() {
        return totalAnimations;
    }


}
