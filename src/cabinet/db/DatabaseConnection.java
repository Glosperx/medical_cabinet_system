package cabinet.db;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;
    private final Properties props = new Properties();

    private DatabaseConnection() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (is == null) throw new RuntimeException("db.properties negasit in classpath");
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Eroare la incarcarea db.properties", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password")
            );
        }
        return connection;
    }

    public void runScript(String filePath) throws SQLException, IOException {
        String sql = new String(Files.readAllBytes(Paths.get(filePath)));
        try (Statement stmt = getConnection().createStatement()) {
            for (String s : sql.split(";")) {
                String trimmed = s.trim();
                if (!trimmed.isEmpty()) {
                    stmt.execute(trimmed);
                }
            }
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Eroare la inchiderea conexiunii: " + e.getMessage());
        }
    }
}
