package com.servegame.bl4de.Animation;

import com.google.inject.Inject;
import com.servegame.bl4de.Animation.data.SQLManager;
import com.servegame.bl4de.Animation.task.TaskManager;
import com.servegame.bl4de.Animation.util.Resource;
import com.servegame.bl4de.Animation.util.Util;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.File;

/**
 * File: AnimationPlugin.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION,
        authors = Resource.AUTHORS, url = Resource.URL)
public class AnimationPlugin {

    @Inject
    public static Logger logger;
    public static AnimationPlugin instance;
    public static PluginContainer plugin;
    public static TaskManager taskManager;
    public static SQLManager sqlManager;

    @Inject
    private Game game;

    private final String CONFIG_DIR = "./config/animation";
    private final String ANIMATION_DATA_DIR = CONFIG_DIR + "/animations";

    private boolean debug;

    @Listener
    public void onConstruction(GameConstructionEvent event){
        instance = this;
        plugin = Sponge.getPluginManager().getPlugin(Resource.ID).get();
        taskManager = new TaskManager();
    }

    @Listener
    public void onPreInit(GamePreInitializationEvent event){

    }

    @Listener
    public void onInit(GameInitializationEvent event){
        // Check for config directory
        if (!new File(CONFIG_DIR).exists()){
            //noinspection ResultOfMethodCallIgnored
            new File(CONFIG_DIR).mkdir();
        }

        this.debug = true;
        Util.registerCommands(this);
        taskManager.stopAllAnimations();
        sqlManager = SQLManager.get(plugin);

    }

    @Listener
    public void onLoad(GameLoadCompleteEvent event){
    }

    @Listener
    public void onStop(GameStoppingEvent event){
        //logger.info("Stopping animations...");
        System.out.println("Stopping animations...");
        //AnimationUtil.stopAllAnimations();
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
