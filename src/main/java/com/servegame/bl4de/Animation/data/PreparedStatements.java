package com.servegame.bl4de.Animation.data;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.servegame.bl4de.Animation.data.SQLResources.*;

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
                    "INSERT INTO " + ANIMATION_TABLE + " (" +
                            COLUMN_ANIMATION_NAME + ", " +
                            COLUMN_ANIMATION_OWNER + ", " +
                            COLUMN_ANIMATION_STATUS + ", " +
                            COLUMN_ANIMATION_START_FRAME_INDEX + ", " +
                            COLUMN_ANIMATION_TICK_DELAY + ", " +
                            COLUMN_ANIMATION_CYCLES + ", " +
                            COLUMN_ANIMATION_FRAME_NAMES +") " +
                            " VALUES (?, ?, ?, ?, ?, ?, ?)"
            );
            // Put all the names of the frames into a list
            final List<Frame> frames = animation.getFrames();
            final List<String> frameNames = new ArrayList<>();
            frames.forEach(frame -> frameNames.add(frame.getName()));

            statement.setString(1, animation.getAnimationName());       // Animation name
            statement.setObject(2, animation.getOwner());               // Owner
            statement.setString(3, animation.getStatus().toString());   // Status
            statement.setInt(4, animation.getStartFrameIndex());        // Start frame index
            statement.setInt(5, animation.getTickDelay());              // Tick delay
            statement.setInt(6, animation.getCycles());                 // Cycles
            statement.setArray(7, connection.createArrayOf(             // Array of all frame names
                    "VARCHAR2",
                    frameNames.toArray())
            );
            statement.executeUpdate();

            // Create table for the frames
            final String FRAMES_TABLE = getFrameTableName(animation);
            SQLManager.get(AnimationPlugin.plugin).createFrameTable(FRAMES_TABLE);

            // Add all the frames
            List<Frame> frameList = animation.getFrames();
            for (Frame frame :
                    frameList) {
                if (!createFrame(animation, frame, FRAMES_TABLE)){
                    return false;
                }
            }
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
                    "UPDATE " + ANIMATION_TABLE + " SET " +
                            COLUMN_ANIMATION_STATUS + " = ?," +
                            COLUMN_ANIMATION_START_FRAME_INDEX + " = ?," +
                            COLUMN_ANIMATION_TICK_DELAY + " = ?," +
                            COLUMN_ANIMATION_CYCLES + " = ?," +
                            COLUMN_ANIMATION_FRAME_NAMES + " = ?" +
                            "WHERE " +
                            COLUMN_ANIMATION_NAME + " = ? AND " +
                            COLUMN_ANIMATION_OWNER + " = ?"
            );

            // Put all the names of the frames into a list
            final List<Frame> frames = animation.getFrames();
            final List<String> frameNames = new ArrayList<>();
            frames.forEach(frame -> frameNames.add(frame.getName()));

            // SET Clause
            statement.setString(1, animation.getStatus().toString());   // Status
            statement.setInt(2, animation.getStartFrameIndex());        // Start frame index
            statement.setInt(3, animation.getTickDelay());              // Tick delay
            statement.setInt(4, animation.getCycles());                 // Cycles
            statement.setArray(5,                                       // Array of frame names
                    connection.createArrayOf(
                            "VARCHAR2",
                            frameNames.toArray()
                    )
            );

            // WHERE Clause
            statement.setString(6, animation.getAnimationName());       // Animation name
            statement.setObject(7, animation.getOwner());               // Owner
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
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        // The animation we are looking for doesn't appear in the call find all animations by name
        // and or the data didn't exist/couldn't get pulled from the DB
        return Optional.ofNullable(newAnimation);
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

    private static boolean createFrame(Animation animation, Frame frame, final String FRAMES_TABLE){
        try (Connection connection = SQLManager.getConnection()){
            PreparedStatement insertFrameStatement = connection.prepareStatement(
                    "INSERT INTO " + FRAMES_TABLE + " (" +
                            COLUMN_FRAME_NAME + ", " +
                            COLUMN_FRAME_CREATOR + ", " +
                            COLUMN_FRAME_SUBSPACE_C1 + ", " +
                            COLUMN_FRAME_SUBSPACE_C2 + ", " +
                            COLUMN_FRAME_SUBSPACE_CONTENTS + ") " +
                            "VALUES (?, ?, ?, ?, ?)"
            );

            insertFrameStatement.setString(1, frame.getName());     // Frame name
            insertFrameStatement.setObject(2, frame.getCreator());  // Creator
            Optional<Location<World>> corner1Optional = frame.getCornerOne();
            Optional<Location<World>> corner2Optional = frame.getCornerTwo();
            Location corner1 = null, corner2 = null;

            // Not using ifPresent here because you need to have a separate
            // try-catch for the SQL and IOException (which is ugly)
            if (corner1Optional.isPresent()){
                corner1 = corner1Optional.get();
                insertFrameStatement.setString(3, DataFormats.HOCON.write(corner1.createSnapshot().toContainer()));
            }
            if (corner2Optional.isPresent()){
                corner2 = corner2Optional.get();
                insertFrameStatement.setString(4, DataFormats.HOCON.write(corner2.createSnapshot().toContainer()));
            }
            if (corner1 != null && corner2 != null){
                if (!createContents(animation, frame)){
                    return false;
                }
                int xLen = Math.abs(Math.abs(corner1.getBlockX()) - Math.abs(corner2.getBlockX()));
                int yLen = Math.abs(Math.abs(corner1.getBlockY()) - Math.abs(corner2.getBlockY()));
                int zLen = Math.abs(Math.abs(corner1.getBlockZ()) - Math.abs(corner2.getBlockZ()));

                String[][][] contentPositions = new String[xLen][yLen][zLen];
                // Populate the contentPositions with coordinates in the format of "x|y|z"
                for (int i = 0; i < xLen; i++) {
                    for (int j = 0; j < yLen; j++) {
                        for (int k = 0; k < zLen; k++) {
                            contentPositions[i][j][k] = i + "|" + j + "|" + k;
                        }
                    }
                }
                insertFrameStatement.setArray(5, connection.createArrayOf("VARCHAR2", contentPositions));
            }
            insertFrameStatement.executeUpdate();

        } catch (SQLException|IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean saveFrame(Animation animation, Frame frame){
        return false;
    }

    public static Optional<Frame> getFrame(Animation animation, String frameName){
        return null;
    }

    public static Optional<Frame> getFrame(String animationName, String frameName){
        return null;
    }

    /* END: Frame Operations */

    /* START: Content Operations */
    
    private static boolean createContents(Animation animation, Frame frame){
        Optional<Location<World>> corner1Optional = frame.getCornerOne();
        Optional<Location<World>> corner2Optional = frame.getCornerTwo();
        Location corner1, corner2;
        
        if (!corner1Optional.isPresent() || !corner2Optional.isPresent()){
            return false;
        }
        corner1 = corner1Optional.get();
        corner2 = corner2Optional.get();
        
        // Create 3D array for the subspace_contents
        int xLen = Math.abs(Math.abs(corner1.getBlockX()) - Math.abs(corner2.getBlockX()));
        int yLen = Math.abs(Math.abs(corner1.getBlockY()) - Math.abs(corner2.getBlockY()));
        int zLen = Math.abs(Math.abs(corner1.getBlockZ()) - Math.abs(corner2.getBlockZ()));
        Optional<BlockSnapshot[][][]> blockSnapshotsOptional = frame.getContents();
        if (!blockSnapshotsOptional.isPresent()){
            return false;
        }
        BlockSnapshot[][][] contents = blockSnapshotsOptional.get();
        
        try (Connection connection = SQLManager.getConnection()){
            Map<String, String> contentPositions = new HashMap<>();
            
            // Populate the contentPositions with coordinates in the format of "x|y|z"
            for (int i = 0; i < xLen; i++) {
                for (int j = 0; j < yLen; j++) {
                    for (int k = 0; k < zLen; k++) {
                        contentPositions.put(i + "|" + j + "|" + k, DataFormats.HOCON.write(contents[i][j][k].toContainer()));
                    }
                }
            }
            
            // Create contents table
            final String CONTENT_TABLE = SQLResources.getContentTableName(animation, frame);
            SQLManager.get(AnimationPlugin.plugin).createContentsTable(CONTENT_TABLE);
            
            // Populate the table
            for (Map.Entry<String, String> entry :
                    contentPositions.entrySet()) {
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO " + CONTENT_TABLE + " SET " +
                                COLUMN_CONTENTS_XYZ + " = ?, " +
                                COLUMN_CONTENTS_DATA + " = ?"
                );
                statement.setString(1, entry.getKey());
                statement.setString(2, entry.getValue());
                statement.execute();
            }
            connection.commit();
        } catch (SQLException|IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static boolean saveContents(Animation animation, Frame frame){
        return false;
    }

    private static BlockSnapshot[][][] getContents(Animation animation, Frame frame){
        return null;
    }

    
    /* END: Content Operations */
}
