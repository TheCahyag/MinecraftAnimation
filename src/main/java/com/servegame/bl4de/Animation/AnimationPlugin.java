package com.servegame.bl4de.Animation;

import com.google.inject.Inject;
import com.servegame.bl4de.Animation.util.Util;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameLoadCompleteEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;

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
    public static Logger logger;

    @Inject
    private Game game;

    private final String CONFIG_DIR = "./config/animation";
    private final String ANIMATION_DATA_DIR = CONFIG_DIR + "/animations";

    private boolean debug;

    @Listener
    public void onInit(GameInitializationEvent event){
        // Check for config directory
        if (!new File(CONFIG_DIR).exists()){
            //noinspection ResultOfMethodCallIgnored
            new File(CONFIG_DIR).mkdir();
        }
        // Check for the directory that will store the animation files
        if (!new File(ANIMATION_DATA_DIR).exists()){
            //noinspection ResultOfMethodCallIgnored
            new File(ANIMATION_DATA_DIR).mkdir();
        }
        this.debug = false;
        Util.registerCommands(this);
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
