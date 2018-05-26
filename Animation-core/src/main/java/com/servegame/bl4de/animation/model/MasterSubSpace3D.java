package com.servegame.bl4de.animation.model;

import com.servegame.bl4de.animation.data.DataSerializable;
import com.servegame.bl4de.animation.world.World;

import java.util.Optional;

/**
 * File: MasterSubSpace3D.java
 * // TODO explain diff between master and regular subspace
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class MasterSubSpace3D implements Initializable, DataSerializable {

    /* State */

    /** TODO **/
    private Location<World> cornerOne;

    /** TODO **/
    private Location<World> cornerTwo;

    /* Constructors */

    /**
     * TODO
     */
    public MasterSubSpace3D() {}

    /**
     * TODO
     * @param cornerOne TODO
     * @param cornerTwo TODO
     */
    public MasterSubSpace3D(Location<World> cornerOne, Location<World> cornerTwo) {
        this.cornerOne = cornerOne;
        this.cornerTwo = cornerTwo;
    }

    /**
     * TODO
     * @param subSpace3D TODO
     */
    public MasterSubSpace3D(SubSpace3D subSpace3D){
        subSpace3D.getCornerOne().ifPresent(this::setCornerOne);
        subSpace3D.getCornerOne().ifPresent(this::setCornerTwo);
    }

    /* Methods */

    @Override
    public boolean isInit() {

        throw new RuntimeException();
    }

    /* START: Getters and Setters */

    /**
     * TODO
     * @return TODO
     */
    public Optional<Location<World>> getCornerOne() {
        return Optional.ofNullable(this.cornerOne);
    }

    /**
     * TODO
     * @param cornerOne TODO
     */
    public void setCornerOne(Location<World> cornerOne) {
        this.cornerOne = cornerOne;
    }

    /**
     * TODO
     * @return TODO
     */
    public Optional<Location<World>> getCornerTwo() {
        return Optional.ofNullable(this.cornerTwo);
    }

    /**
     * TODO
     * @param cornerTwo TODO
     */
    public void setCornerTwo(Location<World> cornerTwo) {
        this.cornerTwo = cornerTwo;
    }

    /* END: Getters and Setters */

    @Override
    public String toString() {
        return "MasterSubSpace3D{" +
                "cornerOne=" + cornerOne +
                ", cornerTwo=" + cornerTwo +
                '}';
    }
}
