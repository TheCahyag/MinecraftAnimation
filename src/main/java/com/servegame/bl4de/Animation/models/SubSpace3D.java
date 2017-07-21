package com.servegame.bl4de.Animation.models;

import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

/**
 * File: SubSpace3D.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SubSpace3D {
    private Location<World> cornerOne;
    private Location<World> cornerTwo;

    /**
     * Does-nothing constructor does nothing
     */
    public SubSpace3D(){}

    /**
     * SubSpace3D constructor
     * @param bound1 {@link Location}
     * @param bound2 {@link Location}
     */
    public SubSpace3D(Location<World> bound1, Location<World> bound2){
        this.cornerOne = bound1;
        this.cornerTwo = bound2;
    }

    /**
     * Copy constructor
     * @param subSpace3D SubSpace
     */
    public SubSpace3D(SubSpace3D subSpace3D){
        this.cornerOne = subSpace3D.getCornerOne();
        this.cornerTwo = subSpace3D.getCornerTwo();
    }

    /**
     * Ensure that the boundaries of the designated subspace has been set
     * @return boolean indicating complete initialization
     */
    public boolean isInitialized() {
        if (this.getCornerOne() == null) {
            return false;
        }
        if (this.getCornerTwo() == null) {
            return false;
        }
        return true;
    }

    /**
     * Getter for corner one
     * @return {@link Location}
     */
    public Location<World> getCornerOne() {
        return cornerOne;
    }

    /**
     * Setter for corner one
     * @param cornerOne {@link Location}
     */
    public void setCornerOne(Location<World> cornerOne) {
        this.cornerOne = cornerOne;
    }

    /**
     * Getter for corner two
     * @return {@link Location}
     */
    public Location<World> getCornerTwo() {
        return cornerTwo;
    }

    /**
     * Setter for corner two
     * @param cornerTwo {@link Location}
     */
    public void setCornerTwo(Location<World> cornerTwo) {
        this.cornerTwo = cornerTwo;
    }
}
