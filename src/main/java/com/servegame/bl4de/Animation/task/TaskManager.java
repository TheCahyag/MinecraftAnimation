package com.servegame.bl4de.Animation.task;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.scheduler.Task;

import java.util.*;

/**
 * File: TaskManager.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class TaskManager {
    private Map<UUID, List<AnimationTasks>> runningAnimations = new HashMap<>();
    private PluginContainer plugin;

    /**
     * Constructor for the TaskManager
     */
    public TaskManager(){
        this.plugin = AnimationPlugin.plugin;
    }

    /**
     * Designated createBatch method: Creates {@link Task}s for each frame of the given {@link Animation}
     * @param animation - the {@link Animation} that needs tasks
     * @param owner - the {@link UUID} of the {@link User} who started the {@link Animation}
     * @throws UninitializedException - thrown when the {@link Animation} is not properly initialized
     */
    public void createBatch(Animation animation, UUID owner) throws UninitializedException {
        if (!animation.isInitialized()){
            throw new UninitializedException(TextResponses.ANIMATION_NOT_INITIALIZED_ERROR);
        }
        AnimationTasks animationTasks =
                new AnimationTasks(animation, Sponge.getScheduler().createSyncExecutor(this.plugin));
        animationTasks.populateTaskList();
        if (this.runningAnimations.containsKey(owner)){
            // The owner is running multiple animations
            List<AnimationTasks> tasks = this.runningAnimations.get(owner);
            tasks.add(animationTasks);
            this.runningAnimations.put(owner, tasks);
        } else {
            // The owner has no other animations running
            List<AnimationTasks> tasks = new ArrayList<>();
            tasks.add(animationTasks);
            this.runningAnimations.put(owner, tasks);
        }
    }

    /**
     * TODO
     * @param animation
     * @throws UninitializedException
     */
    public void createBatch(Animation animation) throws UninitializedException {
        this.createBatch(animation, animation.getOwner());
    }

    /**
     * Goes through every {@link AnimationTasks} and stops all tasks
     */
    public void stopAllAnimations(){
        for (Map.Entry<UUID, List<AnimationTasks>> entry :
                this.runningAnimations.entrySet()) {
            entry.getValue().forEach(AnimationTasks::stopAll);
            this.runningAnimations.remove(entry.getKey());
        }
    }
}
