package com.servegame.bl4de.Animation.models;

import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.util.AnimationUtil;
import com.servegame.bl4de.Animation.util.TextResponses;
import com.servegame.bl4de.Animation.util.Util;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.Serializable;
import java.util.Optional;

/**
 * File: SubSpace3D.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SubSpace3D implements Serializable {
    private static final long serialVersionUID = -34561563741840235L;

    // Location<World>
    private ConfigurationNode cornerOne;
    private ConfigurationNode cornerTwo;

    private BlockSnapshot[][][] subSpace;

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
//        this.cornerOne = AnimationUtil.translateFromConfig(bo);
//        this.cornerTwo = bound2.toContainer(); TODO not sure if this is useable
        this.set3DArray();
    }

    /**
     * Copy constructor
     * @param subSpace3D SubSpace
     */
    public SubSpace3D(SubSpace3D subSpace3D) throws UninitializedException {
        Optional<Location> cornerOneOptional = subSpace3D.getCornerOne();
        Optional<Location> cornerTwoOptional = subSpace3D.getCornerTwo();
        if (subSpace3D.isInitialized()){
            throw new UninitializedException(TextResponses.SUBSPACE_NOT_INITIALIZED_ERROR);
        } else {
            this.cornerOne = AnimationUtil.translateToConfig(cornerOneOptional.get());
            this.cornerTwo = AnimationUtil.translateToConfig(cornerTwoOptional.get());
        }
    }

    /**
     * Fills a 3D primitive {@link BlockSnapshot} array
     */
    private void set3DArray(){
        //this.subSpace = Util.copyWorldToSubSpace(this.cornerOne, this.cornerTwo);
    }

    /**
     * Ensure that the boundaries of the designated subspace has been set
     * @return boolean indicating complete initialization
     */
    public boolean isInitialized() {
        if (this.cornerOne == null) {
            return false;
        }
        if (this.cornerTwo == null) {
            return false;
        }
        return true;
    }

    /**
     * Getter for corner one
     * @return {@link Location}
     */
    public Optional<Location> getCornerOne() {
        if (this.cornerOne == null){
            return Optional.empty();
        } else {
            return AnimationUtil.translateFromConfig(this.cornerOne);
        }
    }

    /**
     * Setter for corner one
     * @param cornerOne {@link Location}
     */
    public void setCornerOne(Location cornerOne) {
        this.cornerOne = AnimationUtil.translateToConfig(cornerOne);
        if (this.isInitialized()){
            this.set3DArray();
        }
    }

    /**
     * Getter for corner two
     * @return {@link Location}
     */
    public Optional<Location> getCornerTwo() {
        if (this.cornerTwo == null){
            return Optional.empty();
        } else {
            return AnimationUtil.translateFromConfig(this.cornerTwo);
        }
    }

    /**
     * Setter for corner two
     * @param cornerTwo {@link Location}
     */
    public void setCornerTwo(Location cornerTwo) {
        this.cornerTwo = AnimationUtil.translateToConfig(cornerTwo);
        if (this.isInitialized()){
            this.set3DArray();
        }
    }
}
