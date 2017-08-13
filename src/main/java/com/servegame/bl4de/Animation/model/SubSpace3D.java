package com.servegame.bl4de.Animation.model;

import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import static com.servegame.bl4de.Animation.data.DataQueries.*;

import java.util.Optional;

/**
 * File: SubSpace3D.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SubSpace3D implements DataSerializable {

    private Location<World> cornerOne;
    private Location<World> cornerTwo;

    private BlockSnapshot[][][] contents;

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
//        this.cornerOne = bound1.toContainer();
//        this.cornerTwo = bound2.toContainer();
        this.set3DArray();
    }

    /**
     * Copy constructor
     * @param subSpace3D SubSpace
     */
    public SubSpace3D(SubSpace3D subSpace3D) throws UninitializedException {
        Optional<Location<World>> cornerOneOptional = subSpace3D.getCornerOne();
        Optional<Location<World>> cornerTwoOptional = subSpace3D.getCornerTwo();
        if (subSpace3D.isInitialized()){
            throw new UninitializedException(TextResponses.SUBSPACE_NOT_INITIALIZED_ERROR);
        } else {
            this.cornerOne = cornerOneOptional.get();
            this.cornerTwo = cornerTwoOptional.get();
            this.contents = subSpace3D.getContents();
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
    public Optional<Location<World>> getCornerOne() {
        if (this.cornerOne == null){
            return Optional.empty();
        } else {
            return Optional.of(this.cornerOne);        }
    }

    /**
     * Setter for corner one
     * @param cornerOne {@link Location}
     */
    public void setCornerOne(Location<World> cornerOne) {
        this.cornerOne = cornerOne;
        if (this.isInitialized()){
            this.set3DArray();
        }
    }

    /**
     * Getter for corner two
     * @return {@link Location}
     */
    public Optional<Location<World>> getCornerTwo() {
        if (this.cornerTwo == null){
            return Optional.empty();
        } else {
            return Optional.of(this.cornerTwo);
        }
    }

    /**
     * Setter for corner two
     * @param cornerTwo {@link Location}
     */
    public void setCornerTwo(Location<World> cornerTwo) {
        this.cornerTwo = cornerTwo;
        if (this.isInitialized()){
            this.set3DArray();
        }
    }

    /**
     * Getter for the contents of the given {@link SubSpace3D}
     * @return contents received
     */
    public BlockSnapshot[][][] getContents(){
        return this.contents;
    }

    /**
     * Setter for the contents of the given {@link SubSpace3D}
     * @param contents contents to set
     */
    public void setContents(BlockSnapshot[][][] contents){
        this.contents = contents;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = DataContainer.createNew()
                .set(SUBSPACE_CORNER_ONE, getCornerOne())
                .set(SUBSPACE_CORNER_TWO, getCornerTwo())
                .set(SUBSPACE_CONTENTS, getContents());
        return container;
    }

    public static class Builder extends AbstractDataBuilder<SubSpace3D> {

        public Builder(){
            super(SubSpace3D.class, 0);
        }

        @Override
        protected Optional<SubSpace3D> buildContent(DataView container) throws InvalidDataException {
            return null;
        }
    }
}
