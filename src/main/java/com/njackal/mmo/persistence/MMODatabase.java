package com.njackal.mmo.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

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
                    "CREATE TABLE xp (Swords INT, Archery INT, Crossbows INT, Tridents INT, Spears INT, Maces INT, Axes INT, Unarmed INT, TNT INT, Mining INT, Excavation INT, Woodcutting INT, Acrobatics INT, Alchemy INT, Fishing Int, Herbalism INT, Repair INT, Taming INT);"
            );
            statement.execute();
            LOGGER.info("Table Initialized");
        } catch (SQLException e) {
            LOGGER.error("Failed to create tables in MMODatabase");
            throw new RuntimeException(e);
        }
    }
}
