package com.servegame.bl4de.Animation.exception;

import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.text.Text;

/**
 * File: UninitializedException.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class UninitializedException extends Exception {

    private Text detailText;

    /**
     * UninitializedException
     * @param detail Exception detail
     */
    public UninitializedException(String detail){
        super(detail);
    }

    /**
     * Sets the message as a {@link Text} and also gives the
     * super class a plain string to store
     * @param detail {@link Text}
     */
    public UninitializedException(Text detail){
        super(detail.toPlain());
        this.detailText = detail;
    }

    /**
     * Getter for the detail message
     * @return {@link Text}
     */
    public Text getDetailText() {
        if (detailText == null){
            return Text.of(Util.ERROR_COLOR, super.getMessage());
        }
        return detailText;
    }
}
