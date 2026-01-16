package com.njackal.mmo.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.UUID;

public class MMODatabase {

    public static final Logger LOGGER = LoggerFactory.getLogger("fabric-mmo-db");

    private final Connection conn;

    public MMODatabase(String uri, String username, String password) {
        try {//set jdbc driver thing
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

        try { // init db connection
            conn = DriverManager.getConnection(String.format("%s?user=%s&password=%s",uri,username,password));
            LOGGER.info("Database Connected");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        initTables();
    }

    private void initTables() {
        try {//check if table exists
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM information_schema.tables WHERE table_schema = 'fabricmmo' AND table_name = 'xp' LIMIT 1;");
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return; //table exists so exit early
            }

        } catch (SQLException e) {
            LOGGER.error("Failed to check table init state");
            throw new RuntimeException(e);
        }

        try {// init table

            PreparedStatement statement = conn.prepareStatement(
                    "CREATE TABLE xp (PlayerID VARCHAR(64), " +
                            "NotifMode VARCHAR(16) DEFAULT 'title', " +
                            "XPBarEnabled TINYINT DEFAULT 1, " +
                            "Archery INT DEFAULT 0, " +
                            "Crossbows INT DEFAULT 0, " +
                            "Tridents INT DEFAULT 0, " +
                            "Spears INT DEFAULT 0, " +
                            "Maces INT DEFAULT 0, " +
                            "Axes INT DEFAULT 0, " +
                            "Unarmed INT DEFAULT 0, " +
                            "TNT INT DEFAULT 0, " +
                            "Mining INT DEFAULT 0, " +
                            "Excavation INT DEFAULT 0, " +
                            "Woodcutting INT DEFAULT 0, " +
                            "Acrobatics INT DEFAULT 0, " +
                            "Alchemy INT DEFAULT 0, " +
                            "Fishing INT DEFAULT 0, " +
                            "Herbalism INT DEFAULT 0, " +
                            "Repair INT DEFAULT 0, " +
                            "Taming INT DEFAULT 0, " +
                            "PRIMARY KEY (PlayerID));"
            );
            statement.execute();
            LOGGER.info("Table Initialized");
        } catch (SQLException e) {
            LOGGER.error("Failed to create tables in MMODatabase");
            throw new RuntimeException(e);
        }
    }

    public int getXp(UUID player, XPType type) {
        int xp = 0;
        try {
            PreparedStatement statement = conn.prepareStatement(
                    String.format("SELECT %s FROM xp WHERE PlayerID = ?;", type.dbId)
            );
            statement.setString(1, player.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                xp = rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to get XP from database in MMODatabase");
        }
        return xp;
    }

    public int addXp(UUID player, XPType xpType, int xpAmount) {
        int totalXp = 0;
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO xp (PlayerID, %s) VALUES (?, ?) ON DUPLICATE KEY UPDATE %s = %s + ?;".replaceAll("%s",xpType.dbId)
            );
            statement.setString(1, player.toString());
            statement.setInt(2, xpAmount);
            statement.setInt(3, xpAmount);

            statement.executeUpdate();

            LOGGER.info("{} {} XP added to {}", xpAmount, xpType, player);//todo debug

            PreparedStatement s = conn.prepareStatement("SELECT %s FROM xp WHERE PlayerID = ?;".replaceAll("%s",xpType.dbId));
            s.setString(1, player.toString());
            ResultSet rs = s.executeQuery();
            if (rs.next()) {
                totalXp = rs.getInt(1);
            }
            rs.close();
        } catch (SQLException e) {
            LOGGER.error("Failed to add XP to database in MMODatabase");
            LOGGER.error(e.getMessage());
            throw new DatabaseException("Failed to add XP to database in MMODatabase");
        }

        return totalXp;
    }

    /**
     * try to initialize a player from their uuid, if the player already exists nothing happens
     * @param uuid uuid of the player
     */
    public void initPlayer(UUID uuid) {
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO xp (PlayerID) Values (?) ON DUPLICATE KEY UPDATE PlayerID = PlayerID;"
            );
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("failed to initialize db for {}", uuid);
        }
    }

    /**
     * set notification mode for a player
     * @param uuid player id to set for
     * @param mode mode to set to
     */
    public void setNotifMode(UUID uuid, NotificationMode mode) {
        try {
            PreparedStatement statement =
                    conn.prepareStatement("UPDATE xp SET NotifMode = ? WHERE PlayerID = ?;");
            statement.setString(1, mode.value);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Failed to set notif mode for {}", uuid);
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * get notification mode for a player
     *
     * @param uuid player to get notification mode for
     * @return notification mode for player
     * @throws DatabaseException thrown if the sql fails or the mode is not found for the player
     */
    public NotificationMode getNotifMode(UUID uuid) throws DatabaseException {
        try {
            PreparedStatement statement =
                    conn.prepareStatement("SELECT NotifMode FROM xp WHERE PlayerID = ?");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return NotificationMode.fromValue(rs.getString(1));
            } else {
                throw new DatabaseException("Failed to get NotifMode for " + uuid);
            }
        } catch (SQLException e) {
            LOGGER.warn("Failed to find notif mode for {}", uuid);
            throw new DatabaseException("Failed query to get notif mode for " + uuid,e);
        }

    }

    /**
     * set xp bar visibility for a player
     * @param uuid player to set for
     * @param enabled is bar visible
     */
    public void setXpBarEnabled(UUID uuid, boolean enabled) {
        try {
            PreparedStatement statement =
                    conn.prepareStatement("UPDATE xp SET XPBarEnabled = ? WHERE PlayerID = ?");
            statement.setInt(1, enabled? 1: 0);
            statement.setString(2, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.warn("Failed to set XPBarEnabled for {}", uuid);
        }
    }

    /**
     * get xp bar visibility for a player
     * @param uuid player to get visibility for
     * @return boolean whether xp bar is enabled
     * @throws DatabaseException thrown if sql fails or player not found
     */
    public boolean getXpBarEnabled(UUID uuid) throws DatabaseException {
        try {
            PreparedStatement statement =
                    conn.prepareStatement("SELECT XPBarEnabled FROM xp WHERE PlayerID = ?;");
            statement.setString(1, uuid.toString());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            } else {
                throw new DatabaseException("Failed to get XPBarEnabled for " + uuid);
            }

        } catch (SQLException e) {
            LOGGER.warn("Failed to find XPBarEnabled for {}", uuid);
            throw new DatabaseException("Failed query to find XPBarEnabled for " + uuid, e);
        }
    }


}
