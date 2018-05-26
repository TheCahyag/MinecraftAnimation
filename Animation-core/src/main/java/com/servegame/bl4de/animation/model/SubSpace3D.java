package com.servegame.bl4de.animation.model;

import com.servegame.bl4de.animation.block.Block;
import com.servegame.bl4de.animation.data.DataSerializable;
import com.servegame.bl4de.animation.world.Vector3D;
import com.servegame.bl4de.animation.world.World;

import java.util.Optional;

/**
 * File: SubSpace3D.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SubSpace3D extends MasterSubSpace3D implements Displayable {

    /* State */

    /** TODO **/
    private Block[][][] content;

    /* Constructors */

    public SubSpace3D() {
    }

    public SubSpace3D(SubSpace3D subSpace3D) {
        super(subSpace3D);
        this.content = subSpace3D.getContent().orElse(null);
    }

    /* Methods */

    /* START: Getters and Setters */

    public Optional<Block[][][]> getContent() {
        return Optional.ofNullable(this.content);
    }

    public void setContent(Block[][][] content) {
        this.content = content;
    }

    /* END: Getters and Setters */
}
