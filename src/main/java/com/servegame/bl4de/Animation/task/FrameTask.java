package com.servegame.bl4de.Animation.task;

import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.model.SubSpace3D;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.world.Location;

/**
 * File: FrameTask.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public interface FrameTask extends Runnable {

    /**
     * modifyWorld uses a {@link SubSpace3D} to obtain two
     * {@link Location}s that serve as the two opposite
     * corners where blocks will be changed. Additionally,
     * the {@link SubSpace3D} will have a 3D array of
     * {@link BlockSnapshot}s that represent the desired
     * blocks for a given {@link Frame}.
     */
    void modifyWorld();
}
