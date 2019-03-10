package com.servegame.bl4de.Animation.exception;

import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.UUID;

/**
 * File: WorldNotPresentException.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class WorldNotPresentException extends Exception {

    private static String formatMessage(UUID worldUUID){
        return "[" + worldUUID.toString() + "] World could not be found when loading the " +
                "animation on this server. See the wiki for more information on this error.";
    }

    private Text detailText;

    /**
     * WorldNotPresentException
     * @param worldUUID - {@link UUID} of the {@link World} that couldn't be loaded
     */
    public WorldNotPresentException(UUID worldUUID) {
        super(formatMessage(worldUUID));
        this.detailText = Text.of(Util.ERROR_COLOR, super.getMessage());
    }

    public Text getDetailText(){
        return this.detailText;
    }
}
