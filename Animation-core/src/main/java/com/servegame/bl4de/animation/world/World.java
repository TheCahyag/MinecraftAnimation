package com.servegame.bl4de.animation.world;

import com.servegame.bl4de.animation.world.extent.Extent;

/**
 * File: World.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public interface World extends Extent {

    /**
     * TODO
     * @return
     */
    String getName();

    int getMaxY();

    boolean setBlock(); // TODO add parameters`

}
