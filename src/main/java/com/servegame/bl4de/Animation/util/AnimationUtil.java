package com.servegame.bl4de.Animation.util;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.data.SQLManager;
import com.servegame.bl4de.Animation.model.Animation;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.servegame.bl4de.Animation.util.Util.*;

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
        try (Connection connection = SQLManager.getConnection()){
            if (animations.contains(name)){
                PreparedStatement statement = connection.prepareStatement("SELECT name, owner, data FROM `ANIMATION_TABLE` WHERE name = ? AND owner = ?");
                statement.setString(1, name);
                statement.setObject(2, owner);
                ResultSet rs = statement.executeQuery();
                if (AnimationPlugin.instance.isDebug()){
                    System.out.println(rs.toString());
                }
                if (rs.next()){
                    // The data exists
                    newAnimation = Animation.deserialize(rs.getString("data"));
                    return Optional.ofNullable(newAnimation);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            //AnimationPlugin.logger.info("SQLException: Failed to retrieve animations.");
        }
        // The animation we are looking for doesn't appear in the call find all animations by name
        // and or the data didn't exist/couldn't get pulled from the DB
        return Optional.empty();
    }

    /**
     * Iterates through the list of files in the animation directory and puts the data in a map
     * @return Map containing the UUID of the owner as the key and
     * all the names of the Animations he owns
     */
    private static Map<UUID, ArrayList<String>> getAnimations(){
        Map<UUID, ArrayList<String>> animationOwnerNamePair = new ConcurrentHashMap<>();
        try (Connection connection = SQLManager.getConnection()) {
            ResultSet rs = connection.prepareStatement("SELECT * FROM `ANIMATION_TABLE`").executeQuery();
            if (AnimationPlugin.instance.isDebug()){
                System.out.println(rs.toString());
            }
            while (rs.next()){
                String name = rs.getString("name");
                UUID uuid = (UUID) rs.getObject("owner");
                if (animationOwnerNamePair.containsKey(uuid)){
                    ArrayList<String> values = animationOwnerNamePair.get(uuid);
                    values.add(name);
                    animationOwnerNamePair.put(uuid, values);
                } else {
                    ArrayList<String> values = new ArrayList<>();
                    values.add(name);
                    animationOwnerNamePair.put(uuid, values);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            AnimationPlugin.logger.info("SQLException while going through the rows that represent Animations.");
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
        if (AnimationPlugin.instance.isDebug()){
            for (String s :
                    animations.getOrDefault(owner, new ArrayList<>())) {
                System.out.println("animation name: " + s);
            }
        }
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
     * With the given {@link Animation} create an entry in the DB
     * @param animation given {@link Animation}
     * @return Success status: true=success, false=failed
     */
    public static boolean createAnimation(Animation animation){
        try (Connection connection = SQLManager.getConnection()){
            PreparedStatement statement = connection.prepareStatement("INSERT INTO `ANIMATION_TABLE` (name, owner, data) VALUES (?, ?, ?)");
            statement.setString(1, animation.getAnimationName());
            statement.setObject(2, animation.getOwner());
            statement.setString(3, Animation.serialize(animation));
            statement.executeUpdate();

            connection.close();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update the given {@link Animation} in the DB
     * @param animation given {@link Animation}
     * @return Success status: true=success, false=failed
     */
    public static boolean saveAnimation(Animation animation){
        try (Connection connection = SQLManager.getConnection()){
            PreparedStatement statement = connection.prepareStatement("UPDATE `ANIMATION_TABLE` SET data = ? WHERE name = ? AND owner = ?");
            String data = Animation.serialize(animation);
            if (AnimationPlugin.instance.isDebug()){
                System.out.println(data);
            }
            statement.setString(1, data);
            statement.setString(2, animation.getAnimationName());
            statement.setObject(3, animation.getOwner());
            statement.executeUpdate();

            connection.close();
            return true;
        } catch (SQLException e){
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
        try (Connection connection = SQLManager.getConnection()){
            PreparedStatement statement = connection.prepareStatement("DELETE FROM `ANIMATION_TABLE` WHERE name = ? AND owner = ?");
            statement.setString(1, animation.getAnimationName());
            statement.setObject(2, animation.getOwner());
            statement.executeUpdate();

            connection.close();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
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
