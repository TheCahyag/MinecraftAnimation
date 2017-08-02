package com.servegame.bl4de.Animation.models;

import java.io.Serializable;
import java.util.UUID;

/**
 * File: Frame.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class Frame extends SubSpace3D implements Serializable {
    private UUID creator;
    private String name;

    /**
     * Frame constructor that defines the subspace
     * @param creator player {@link UUID}
     * @param name name of the frame
     * @param subSpace {@link SubSpace3D}
     */
    public Frame(UUID creator, String name, SubSpace3D subSpace){
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
}
