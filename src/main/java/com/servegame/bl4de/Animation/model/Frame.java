package com.servegame.bl4de.Animation.model;

import com.servegame.bl4de.Animation.util.Util;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import static com.servegame.bl4de.Animation.data.DataQueries.*;

import java.util.Optional;
import java.util.UUID;

/**
 * File: Frame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
@ConfigSerializable
public class Frame extends SubSpace3D implements DataSerializable {

    private UUID creator;
    private String name;

    /**
     * Does-nothing constructor does nothing
     */
    public Frame(){}

    /**
     * Frame constructor that defines the subspace
     * @param creator player {@link UUID}
     * @param name name of the frame
     * @param subSpace {@link SubSpace3D}
     */
    public Frame(UUID creator, String name, SubSpace3D subSpace) {
        super(subSpace); // Init the subspace
        this.creator = creator;
        this.name = name;
    }

    /**
     * Ensure that the Frame's variables have been set
     * @return boolean indicating complete initialization
     */
    public boolean isInitialized(){
        if (!super.isInitialized()){
            return false;
        }
        if (this.name.equals("")){
            return false;
        }
        return true;
    }

    /**
     * Getter for the {@link UUID} of the frame creator
     * @return {@link UUID}
     */
    public UUID getCreator() {
        return creator;
    }

    /**
     * Getter for the name of the frame
     * @return frame name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the frame name
     * @param name frame name
     */
    public void setName(String name) {
        this.name = name;
    }

    public SubSpace3D getSubspace(){
        SubSpace3D tmp = new SubSpace3D(getCornerOne().get(), getCornerTwo().get());
        tmp.setContents(getContents());
        return tmp;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        DataContainer container = new MemoryDataContainer()
                .set(FRAME_CREATOR, getCreator())
                .set(FRAME_NAME, getName())
                .set(FRAME_SUBSPACE, getSubspace());
        return container;
    }

    @Override
    public boolean equals(Object obj) {
        Frame frame;
        if (!(obj instanceof Frame)){
            return false;
        }
        frame = (Frame) obj;
        if (this.getCreator().equals(frame.getCreator())){
            if (this.getName().equals(frame.getName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String message = "********************************  FRAME    INFO   *******************************\n" +
                "Frame Name: " + this.getName() + "\n" +
                "Frame Creator (UUID): " + this.getCreator() + "\n" +
                "Frame Creator (Player Name): " + Util.getOfflinePlayer(this.getCreator(), null);
        message += super.toString();
        return message;
    }

    public static class Builder extends AbstractDataBuilder<Frame> {

        /**
         * TODO
         */
        public Builder() {
            super(Frame.class, 0);
        }

        @Override
        protected Optional<Frame> buildContent(DataView container) throws InvalidDataException {
            Frame frame = null;
            if (container.contains(FRAME_CREATOR, FRAME_NAME, FRAME_SUBSPACE)){
                UUID creator = container.getObject(FRAME_CREATOR, UUID.class).get();
                String name = container.getString(FRAME_NAME).get();
                SubSpace3D subSpace3D = container.getObject(FRAME_SUBSPACE, SubSpace3D.class).get();
                frame = new Frame(creator, name, subSpace3D);
            }
            return Optional.ofNullable(frame);
        }
    }
}
