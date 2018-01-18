package com.servegame.bl4de.Animation.data;

import com.servegame.bl4de.Animation.AnimationPlugin;
import com.servegame.bl4de.Animation.model.Animation;
import com.servegame.bl4de.Animation.model.Frame;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.sql.SqlService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
     * Default constructor that just inits itself and sets a few things up
     * @param plugin
     */
    private SQLManager(PluginContainer plugin){
        this.plugin = plugin;
        initSettings();
    }

    /**
     * Create default must-have tables (currently only the animation table)
     */
    private void initSettings() {
        // In the future this would get data from a config file
        this.database = DATABASE;
        try (Connection connection = getDataSource().getConnection()){
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + ANIMATION_TABLE + " (" +
                    "name VARCHAR2(255), " +
                    "owner UUID, " +
                    "status VARCHAR_IGNORECASE(100) CHECK (status IN ('STOPPED', 'RUNNING', 'PAUSED')), " +
                    "startFrameIndex INT, " +
                    "tickDelay INT, " +
                    "cycles INT, " +
                    "frameNames ARRAY," +
                    "animation_cornerOne CLOB, " +
                    "animation_cornerTwo CLOB) ")
                    .executeUpdate();
        } catch (SQLException e) {
            AnimationPlugin.logger.error("Failed to init database: ", e);
        }
    }

    /**
     * Get an instance of the {@link SQLManager}, this gives access to the create and delete table methods
     * @param plugin instance of the {@link PluginContainer} that is requesting access to the SQLManger
     * @return {@link SQLManager} instance
     */
    public static SQLManager get(PluginContainer plugin) {
        if (!sqlManagers.containsKey(plugin.getId())) {
            SQLManager sqlManager = new SQLManager(plugin);
            sqlManagers.put(plugin.getId(), sqlManager);
        }

        return sqlManagers.get(plugin.getId());
    }

    /**
     * Get a {@link Connection} to the database responsible for storing all information regarding the
     * {@link Animation} and their given {@link Frame}s
     * @return the {@link Connection} object
     * @throws SQLException when a connection could not be obtained or other bad things happen
     */
    public static Connection getConnection() throws SQLException {
        return get(AnimationPlugin.plugin).getDataSource().getConnection();
    }

    public static boolean testConnection(){
        try (Connection conn = SQLManager.getConnection()){
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private DataSource getDataSource() throws SQLException {
        SqlService sqlService = Sponge.getServiceManager().provide(SqlService.class).get();
        return sqlService.getDataSource("jdbc:h2:./config/" + this.plugin.getId() + "/animation/" + this.database);
    }

    /**
     * Delete a given table. Used for deleting content tables and frame data tables when either an
     * {@link Animation} is deleted or a {@link Frame} is deleted.
     * @param tableName name of the table to delete, this is usually obtained from
     *                  {@link SQLResources#getContentTableName(Animation, Frame)} or
     *                  {@link SQLResources#getFrameTableName(Animation)}
     */
    void deleteTable(String tableName){
        try (Connection connection = SQLManager.getConnection()){
            connection.prepareStatement("DROP TABLE IF EXISTS " + tableName).executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Creates a table that stores information about the {@link Frame}s assoiated with an {@link Animation}.
     * The name of the table should always be generated with {@link SQLResources#getFrameTableName(Animation)}
     * @param tableName the name the new table will be given
     */
    void createFrameTable(String tableName){
        try (Connection conn = getConnection()){
            conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                    "frameName VARCHAR2(255), " +
                    "creator UUID, " +
                    "subspace_cornerOne CLOB, " +
                    "subspace_cornerTwo CLOB, " +
                    "subspace_contents ARRAY" +
                    ")").executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Create a table that is used to store the contents of a {@link Frame}, the name of the
     * table should be obtained for {@link SQLResources#getContentTableName(Animation, Frame)}
     * @param tableName the name of the table that will be created
     */
    void createContentsTable(String tableName){
        try (Connection conn = getConnection()){
            conn.prepareStatement("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                    "xyz VARCHAR2(100), " +
                    "data CLOB" +
                    ")").executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static boolean doesTableExist(String tableName){
        try (Connection connection = SQLManager.getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE table_name = ?");
            statement.setString(1, tableName.toUpperCase());
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
