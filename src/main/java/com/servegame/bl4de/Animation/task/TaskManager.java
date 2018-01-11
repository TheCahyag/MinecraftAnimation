package com.servegame.bl4de.Animation.task;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.TextResponses;
import org.spongepowered.api.entity.living.player.Player;
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
                new AnimationTasks(animation);
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
     * Stops the given {@link Animation}. Currently the {@link UUID}
     * of the {@link Player} is also required. This is to allow for
     * future expansions. Specifically accounting for the ability of
     * a {@link Player} to transfer ownership of the {@link Animation}
     * or if there will be multiple owners for a single {@link Animation}.
     *
     * When calling ensure to specify the {@link Player} who originally
     * started the {@link Animation}.
     * @param animation {@link Animation} to stop
     * @param starter {@link UUID} of the {@link Player} who started the animation
     */
    public boolean stopAnimation(Animation animation, UUID starter) {
        if (!this.runningAnimations.containsKey(starter)) {
            // Owner isn't running any animations
            AnimationPlugin.logger.error(
                    "Couldn't stop "
                            + animation.getAnimationName()
                            + "."
                            + starter.toString()
                            + ": Owner not present in map.");
            return false;
        }
        List<AnimationTasks> tasks = this.runningAnimations.get(starter);
        for (AnimationTasks taskSet :
                tasks) {
            if (taskSet.getAnimationName().equals(animation.getAnimationName())) {
                // Animation name matches
                taskSet.stopAll();
                if (tasks.size() == 0) {
                    // Remove the Map entry
                    this.runningAnimations.remove(starter);
                } else {
                    // Remove the AnimationTasks from the List from the entry
                    tasks.remove(taskSet);
                    this.runningAnimations.replace(starter, tasks);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * {@link TaskManager#stopAnimation(Animation, UUID)}
     * @param animation {@link Animation} that needs to be stopped
     */
    public boolean stopAnimation(Animation animation){
        return this.stopAnimation(animation, animation.getOwner());
    }

    /**
     * Goes through every {@link AnimationTasks} and stops all tasks
     * effectively stopping every {@link Animation}
     */
    public void stopAllAnimations(){
        for (Map.Entry<UUID, List<AnimationTasks>> entry :
                this.runningAnimations.entrySet()) {
            entry.getValue().forEach(AnimationTasks::stopAll);
            this.runningAnimations.remove(entry.getKey());
        }
    }
}
