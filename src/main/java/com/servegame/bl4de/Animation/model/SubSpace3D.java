package com.servegame.bl4de.Animation.model;

import com.servegame.bl4de.Animation.AnimationPlugin;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

import static com.servegame.bl4de.Animation.data.DataQueries.*;

/**
 * File: SubSpace3D.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SubSpace3D implements DataSerializable {

    private Location<World> cornerOne = null;
    private Location<World> cornerTwo = null;

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
        this.cornerOne = bound1;
        this.cornerTwo = bound2;
        this.set3DArray();
    }

    /**
     * Copy constructor
     * @param subSpace3D SubSpace
     */
    protected SubSpace3D(SubSpace3D subSpace3D) {
        Optional<Location<World>> cornerOneOptional = subSpace3D.getCornerOne();
        Optional<Location<World>> cornerTwoOptional = subSpace3D.getCornerTwo();
        cornerOneOptional.ifPresent(worldLocation -> this.cornerOne = worldLocation);
        cornerTwoOptional.ifPresent(worldLocation -> this.cornerTwo = worldLocation);
        this.contents = subSpace3D.getContents().orElse(null);
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
            return Optional.of(this.cornerOne);
        }
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
    public Optional<BlockSnapshot[][][]> getContents(){
        return this.contents == null ? Optional.empty() : Optional.of(this.contents);
    }

    /**
     * Setter for the contents of the given {@link SubSpace3D}
     * @param contents contents to set
     */
    public void setContents(BlockSnapshot[][][] contents){
        this.contents = contents;
    }

    @Override
    public String toString() {
        String result =  "\n*******************************   SUBSPACE INFO    ******************************\n" +
                "Corner one: " + this.getCornerOne().map(Location::toString).orElse("nil") + "\n" +
                "Corner two: " + this.getCornerTwo().map(Location::toString).orElse("nil") + "\n";
        if (this.contents == null){
            // No contents to show
            result += "Contents: nil\n";
        } else {
            result += "Contents: " + this.contents.toString() + "[" + this.contents.length + "][" + this.contents[0].length + "][" + this.contents[0][0].length + "]\n";
        }
        return result;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        Optional<Location<World>> optionalLocation1 = getCornerOne();
        Optional<Location<World>> optionalLocation2 = getCornerTwo();
        DataContainer container = DataContainer.createNew();
        optionalLocation1.ifPresent(worldLocation -> container.set(SUBSPACE_CORNER_ONE, worldLocation.createSnapshot()));
        optionalLocation2.ifPresent(worldLocation -> container.set(SUBSPACE_CORNER_TWO, worldLocation.createSnapshot()));
        Optional<BlockSnapshot[][][]> optionalBlockSnapshots = this.getContents();
        if (optionalBlockSnapshots.isPresent()){
            BlockSnapshot[][][] blockSnapshots = optionalBlockSnapshots.get();
            int x, y, z;
            x = blockSnapshots.length;
            y = blockSnapshots[0].length;
            z = blockSnapshots[0][0].length;
            container.set(DataQuery.of("x"), x);
            container.set(DataQuery.of("y"), y);
            container.set(DataQuery.of("z"), z);
            // X
            for (int i = 0; i < x; i++) {
                // Y
                for (int j = 0; j < y; j++) {
                    // Z
                    for (int k = 0; k < z; k++) {
                        container.set(DataQuery.of("" + i + j + k), blockSnapshots[i][j][k].toContainer());
                    }
                }
            }
            container.set(SUBSPACE_CONTENTS, true);
        }
        return container;
    }

    /**
     * TODO
     */
    public static class Builder extends AbstractDataBuilder<SubSpace3D> {

        /**
         * TODO
         */
        public Builder(){
            super(SubSpace3D.class, 0);
        }

        @Override
        protected Optional<SubSpace3D> buildContent(DataView container) throws InvalidDataException {
            SubSpace3D subSpace3D = new SubSpace3D();
            if (AnimationPlugin.instance.isDebug()){
                for (DataQuery query :
                     container.getKeys(false)) {
                    System.out.println("Subspace Queries: " + query.toString());
                }
                System.out.println(container);
            }
            if (container.contains(SUBSPACE_CORNER_ONE)){
                Optional<BlockSnapshot> optionalBlockSnapshot = BlockSnapshot.builder().build((DataView) container.get(SUBSPACE_CORNER_ONE).get());
                if (!optionalBlockSnapshot.isPresent()){
                    System.err.println("NO BLOCKSNAPSHOT - Subspace corner one");
                }
                Optional<Location<World>> optionalLocation = optionalBlockSnapshot.get().getLocation();
                if (!optionalLocation.isPresent()){
                    System.err.println("NO LOCATION - Subspace corner one");
                }
                Location<World> location = optionalLocation.get();
                subSpace3D.setCornerOne(location); // TODO Clean?
            }
            if (container.contains(SUBSPACE_CORNER_TWO)){
                Optional<BlockSnapshot> optionalBlockSnapshot = BlockSnapshot.builder().build((DataView) container.get(SUBSPACE_CORNER_TWO).get());
                if (!optionalBlockSnapshot.isPresent()){
                    System.err.println("NO BLOCKSNAPSHOT - Subspace corner two");
                }
                Optional<Location<World>> optionalLocation = optionalBlockSnapshot.get().getLocation();
                if (!optionalLocation.isPresent()){
                    System.err.println("NO LOCATION - Subspace corner two");
                }
                Location<World> location = optionalLocation.get();
                subSpace3D.setCornerTwo(location); // TODO Clean?
            }
            if (container.contains(SUBSPACE_CONTENTS)){
                int x, y, z;
                x = container.getInt(DataQuery.of("x")).get();
                y = container.getInt(DataQuery.of("y")).get();
                z = container.getInt(DataQuery.of("z")).get();

                BlockSnapshot[][][] blockSnapshots = new BlockSnapshot[x][y][z];
                // X
                for (int i = 0; i < x; i++) {
                    // Y
                    for (int j = 0; j < y; j++) {
                        // Z
                        for (int k = 0; k < z; k++) {
                            blockSnapshots[i][j][k] = BlockSnapshot.builder().build(container.getView(DataQuery.of("" + i + j + k)).get()).get();
                        }
                    }
                }
                subSpace3D.setContents(blockSnapshots);
            }
            return Optional.of(subSpace3D);
        }
    }
}
