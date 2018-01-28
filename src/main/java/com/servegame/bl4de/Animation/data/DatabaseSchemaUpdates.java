package com.servegame.bl4de.Animation.data;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import com.servegame.bl4de.Animation.model.SubSpace3D;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import static com.servegame.bl4de.Animation.data.SQLResources.*;

/**
 * File: DatabaseSchemaUpdates.java
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class DatabaseSchemaUpdates {

    private static final String TABLE_VERSION_ONE = "ANIMATION_TABLE";
    private static final String TABLE_VERSION_TWO = "animations".toUpperCase();


    /**
     * Version one of the database was a single table named "animation_table", this schema
     * will be converted to the newer version of the database
     * @return boolean - whether the old table was found or not
     */
    public static boolean checkForVersionOne(){
        try (Connection connection = SQLManager.getConnection()){
            ResultSet rs = connection.getMetaData()
                    .getTables(null, null, TABLE_VERSION_ONE, null);
            if (rs.next()){
                return true;
            }
            connection.close();
        } catch (Exception e){
            return false;
        }
        return false;
    }

    /**
     * Version two of the database has a main table called "animations" that holds information about each
     * {@link Animation} and each animation has an additional table holding information about its {@link SubSpace3D}
     * and for every {@link Frame} there is a table holding information about the frame contents. The change from
     * version two to three are:
     *  - The subspace contents row in the frames table is changed from an array to a boolean
     *    value (whether or not the frame has content)
     * @return true or false, depending on whether or not the column is of type array (the v2 type)
     */
    public static boolean checkForVersionTwo(){
        try (Connection conn = SQLManager.getConnection()){
            PreparedStatement checkForMainTable = conn.prepareStatement("SELECT * FROM " + TABLE_VERSION_TWO);
            ResultSet rs = checkForMainTable.executeQuery();
            String animationName;
            UUID owner;
            if (rs.next()){
                animationName = rs.getString(COLUMN_ANIMATION_NAME);
                owner = (UUID) rs.getObject(COLUMN_ANIMATION_OWNER);
            } else {
                return false;
            }
            String tableName = getFrameTableName(animationName, owner);

            // Check the frame table of the first animation to see if the subspace contents
            // column is an array or a boolean value
            PreparedStatement statement = conn.prepareStatement(
                    "select type_name " +
                            "from information_schema.columns " +
                            "where table_name= '" + tableName + "'" +
                            "and column_name='SUBSPACE_CONTENTS'");
            rs = statement.executeQuery();
            if (rs.next()){
                String type = rs.getString("type_name");
                if (type.equalsIgnoreCase("array")){
                    return true;
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean convertVersionOneToVersionTwo(){
        // Backup Database
        ArrayList<Animation> animations = new ArrayList<>();
        try (Connection connection = SQLManager.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_VERSION_ONE);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Animation tmp = Animation.deserialize(Util.encapColons(rs.getString("data")));
                animations.add(tmp);
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        try {
            for (Animation animation :
                    animations) {
                PreparedStatements.createAnimation(animation);
            }
        } catch (InvalidDataException e){
            AnimationPlugin.logger.error("Failed to convert an animation. ", e);
        }
        SQLManager.get(AnimationPlugin.plugin).deleteTable(TABLE_VERSION_ONE);
        return true;
    }

    public static boolean convertVersionTwoToVersionThree(){
        try (Connection conn = SQLManager.getConnection()){
            Map<UUID, ArrayList<String>> animations = PreparedStatements.getAnimations();
            for (Map.Entry<UUID, ArrayList<String>> entry :
                    animations.entrySet()) {
                ArrayList<String> animationList = entry.getValue();
                for (String animationName :
                        animationList) {
                    String frameTableName = getFrameTableName(animationName, entry.getKey());
                    PreparedStatement alterSubspaceColumnType = conn.prepareStatement(
                            "ALTER TABLE " + frameTableName +
                                    " ALTER COLUMN " + COLUMN_FRAME_SUBSPACE_CONTENTS + " boolean");
                    // need to set default values now where null will be false and otherwise will be true todo

                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
