package com.servegame.bl4de.Animation.data;

import org.spongepowered.api.data.DataQuery;

import static org.spongepowered.api.data.DataQuery.of;

/**
 * File: DataQueries.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class DataQueries {
    // Animation
    public static final DataQuery ANIMATION_NAME = of();
    public static final DataQuery ANIMATION_OWNER = of();
    public static final DataQuery ANIMATION_FRAMES = of();
    public static final DataQuery ANIMATION_SUBSPACE = of();
    public static final DataQuery ANIMATION_FRAME_INDEX = of();
    public static final DataQuery ANIMATION_TICK_DELAY = of();
    public static final DataQuery ANIMATION_CYCLES = of();
    public static final DataQuery ANIMATION_STATUS = of();

    // Frame
    public static final DataQuery FRAME_CREATOR = of();
    public static final DataQuery FRAME_NAME = of();

    // SubSpace
    public static final DataQuery SUBSPACE_CORNER_ONE = of();
    public static final DataQuery SUBSPACE_CORNER_TWO = of();
    public static final DataQuery SUBSPACE_CONTENTS = of();
}
