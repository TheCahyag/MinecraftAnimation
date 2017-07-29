package com.servegame.bl4de.Animation.util;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.models.Animation;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * File: AnimationUtil.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class AnimationUtil {

    private static final String CONFIG_DIR = "./config/animation";
    private static final String ANIMATION_DATA_DIR = CONFIG_DIR + "/animations";

    /**
     * Searches through all files and will check if there is a valid {@link Animation} with a given owner
     * @param name name of the animation
     * @param owner UUID of the owner
     * @return Optional of the {@link Animation}
     */
    public static Optional<Animation> getAnimation(String name, UUID owner){
        Map<UUID, String> animations = getAnimationsByOwner(owner);
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        Animation newAnimation;
        try {
            if (animations.containsKey(owner)){
                File f = new File(ANIMATION_DATA_DIR + "/" + owner.toString() + "." + name);
                fis = new FileInputStream(f); // this needs to be closed TODO
                ois = new ObjectInputStream(fis); // this needs to be closed TODO
                newAnimation = (Animation) ois.readObject();
                fis.close();
                ois.close();
                return Optional.of(newAnimation);
            } else {
                return Optional.empty();
            }
            // There's a lot of exceptions here, possible point of refactor TODO
        } catch (NullPointerException npe){
            // If the fileList is of size zero
            npe.printStackTrace();
            AnimationPlugin.logger.info("NPE: Failed to get the files listed in dir: " + CONFIG_DIR);
        } catch (FileNotFoundException fnfe){
            fnfe.printStackTrace();
            AnimationPlugin.logger.info("FNFE: Failed to find file.");
        } catch (IOException ioe){
            ioe.printStackTrace();
            AnimationPlugin.logger.info("IOE: IOException with ObjectInputStream.");
        } catch (ClassNotFoundException cnfe){
            cnfe.printStackTrace();
            AnimationPlugin.logger.info("CNFE: Class 'Animation.class' was not found. <- This is a pretty bad error to get...");
        }
        return Optional.empty();
    }

    /**
     * Iterates through the list of files in the animation directory and puts the data in a map
     * @return Map containing the UUID of the owner as the key and
     * all the names of the Animations he owns as the value
     */
    private static Map<UUID, String> getAnimations(){
        Map<UUID, String> animationOwnerNamePair = new HashMap<>();
        File[] fileList = new File(ANIMATION_DATA_DIR).listFiles();
        try {
            for (File f :
                    fileList) {
                String[] tokens = f.getName().split(".");
                if (tokens.length == 2){
                    animationOwnerNamePair.put(UUID.fromString(tokens[0]), tokens[1]);
                }
            }
        } catch (NullPointerException npe){
            npe.printStackTrace();
            AnimationPlugin.logger.info("NPE while going through the list of files that represent Animations.");
        }
        return animationOwnerNamePair;
    }

    /**
     * Get all available {@link Animation} that are owned by a given owner
     * @param owner the given owner
     * @return Map containing the UUID (owner) and it their animations
     */
    private static Map<UUID, String> getAnimationsByOwner(UUID owner) {
        Map<UUID, String> animations = getAnimations();
        Map<UUID, String> animationsByOwner = new HashMap<>();
        for (Map.Entry entry :
                animations.entrySet()) {
            if (entry.getKey().equals(owner)) {
                animationsByOwner.put((UUID) entry.getKey(), (String) entry.getValue());
            }
        }
        return animationsByOwner;
    }

    /**
     * Create or overwrite an existing file that will contain the serialized data from the Animation plugin
     * @param animation given {@link Animation}
     * @return Success status: true=success, false=failed
     */
    public static boolean saveAnimation(Animation animation){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File animationFile = new File(ANIMATION_DATA_DIR + "/" + animation.getOwner() + "." + animation.getAnimationName());
            fos = new FileOutputStream(animationFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(animation);
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        } finally {
            try {
                oos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes an animation file
     * @param animation given {@link Animation}
     * @return boolean of whether or not the file was deleted
     */
    public static boolean deleteAnimation(Animation animation){
        File animationFile = new File(ANIMATION_DATA_DIR + "/" + animation.getOwner() + "." + animation.getAnimationName());
        return animationFile.delete();
    }
}
