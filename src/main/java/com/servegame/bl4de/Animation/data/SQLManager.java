package com.servegame.bl4de.Animation.data;

import com.servegame.bl4de.Animation.AnimationPlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * File: SQLManager.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SQLManager {
    private static ConcurrentHashMap<String, SQLManager> sqlManagers = new ConcurrentHashMap<>();

    private PluginContainer plugin;
    private String database;
    private String url;
    private String prefix;
    private String username;
    private String password;

    /**
     * TODO
     * @param plugin
     */
    private SQLManager(PluginContainer plugin){
        this.plugin = plugin;

        initSettings();
    }

    private void initSettings(){
        // In the future this would get data from a config file
        this.database = "animation";
        try {
            getDataSource()
                    .getConnection()
                    .prepareStatement("CREATE TABLE IF NOT EXISTS " + this.database + " (name VARCHAR(255) PRIMARY KEY, data VARCHAR2(25000))")
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
        return AnimationPlugin.sqlManager.getDataSource().getConnection();
    }

    /**
     * TODO
     * @return
     * @throws SQLException
     */
    public DataSource getDataSource() throws SQLException {
        SqlService sqlService = Sponge.getServiceManager().provide(SqlService.class).get();
        return sqlService.getDataSource("jdbc:h2:./config/" + this.plugin.getId() + "/" + this.database);
    }
}