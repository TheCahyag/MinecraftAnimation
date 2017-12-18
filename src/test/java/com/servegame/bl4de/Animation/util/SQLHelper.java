package com.servegame.bl4de.Animation.util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * File: SQLHelper.java
 *
 * @author Brandon Bires-Navel (brandonnavel@outlook.com)
 */
public class SQLHelper {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:C:\\Users\\brand\\Dropbox\\College\\Programming\\Java\\Minecraft Plugins\\Sponge Plugins\\SpongeVanilla\\run\\config\\animation\\animation\\ANIMATIONS", "", "");
    }
}
