package com.servegame.bl4de.animation.model.type;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * File: AnimationBare.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class AnimationBare extends Animation {

    /* State */

    /** TODO **/
    private List<String> frameNames;

    /* Constructors */

    /**
     * TODO
     */
    public AnimationBare(){}

    /**
     * TODO
     * @param owner TODO
     * @param name TODO
     */
    public AnimationBare(UUID owner, String name) {
        super(owner, name);
        this.frameNames = new ArrayList<>();
    }

    /**
     * TODO
     * @param name TODO
     */
    public AnimationBare(String name) {
        super(name);
        this.frameNames = new ArrayList<>();
    }

    /* Methods */

    @Override
    public boolean isInit() {
        // TODO
        throw new RuntimeException();
    }
}
