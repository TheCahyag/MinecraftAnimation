package com.servegame.bl4de.Animation;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.plugin.Plugin;

/**
 * File: AnimationPlugin.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
@Plugin(id = "animation", name = "animation", version = "0.0.0",
        authors = {"TheCahyag"},
        url = "https://github.com/TheCahyag/animation")
public class AnimationPlugin {

    @Inject
    Logger logger;

    @Inject
    Game game;



    private boolean debug;

    @Listener
    public void onInit(GameInitializationEvent event){
        this.debug = false;
    }

    @Listener
    public void onLoad(GameLoadCompleteEvent event){

    }

    /**
     * Getter for debug flag
     * @return boolean debug flag
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Setter for debug flag
     * @param debug new flag
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
