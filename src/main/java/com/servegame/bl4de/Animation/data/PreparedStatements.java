package com.servegame.bl4de.Animation.data;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.exception.UninitializedException;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.model.SubSpace3D;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.IOException;
import java.sql.*;
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
                            COLUMN_ANIMATION_FRAME_NAMES + ", " +
                            COLUMN_ANIMATION_C1 + ", " +
                            COLUMN_ANIMATION_C2 + ") " +
                            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
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
            statement.setObject(7, frameNames.toArray());
            Optional<Location<World>> locationOptional1 = animation.getSubSpace().getCornerOne();
            Optional<Location<World>> locationOptional2 = animation.getSubSpace().getCornerTwo();
            if (locationOptional1.isPresent()){
                statement.setString(8, DataFormats.HOCON.write(locationOptional1.get().createSnapshot().toContainer()));
            } else {
                statement.setString(8, null);
            }
            if (locationOptional2.isPresent()){
                statement.setString(9, DataFormats.HOCON.write(locationOptional2.get().createSnapshot().toContainer()));
            } else {
                statement.setString(9, null);
            }
            statement.executeUpdate();

            // Add all the frames
            List<Frame> frameList = animation.getFrames();
            for (Frame frame :
                    frameList) {
                if (!createFrame(animation, frame)){
                    return false;
                }
            }
        } catch (SQLException|IOException e){
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
                            COLUMN_ANIMATION_STATUS + " = ?, " +
                            COLUMN_ANIMATION_START_FRAME_INDEX + " = ?, " +
                            COLUMN_ANIMATION_TICK_DELAY + " = ?, " +
                            COLUMN_ANIMATION_CYCLES + " = ?, " +
                            COLUMN_ANIMATION_FRAME_NAMES + " = ?, " +
                            COLUMN_ANIMATION_C1 + " = ?, " +
                            COLUMN_ANIMATION_C2 + " = ? " +
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
            statement.setObject(5, frameNames.toArray());
            Optional<Location<World>> worldLocation1 = animation.getSubSpace().getCornerOne();
            Optional<Location<World>> worldLocation2 = animation.getSubSpace().getCornerTwo();
            if (worldLocation1.isPresent()){
                statement.setString(6, DataFormats.HOCON.write(worldLocation1.get().createSnapshot().toContainer()));
            } else {
                statement.setString(6, null);
            }
            if (worldLocation2.isPresent()){
                statement.setString(7, DataFormats.HOCON.write(worldLocation2.get().createSnapshot().toContainer()));
            } else {
                statement.setString(7, null);

            }

            // WHERE Clause
            statement.setString(8, animation.getAnimationName());       // Animation name
            statement.setObject(9, animation.getOwner());               // Owner
            statement.executeUpdate();

            for (Frame frame :
                    animation.getFrames()) {
                saveFrame(animation, frame);
            }
        } catch (SQLException|IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean saveBareAnimation(Animation animation){
        try (Connection connection = SQLManager.getConnection()){
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE " + ANIMATION_TABLE + " SET " +
                            COLUMN_ANIMATION_STATUS + " = ?, " +
                            COLUMN_ANIMATION_START_FRAME_INDEX + " = ?, " +
                            COLUMN_ANIMATION_TICK_DELAY + " = ?, " +
                            COLUMN_ANIMATION_CYCLES + " = ?, " +
                            //COLUMN_ANIMATION_FRAME_NAMES + " = ?, " +
                            COLUMN_ANIMATION_C1 + " = ?, " +
                            COLUMN_ANIMATION_C2 + " = ? " +
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
//            statement.setObject(5, frameNames.toArray());
            Optional<Location<World>> worldLocation1 = animation.getSubSpace().getCornerOne();
            Optional<Location<World>> worldLocation2 = animation.getSubSpace().getCornerTwo();
            if (worldLocation1.isPresent()){
                statement.setString(5, DataFormats.HOCON.write(worldLocation1.get().createSnapshot().toContainer()));
            } else {
                statement.setString(5, null);
            }
            if (worldLocation2.isPresent()){
                statement.setString(6, DataFormats.HOCON.write(worldLocation2.get().createSnapshot().toContainer()));
            } else {
                statement.setString(6, null);

            }

            // WHERE Clause
            statement.setString(7, animation.getAnimationName());       // Animation name
            statement.setObject(8, animation.getOwner());               // Owner
            statement.executeUpdate();

        } catch (SQLException|IOException e){
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
        Optional<Animation> bareAnimationOptional = getBareAnimation(name, owner);
        Animation animation = null;
        if (!bareAnimationOptional.isPresent()){
            return Optional.empty();
        }
        try (Connection connection = SQLManager.getConnection()){
            animation = bareAnimationOptional.get();
            // Reset frames list (since blank ones were added in getBareAnimation())
            animation.setFrames(new ArrayList<>());

            // Get all frames
            final String FRAME_TABLE = getFrameTableName(animation);
            PreparedStatement getFrameNames = connection.prepareStatement(
                    "SELECT " + COLUMN_ANIMATION_FRAME_NAMES +
                            " FROM " + ANIMATION_TABLE +
                            " WHERE " + COLUMN_ANIMATION_NAME + " = ? AND " +
                            COLUMN_ANIMATION_OWNER + " = ?");
            getFrameNames.setString(1, animation.getAnimationName());
            getFrameNames.setObject(2, animation.getOwner());
            ResultSet rs = getFrameNames.executeQuery();
            if (rs.next()){
                Object[] array = ((Object[]) rs.getArray(COLUMN_ANIMATION_FRAME_NAMES).getArray());
                String[] frames = new String[array.length];
                for (int i = 0; i < array.length; i++) {
                    frames[i] = (String) array[i];
                }
                for (String frameName :
                        frames) {
                    Optional<Frame> frameOptional = getFrame(frameName, FRAME_TABLE,
                            getContentTableName(animation, new Frame(animation.getOwner(), frameName, new SubSpace3D())), true);
                    if (frameOptional.isPresent()) {
                        animation.addFrame(frameOptional.get());
                    }
                }

            }
        } catch (SQLException e){
            e.printStackTrace();
        } catch (UninitializedException e){
            System.err.println("Frame/Animation is not initialized. (This is likely a bug)");
        }
        // The animation we are looking for doesn't appear in the call find all animations by name
        // and or the data didn't exist/couldn't get pulled from the DB
        return Optional.ofNullable(animation);
    }

    /**
     * Get an animation with no frames or any of the data associated with the frames.
     * Do note that it does have a SubSpace inorder to know the corners of the animation.
     * @param name name of the {@link Animation}
     * @param owner {@link UUID} of the owner of the animation
     * @return - Optional of the animation
     */
    public static Optional<Animation> getBareAnimation(String name, UUID owner){
        Animation animation = null;
        try (Connection connection = SQLManager.getConnection()){
            PreparedStatement getAnimation = connection.prepareStatement(
                    "SELECT * FROM " + ANIMATION_TABLE +
                            " WHERE " + COLUMN_ANIMATION_NAME + " = ? AND " +
                            COLUMN_ANIMATION_OWNER + " = ?");
            getAnimation.setString(1, name);
            getAnimation.setObject(2, owner);
            ResultSet rs = getAnimation.executeQuery();
            String[] frameNames = {};

            if (rs.next()){
                // Get animation data (except subspace data)
                animation = new Animation(owner, name);
                animation.setStatus(Animation.Status.valueOf(rs.getString(COLUMN_ANIMATION_STATUS)));
                animation.setCycles(rs.getInt(COLUMN_ANIMATION_CYCLES));
                animation.setTickDelay(rs.getInt(COLUMN_ANIMATION_TICK_DELAY));
                animation.setStartFrameIndex(rs.getInt(COLUMN_ANIMATION_START_FRAME_INDEX));

                Object[] array = ((Object[]) rs.getArray(COLUMN_ANIMATION_FRAME_NAMES).getArray());
                frameNames = new String[array.length];

                for (int i = 0; i < array.length; i++) {
                    frameNames[i] = (String) array[i];
                }

                // Create SubSpace3D
                SubSpace3D subSpace3D = new SubSpace3D();
                Optional<BlockSnapshot> c1 = Optional.empty();
                Optional<BlockSnapshot> c2 = Optional.empty();
                String c1String = rs.getString(COLUMN_ANIMATION_C1);
                String c2String = rs.getString(COLUMN_ANIMATION_C2);

                if (c1String != null){
                    // Corner one is in the DB
                    c1 = BlockSnapshot.builder().build(DataFormats.HOCON.read(c1String));
                }
                if (c2String != null){
                    // Corner two is in the DB
                    c2 = BlockSnapshot.builder().build(DataFormats.HOCON.read(c2String));
                }
                c1.ifPresent(blockSnapshot -> subSpace3D.setCornerOne(blockSnapshot.getLocation().orElse(null)));
                c2.ifPresent(blockSnapshot -> subSpace3D.setCornerTwo(blockSnapshot.getLocation().orElse(null)));
                animation.setSubSpace(subSpace3D);
            }

            for (String frameName :
                    frameNames) {
                animation.addFrame(getFrame(frameName, SQLResources.getFrameTableName(animation), null, false).get());
            }
        } catch (SQLException|IOException e){
            e.printStackTrace();
        } catch (UninitializedException e){
            System.err.println("An animation was uninitialized but frames were added anyway. (This is likely a bug)");
        }
        return Optional.ofNullable(animation);
    }

    /**
     * Iterates through the file in the animation directory and puts the data in a map
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

    public static ArrayList<String> getAnimationsByOwner(UUID owner) {
        Map<UUID, ArrayList<String>> animations = getAnimations();
        return animations.getOrDefault(owner, new ArrayList<>());
    }

    public static boolean deleteAnimation(Animation animation){
        try (Connection connection = SQLManager.getConnection()){
            // Get all the frames in for the given animation
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT " + COLUMN_ANIMATION_FRAME_NAMES +
                            " FROM " + ANIMATION_TABLE + " WHERE " +
                            COLUMN_ANIMATION_NAME + " = ? AND " +
                            COLUMN_ANIMATION_OWNER + " = ?"
            );
            statement.setString(1, animation.getAnimationName());
            statement.setObject(2, animation.getOwner());
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                Object[] array = ((Object[]) rs.getArray(COLUMN_ANIMATION_FRAME_NAMES).getArray());
                String[] frameNames = new String[array.length];
                for (int i = 0; i < array.length; i++) {
                    frameNames[i] = (String) array[i];
                }
                for (String frame :
                        frameNames) {
                    // For every frame in the animation delete the content table associated with the frame
                    Frame frameWithOnlyName = new Frame(animation.getOwner(), frame, new SubSpace3D());
                    SQLManager.get(AnimationPlugin.plugin).deleteTable(getContentTableName(animation, frameWithOnlyName));
                }

            }
            // Delete the frame table associated with the animation
            SQLManager.get(AnimationPlugin.plugin).deleteTable(getFrameTableName(animation));

            // Delete the row containing the animation in the animation table
            PreparedStatement deleteAnimationRow = connection.prepareStatement(
                    "DELETE FROM " + ANIMATION_TABLE +
                            " WHERE " + COLUMN_ANIMATION_NAME + " = ? AND " +
                            COLUMN_ANIMATION_OWNER + " = ?");
            deleteAnimationRow.setString(1, animation.getAnimationName());
            deleteAnimationRow.setObject(2, animation.getOwner());
            deleteAnimationRow.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean refreshAnimation(String animationName, UUID owner){
        try (Connection conn = SQLManager.getConnection()){
            PreparedStatement getAnimationFrameNames = conn.prepareStatement(
                    "SELECT " + COLUMN_ANIMATION_FRAME_NAMES +
                            " FROM " + ANIMATION_TABLE);
            ResultSet rs = getAnimationFrameNames.executeQuery();
            String[] frameNames;
            if (rs.next()){
                Object[] array = ((Object[]) rs.getArray(COLUMN_ANIMATION_FRAME_NAMES).getArray());
                frameNames = new String[array.length];
                for (int i = 0; i < array.length; i++) {
                    frameNames[i] = (String) array[i];
                }
            } else {
                // Couldn't get the frames from the animation
                return false;
            }

            for (String frameName :
                    frameNames) {
                // For each frame check if there exists a table for its content, if so
                // then set subspace_contents to true in the animation frames table
                Frame bareFrame = new Frame();
                bareFrame.setName(frameName);
                bareFrame.setCreator(owner);
                String frameContentTableName = getContentTableName(new Animation(owner, animationName), bareFrame);
                frameContentTableName = frameContentTableName.substring(1, frameContentTableName.length() - 1).toUpperCase();
                String contentValue;
                if (SQLManager.doesTableExist(frameContentTableName)){
                    contentValue = "()";
                } else {
                    contentValue = null;
                }
                // The content table exists
                String framesTable = getFrameTableName(animationName, owner);
                PreparedStatement updateSubspaceContent = conn.prepareStatement(
                        "UPDATE " + framesTable +
                                " SET " + COLUMN_FRAME_SUBSPACE_CONTENTS + " = ? " +
                                "WHERE " + COLUMN_FRAME_NAME + " = '" + frameName + "' " +
                                " AND " + COLUMN_FRAME_CREATOR + " = ?");
                updateSubspaceContent.setString(1, contentValue); // empty array for now should be changed to true todo
                updateSubspaceContent.setObject(2, owner);
                updateSubspaceContent.executeUpdate();
            }
        } catch (SQLException e){
            AnimationPlugin.logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * To rename an animation there are several places in the database that need to be changed
     * 1. Update the animation's row in the animations table
     * 2. Update the animation's frame table's name
     * 3. For each frame the animation has, the table representing the frame needs to be renamed
     * @param animation - animation to rename
     * @param newName - the new name for the animation
     * @return true if all went well, otherwise false
     */
    public static boolean renameAnimation(Animation animation, String newName){
        String originalName = animation.getAnimationName();
        try (Connection conn = SQLManager.getConnection()){
            // Rename the animation in the animations table
            PreparedStatement updateRow = conn.prepareStatement(
                    "UPDATE " + ANIMATION_TABLE +
                            " SET " + COLUMN_ANIMATION_NAME + " = ? " +
                            " WHERE " + COLUMN_ANIMATION_OWNER + " = ? " +
                            "AND " + COLUMN_ANIMATION_NAME + " = ?");
            updateRow.setString(1, newName);
            updateRow.setObject(2, animation.getOwner());
            updateRow.setString(3, originalName);
            updateRow.executeUpdate();

            // Rename the frame list table
            SQLManager.get(AnimationPlugin.plugin).renameTable(
                    getFrameTableName(animation),
                    getFrameTableName(new Animation(animation.getOwner(), newName))
            );

            List<Frame> frames = animation.getFrames();

            for (Frame frame :
                    frames) {
                // For each frame, rename the content table in the database
                SQLManager.get(AnimationPlugin.plugin).renameTable(
                        getContentTableName(animation, frame),
                        getContentTableName(new Animation(null, newName), frame)
                );
            }

        } catch (SQLException e){
            AnimationPlugin.logger.error("Failed to rename animation: " + animation.getAnimationName());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /* END: Animation Operations */

    /* START: Frame Operations */

    public static boolean createFrame(Animation animation, Frame frame){
        final String FRAMES_TABLE = getFrameTableName(animation);
        try (Connection connection = SQLManager.getConnection()){
            // Create table for the frames
            SQLManager.get(AnimationPlugin.plugin).createFrameTable(FRAMES_TABLE);

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
            if (frame.getContents().isPresent()){
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
                insertFrameStatement.setObject(5, contentPositions);
            } else {
                insertFrameStatement.setObject(5, null);
            }
            insertFrameStatement.executeUpdate();

        } catch (SQLException|IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean saveFrame(Animation animation, Frame frame){
        final String FRAME_TABLE = getFrameTableName(animation);
        final String CONTENT_TABLE = getContentTableName(animation, frame);
        SQLManager.get(AnimationPlugin.plugin).createFrameTable(FRAME_TABLE);

        try (Connection connection = SQLManager.getConnection()){
            // Check to make sure the frame has been created
            PreparedStatement beenCreated = connection.prepareStatement(
                    "SELECT * FROM " + FRAME_TABLE +
                            " WHERE " + COLUMN_FRAME_NAME + " = ? AND " +
                            COLUMN_FRAME_CREATOR + " = ?");
            beenCreated.setString(1, frame.getName());
            beenCreated.setObject(2, frame.getCreator());
            ResultSet resultSet = beenCreated.executeQuery();
            if (!resultSet.next()){
                return createFrame(animation, frame);
            }

            // Update frame row
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE " + FRAME_TABLE + " SET " +
                            COLUMN_FRAME_SUBSPACE_C1 + " = ?, " +
                            COLUMN_FRAME_SUBSPACE_C2 + " = ?, " +
                            COLUMN_FRAME_SUBSPACE_CONTENTS + " = ?" +
                            " WHERE " +
                            COLUMN_FRAME_NAME + " = ? AND " +
                            COLUMN_FRAME_CREATOR + " = ?");
            // SET Clause
            Optional<Location<World>> corner1Optional = frame.getCornerOne();
            Optional<Location<World>> corner2Optional = frame.getCornerTwo();
            Location corner1 = null, corner2 = null;

            // Not using ifPresent here because you need to have a separate
            // try-catch for the SQL and IOException (which is ugly)
            if (corner1Optional.isPresent()){
                corner1 = corner1Optional.get();
                statement.setString(1, DataFormats.HOCON.write(corner1.createSnapshot().toContainer()));
            }
            if (corner2Optional.isPresent()){
                corner2 = corner2Optional.get();
                statement.setString(2, DataFormats.HOCON.write(corner2.createSnapshot().toContainer()));
            }
            if (frame.getContents().isPresent()){
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
                statement.setObject(3, contentPositions);
                saveContents(CONTENT_TABLE, frame.getContents().get());
            } else {
                statement.setObject(3, null);
            }

            // WHERE Clause
            statement.setString(4, frame.getName());
            statement.setObject(5, frame.getCreator());
            statement.executeUpdate();
        } catch (SQLException|IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean updateAnimationStatus(Animation animation, Animation.Status status){
        try (Connection connection = SQLManager.getConnection()){
            PreparedStatement updateStatus = connection.prepareStatement(
                    "UPDATE " + ANIMATION_TABLE +
                            " SET " + COLUMN_ANIMATION_STATUS + " = ? WHERE " +
                            COLUMN_ANIMATION_NAME + " = ? AND " + COLUMN_ANIMATION_OWNER + " = ?");
            updateStatus.setString(1, status.toString());
            updateStatus.setString(2, animation.getAnimationName());
            updateStatus.setObject(3, animation.getOwner());
            updateStatus.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static Optional<Frame> getFrame(String frameName, final String FRAME_TABLE, final String CONTENTS_TABLE, boolean loadContents){
        Frame frame = null;
        try (Connection connection = SQLManager.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM " + FRAME_TABLE +
                            " WHERE " + COLUMN_FRAME_NAME + " = ?");
            statement.setString(1, frameName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){

                // Create SubSpace3D
                SubSpace3D subSpace3D = new SubSpace3D();
                Optional<BlockSnapshot> c1 = BlockSnapshot.builder().build(DataFormats.HOCON.read(rs.getString(COLUMN_FRAME_SUBSPACE_C1)));
                Optional<BlockSnapshot> c2 = BlockSnapshot.builder().build(DataFormats.HOCON.read(rs.getString(COLUMN_FRAME_SUBSPACE_C2)));
                c1.ifPresent(blockSnapshot -> subSpace3D.setCornerOne(blockSnapshot.getLocation().orElse(null)));
                c2.ifPresent(blockSnapshot -> subSpace3D.setCornerTwo(blockSnapshot.getLocation().orElse(null)));

                if (rs.getObject(COLUMN_FRAME_SUBSPACE_CONTENTS) != null && loadContents){
                    // Create and set contents of the SubSpace if loadContents is true
                    Location<World> corner1 = c1.get().getLocation().get();
                    Location<World> corner2 = c2.get().getLocation().get();

                    int xLen = Math.abs(Math.abs(corner1.getBlockX()) - Math.abs(corner2.getBlockX())) + 1;
                    int yLen = Math.abs(Math.abs(corner1.getBlockY()) - Math.abs(corner2.getBlockY())) + 1;
                    int zLen = Math.abs(Math.abs(corner1.getBlockZ()) - Math.abs(corner2.getBlockZ())) + 1;

                    BlockSnapshot[][][] blockSnapshots = new BlockSnapshot[xLen][yLen][zLen];
                    getContents(CONTENTS_TABLE, blockSnapshots);
                    subSpace3D.setContents(blockSnapshots);
                }

                // Create Frame
                String name = rs.getString(COLUMN_FRAME_NAME);
                UUID creator = (UUID) rs.getObject(COLUMN_FRAME_CREATOR);

                frame = new Frame(creator, name, subSpace3D);
            }


        } catch (IOException|SQLException e){
            e.printStackTrace();
        }
        return Optional.ofNullable(frame);
    }

    public static boolean deleteFrame(Animation animation, Frame frame){
        final String FRAME_TABLE = getFrameTableName(animation);
        final String CONTENT_TABLE = getContentTableName(animation, frame);
        try (Connection connection = SQLManager.getConnection()){
            deleteContents(CONTENT_TABLE);
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM " + FRAME_TABLE +
                            " WHERE " + COLUMN_FRAME_NAME + " = ? " +
                            "AND " + COLUMN_FRAME_CREATOR + " = ?");
            statement.setString(1, frame.getName());
            statement.setObject(2, frame.getCreator());
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
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
            return true;
        }
        BlockSnapshot[][][] contents = blockSnapshotsOptional.get();
        
        try (Connection connection = SQLManager.getConnection()){
            // The key is the coordinate in the format of "x|y|z" and the value
            // is the HOCON translation of the BlockSnapShot's DataView
            Map<String, String> contentPositions = new HashMap<>();
            
            // Populate the contentPositions with coordinates in the format of "x|y|z"
            for (int i = 0; i <= xLen; i++) {
                for (int j = 0; j <= yLen; j++) {
                    for (int k = 0; k <= zLen; k++) {
                        if (contents[i][j][k] == null){
                            continue;
                        }
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

    private static boolean saveContents(final String CONTENT_TABLE, BlockSnapshot[][][] blockSnapshots){
        int xLength = blockSnapshots.length;
        int yLength = blockSnapshots[0].length;
        int zLength = blockSnapshots[0][0].length;

        SQLManager.get(AnimationPlugin.plugin).createContentsTable(CONTENT_TABLE);

        try (Connection connection = SQLManager.getConnection()){
            // Delete all rows from the table
            PreparedStatement statement = connection.prepareStatement("TRUNCATE TABLE " + CONTENT_TABLE);
            statement.execute();
            for (int i = 0; i < xLength; i++) {
                for (int j = 0; j < yLength; j++) {
                    for (int k = 0; k < zLength; k++) {
                        // Re add each block as a row
                        if (blockSnapshots[i][j][k] == null){
                            // If the block is air, don't put it in the database
                            continue;
                        }
                        PreparedStatement statement1 = connection.prepareStatement(
                                "INSERT INTO " + CONTENT_TABLE +
                                        " SET " +
                                        COLUMN_CONTENTS_XYZ + " = ?, " +
                                        COLUMN_CONTENTS_DATA + " = ?"
                        );
                        statement1.setString(1, i + "|" + j + "|" + k);
                        statement1.setString(2, DataFormats.HOCON.write(blockSnapshots[i][j][k].toContainer()));
                        statement1.execute();
                    }
                }
            }
            connection.commit();
        } catch (SQLException|IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void getContents(final String CONTENT_TABLE, BlockSnapshot[][][] blockSnapshotsReference){
        int xLength = blockSnapshotsReference.length;
        int yLength = blockSnapshotsReference[0].length;
        int zLength = blockSnapshotsReference[0][0].length;

        try (Connection connection = SQLManager.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(
                    "SELECT *" + " FROM " + CONTENT_TABLE);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                String posData = rs.getString(COLUMN_CONTENTS_XYZ);
                String data = rs.getString(COLUMN_CONTENTS_DATA);
                String[] xyzData = posData.split("\\|");
                int x =  Integer.parseInt(xyzData[0]);
                int y =  Integer.parseInt(xyzData[1]);
                int z =  Integer.parseInt(xyzData[2]);
                blockSnapshotsReference[x][y][z] = BlockSnapshot.builder().build(DataFormats.HOCON.read(data)).get();
            }
            // Iterate through the list and check for nulls (if null insert minecraft air) todo
//            for (int i = 0; i < xLength; i++) {
//                for (int j = 0; j < yLength; j++) {
//                    for (int k = 0; k < zLength; k++) {
//                    }
//                }
//            }
        } catch (SQLException|IOException e){
            e.printStackTrace();
        }
    }

    private static void deleteContents(final String CONTENT_TABLE){
        SQLManager.get(AnimationPlugin.plugin).deleteTable(CONTENT_TABLE);
    }

    /* END: Content Operations */
}
