package com.servegame.bl4de.Animation.data;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.util.Util;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * File: DatabaseSchemaUpdates.java
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class DatabaseSchemaUpdates {

    private static final String TABLE_VERSION_ONE = "ANIMATION_TABLE";


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

    public static boolean convertVersionOneToVersionTwo(){
        // Backup Database
        ArrayList<Animation> animations = new ArrayList<>();
        try (Connection connection = SQLManager.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + TABLE_VERSION_ONE);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                System.out.println(rs.getString("name"));
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
}
