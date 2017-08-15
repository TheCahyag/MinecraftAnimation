package com.servegame.bl4de.Animation.util;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.model.Animation;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataTranslator;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.world.Location;

import static com.servegame.bl4de.Animation.util.Util.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

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
        ArrayList<String> animations = getAnimationsByOwner(owner);
        Animation newAnimation = null;
        try {
            if (animations.contains(name)){
                File f = new File(ANIMATION_DATA_DIR + "/" + owner.toString() + "." + name);
                InputStream in = Files.newInputStream(f.toPath()); // TODO Refactor
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line, toDeserialize = null;
                while ((line = bufferedReader.readLine()) != null){
                    toDeserialize = toDeserialize + line;
                }
                newAnimation = Animation.deserialize(toDeserialize);
                return Optional.ofNullable(newAnimation);
            } else {
                return Optional.empty();
            }
            // There's a lot of exception here, possible point of refactor TODO
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
        }
        return Optional.empty();
    }

    /**
     * Iterates through the list of files in the animation directory and puts the data in a map
     * @return Map containing the UUID of the owner as the key and
     * all the names of the Animations he owns
     */
    private static Map<UUID, ArrayList<String>> getAnimations(){
        Map<UUID, ArrayList<String>> animationOwnerNamePair = new HashMap<>();
        File[] fileList = new File(ANIMATION_DATA_DIR).listFiles();
        try {
            for (File f :
                    fileList) {
                String[] tokens = f.getName().split("\\.");
                if (tokens.length == 2){
                    UUID owner = UUID.fromString(tokens[0]);
                    if (animationOwnerNamePair.containsKey(owner)){
                        ArrayList<String> tmp = animationOwnerNamePair.get(owner);
                        tmp.add(tokens[1]);
                        animationOwnerNamePair.put(owner, tmp);
                    } else {
                        ArrayList<String> tmp = new ArrayList<>();
                        tmp.add(tokens[1]);
                        animationOwnerNamePair.put(owner, tmp);
                    }
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
     * @return ArrayList of strings containing the animation names
     */
    public static ArrayList<String> getAnimationsByOwner(UUID owner) {
        Map<UUID, ArrayList<String>> animations = getAnimations();
        return animations.getOrDefault(owner, new ArrayList<>());
    }

    /**
     * Stops all animations, usually ran when the server is shutting down.
     */
    public static void stopAllAnimations(){
        Map<UUID, ArrayList<String>> animations = getAnimations();
        for (Map.Entry<UUID, ArrayList<String>> entry :
                animations.entrySet()) {
            ArrayList<String> animationNames = entry.getValue();
            UUID uuid = entry.getKey();
            for (int i = 0; i < animationNames.size(); i++) {
                getAnimation(animationNames.get(i), uuid).get().stop();
            }
        }
    }

    /**
     * Create or overwrite an existing file that will contain the serialized data from the Animation plugin
     * @param animation given {@link Animation}
     * @return Success status: true=success, false=failed
     */
    public static boolean saveAnimation(Animation animation){
        try {
            File animationFile = new File(ANIMATION_DATA_DIR + "/" + animation.getOwner() + "." + animation.getAnimationName());
            String serializedAnimation = Animation.serialize(animation);
            PrintWriter printWriter = new PrintWriter(animationFile);
            printWriter.write(serializedAnimation);
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
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

    /**
     * TODO
     * @param animation
     * @return
     */
    public static Text linkToAnimationInfo(Animation animation){
        return Text.builder()
                .append(Text.of(NAME_COLOR, COMMAND_STYLE, animation.getAnimationName()))
                .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " info"))
                .build();
    }

    /**
     * TODO
     * @param animation
     * @return
     */
    public static Text getButtonsForAnimation(Animation animation){
        return Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "DELETE",
                                PRIMARY_COLOR, "] "))
                        .onClick(TextActions.runCommand("/animate delete " + animation.getAnimationName()))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "CREATE FRAME",
                                PRIMARY_COLOR, "] "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame create"))
                        .build())
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "LIST FRAMES",
                                PRIMARY_COLOR, "] | "))
                        .onClick(TextActions.runCommand("/animate " + animation.getAnimationName() + " frame list"))
                        .build())
                .build();
    }

    /**
     * TODO
     * @return
     */
    public static Text getButtonsForList(){
        return Text.builder()
                .append(Text.builder()
                        .append(Text.of(PRIMARY_COLOR, "[",
                                ACTION_COLOR, COMMAND_HOVER, "CREATE",
                                PRIMARY_COLOR, "]"))
                        .onClick(TextActions.suggestCommand("/animate create <name>"))
                        .build())
                .build();
    }
}
