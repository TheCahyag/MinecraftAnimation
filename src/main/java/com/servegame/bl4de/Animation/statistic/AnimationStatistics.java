package com.servegame.bl4de.Animation.statistic;


import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * File: AnimationStatistics.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class AnimationStatistics {

    /** Map<Pair<Player UUID, World UUID of Animation>, Name of Animation> **/
    private Map<Pair<UUID, UUID>, String> allAnimations;

    public AnimationStatistics(){
        this.allAnimations = new HashMap<>();
    }
}
