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
    public static final DataQuery ANIMATION_NAME =          of("animation_name");
    public static final DataQuery ANIMATION_OWNER =         of("animation_owner");
    public static final DataQuery ANIMATION_FRAMES =        of("animation_frames");
    public static final DataQuery ANIMATION_SUBSPACE =      of("animation_subspace");
    public static final DataQuery ANIMATION_FRAME_INDEX =   of("animation_frame_index");
    public static final DataQuery ANIMATION_TICK_DELAY =    of("animation_tick_delay");
    public static final DataQuery ANIMATION_CYCLES =        of("animation_cycles");
    public static final DataQuery ANIMATION_STATUS =        of("animation_status");

    // Frame
    public static final DataQuery FRAME_CREATOR =           of("frame_creator");
    public static final DataQuery FRAME_NAME =              of("frame_name");
    public static final DataQuery FRAME_SUBSPACE =          of("frame_subspace");

    // SubSpace
    public static final DataQuery SUBSPACE_CORNER_ONE =     of("subspace_corner_one");
    public static final DataQuery SUBSPACE_CORNER_TWO =     of("subspace_corner_two");
    public static final DataQuery SUBSPACE_CONTENTS =       of("subspace_contents");
}
