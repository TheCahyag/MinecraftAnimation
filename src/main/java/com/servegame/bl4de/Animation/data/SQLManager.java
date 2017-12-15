package com.servegame.bl4de.Animation.data;

import com.servegame.bl4de.Animation.AnimationPlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import static com.servegame.bl4de.Animation.data.SQLResources.ANIMATION_TABLE;
import static com.servegame.bl4de.Animation.data.SQLResources.TABLES;

/**
 * File: SQLManager.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SQLManager {
    private static ConcurrentHashMap<String, SQLManager> sqlManagers = new ConcurrentHashMap<>();

    private PluginContainer plugin;
    private String database;

    /*
    private String url;
    private String prefix;
    private String username;
    private String password;
    */


    /**
     * TODO
     * @param plugin
     */
    private SQLManager(PluginContainer plugin){
        this.plugin = plugin;

        initSettings();
    }

    /**
     * Create default must-have tables
     */
    private void initSettings(){
        // In the future this would get data from a config file
        this.database = "ANIMATIONS";
        try (Connection connection = getDataSource().getConnection()){
            for (String table :
                    TABLES) {
                connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + table + " (name VARCHAR2(255), owner UUID, data CLOB)")
                        .executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create frame table, usually called when an Animation is created
     * @param name - name of the table
     * @return true if successful, false otherwise
     */
    protected boolean createFrameTable(String name){
        try (Connection conn = getConnection()){
            conn.prepareStatement("CREATE TABLE " + name + " (id PRIMARY KEY VARCHAR2(255), data CLOB)").executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Drop the frame table, usually called when an animation is deleted
     * @param name - name of the table
     * @return true if it was successful, false otherwise
     */
    protected boolean dropFrameTable(String name){
        if (name.equals(ANIMATION_TABLE)){
            // Can't drop animations table
            return false;
        }
        try (Connection conn = getConnection()){
            conn.prepareStatement("DROP TABLE " + name).executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * TODO
     * @param plugin
     * @return
     */
    public static SQLManager get(PluginContainer plugin) {
        if (!sqlManagers.containsKey(plugin.getId())) {
            SQLManager sqlManager = new SQLManager(plugin);
            sqlManagers.put(plugin.getId(), sqlManager);
        }

        return sqlManagers.get(plugin.getId());
    }

    /**
     * TODO
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return get(AnimationPlugin.plugin).getDataSource().getConnection();
    }

    /**
     * TODO
     * @return
     * @throws SQLException
     */
    public DataSource getDataSource() throws SQLException {
        SqlService sqlService = Sponge.getServiceManager().provide(SqlService.class).get();
        return sqlService.getDataSource("jdbc:h2:./config/" + this.plugin.getId() + "/animation/" + this.database);
    }
}
