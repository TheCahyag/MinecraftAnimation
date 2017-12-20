package com.servegame.bl4de.Animation.data;

import com.servegame.bl4de.Animation.AnimationPlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import static com.servegame.bl4de.Animation.data.SQLResources.*;

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
        this.database = DATABASE;
        try (Connection connection = getDataSource().getConnection()){
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + ANIMATION_TABLE + " (" +
                    "name VARCHAR2(255), " +
                    "owner UUID, " +
                    "status ENUM('STOPPED', 'RUNNING', 'PAUSED'), " +
                    "startFrameIndex INT, " +
                    "tickDelay INT, " +
                    "cycles INT, " +
                    "frameNames ARRAY)")
                    .executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    void createFrameTable(String tableName){
        try (Connection conn = getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement("CREATE TABLE " + tableName + " ()");
        } catch (SQLException e){

        }
    }

    void createContentsTable(String tableName){

    }
}
