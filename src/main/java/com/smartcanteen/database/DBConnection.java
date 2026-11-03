package com.smartcanteen.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DBConnection {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IllegalStateException("db.properties was not found in src/main/resources.");
            }
            PROPERTIES.load(input);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBConnection() {
        // Prevent instantiation of utility class
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            PROPERTIES.getProperty("db.url"),
            PROPERTIES.getProperty("db.username"), 
            PROPERTIES.getProperty("db.password")
        );
    }
}
