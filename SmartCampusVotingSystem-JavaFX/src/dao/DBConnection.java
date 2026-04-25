package dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Provides JDBC database connections (Unit 3: Persistence and Databases).
 * Loads connection settings from config.properties.
 */
public class DBConnection {

    private static String url;
    private static String user;
    private static String password;

    static {
        try {
            Properties props = new Properties();
            InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("config.properties");
            props.load(input);
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load DB config: " + e.getMessage(), e);
        }
    }

    /** Returns a new connection each call — callers must close it (use try-with-resources). */
    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }
}
