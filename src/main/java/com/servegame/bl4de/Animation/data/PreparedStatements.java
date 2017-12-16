package com.servegame.bl4de.Animation.data;

import com.servegame.bl4de.Animation.model.Animation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.servegame.bl4de.Animation.data.SQLResources.ANIMATION_TABLE;

/**
 * File: PreparedStatements.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class PreparedStatements {

    /* START: Animation Operations */

    /**
     * With the given {@link Animation} create an entry in the DB
     * @param animation given {@link Animation}
     * @return Success status: true=success, false=failed
     */
    public static boolean createAnimation(Animation animation){
        try (Connection connection = SQLManager.getConnection()){
            // Insert new animation into the table
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO " + ANIMATION_TABLE + " (name, owner, data) VALUES (?, ?, ?)"
            );
            statement.setString(1, animation.getAnimationName());
            statement.setObject(2, animation.getOwner());
            statement.setString(3, Animation.serialize(animation));
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Update the given {@link Animation} in the DB
     * @param animation given {@link Animation}
     * @return Success status: true=success, false=failed
     */
    public static boolean saveAnimation(Animation animation){
        try (Connection connection = SQLManager.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE " + ANIMATION_TABLE + " SET data = ? WHERE name = ? AND owner = ?"
            );
            statement.setString(1, Animation.serialize(animation));
            statement.setString(2, animation.getAnimationName());
            statement.setObject(3, animation.getOwner());
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

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
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT name, owner, data FROM " + ANIMATION_TABLE + " WHERE name = ? AND owner = ?"
                );
                statement.setString(1, name);
                statement.setObject(2, owner);
                ResultSet rs = statement.executeQuery();
                if (rs.next()){
                    // The data exists
                    String animationString = rs.getString("data");
                    newAnimation = Animation.deserialize(animationString);
                    return Optional.ofNullable(newAnimation);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
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
    public static Map<UUID, ArrayList<String>> getAnimations(){
        Map<UUID, ArrayList<String>> animationOwnerNamePair = new ConcurrentHashMap<>();
        try (Connection connection = SQLManager.getConnection()) {
            ResultSet rs = connection.prepareStatement("SELECT name, owner FROM " + ANIMATION_TABLE).executeQuery();
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
     * Deletes an animation file
     * @param animation given {@link Animation}
     * @return boolean of whether or not the file was deleted
     */
    public static boolean deleteAnimation(Animation animation){
        try (Connection connection = SQLManager.getConnection()){
            // Delete row in animation table
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM " + ANIMATION_TABLE + " WHERE name = ? AND owner = ?"
            );
            statement.setString(1, animation.getAnimationName());
            statement.setObject(2, animation.getOwner());
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /* END: Animation Operations */

    /* START: Frame Operations */
    /* END: Frame Operations */

    /* START: SubSpace3D Operations */
    /* END: SubSpace3D Operations */
}
