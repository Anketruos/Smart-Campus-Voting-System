package dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;

/**
 * Provides JDBC connections and bootstraps the local database automatically.
 * This keeps the project easy to run in a classroom environment while still
 * using standard JDBC persistence.
 */
public final class DBConnection {

    private static final Properties PROPERTIES = loadProperties();
    private static final String URL = requireProperty("db.url");
    private static final String USER = PROPERTIES.getProperty("db.user", "");
    private static final String PASSWORD = PROPERTIES.getProperty("db.password", "");
    private static final String DRIVER = PROPERTIES.getProperty("db.driver", "org.sqlite.JDBC");
    private static volatile boolean initialized;

    static {
        try {
            prepareDatabaseDirectory();
            Class.forName(DRIVER);
            initializeDatabase();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize the database.", e);
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        enableSqliteForeignKeys(connection);
        return connection;
    }

    public static synchronized void initializeDatabase() {
        if (initialized) {
            return;
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            enableSqliteForeignKeys(connection);
            runSchemaScript(connection, loadSchemaScript());
            initialized = true;
        } catch (Exception e) {
            throw new RuntimeException("Database bootstrap failed.", e);
        }
    }

    private static void enableSqliteForeignKeys(Connection connection) throws SQLException {
        if (URL.startsWith("jdbc:sqlite:")) {
            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA foreign_keys = ON");
            }
        }
    }

    private static void runSchemaScript(Connection connection, String script) throws SQLException {
        StringBuilder statementBuilder = new StringBuilder();

        for (String line : script.split("\\R")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                continue;
            }

            statementBuilder.append(line).append('\n');
            if (trimmed.endsWith(";")) {
                String sql = statementBuilder.toString().trim();
                statementBuilder.setLength(0);
                if (!sql.isEmpty()) {
                    try (Statement statement = connection.createStatement()) {
                        statement.execute(sql);
                    }
                }
            }
        }
    }

    private static String loadSchemaScript() throws IOException {
        InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("schema.sql");
        if (inputStream == null) {
            throw new IOException("schema.sql was not found on the classpath.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder script = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append('\n');
            }
            return script.toString();
        }
    }

    private static void prepareDatabaseDirectory() throws IOException {
        if (!URL.startsWith("jdbc:sqlite:")) {
            return;
        }

        String databasePath = URL.substring("jdbc:sqlite:".length()).trim();
        if (databasePath.isEmpty() || ":memory:".equals(databasePath)) {
            return;
        }

        Path path = Path.of(databasePath).toAbsolutePath();
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    private static Properties loadProperties() {
        try (InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new IOException("config.properties was not found on the classpath.");
            }

            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database configuration.", e);
        }
    }

    private static String requireProperty(String key) {
        return Objects.requireNonNull(PROPERTIES.getProperty(key), "Missing property: " + key);
    }
}
