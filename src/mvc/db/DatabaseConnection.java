package mvc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Kapselt die JDBC-Verbindung zur SQLite-Datenbank und legt das Schema
 * (Tabellen Faecher und Hausaufgaben) bei Bedarf an.
 */
public final class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:haplaner.db";
    private static DatabaseConnection instance;

    private final Connection connection;

    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(URL);
            try (Statement pragma = connection.createStatement()) {
                pragma.execute("PRAGMA foreign_keys = ON");
            }
            erstelleSchema();
        } catch (SQLException e) {
            throw new RuntimeException("Konnte keine Verbindung zur Datenbank herstellen", e);
        }
    }

    public static synchronized DatabaseConnection geben() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void erstelleSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Faecher (" +
                "  id TEXT PRIMARY KEY," +
                "  kuerzel TEXT NOT NULL," +
                "  name TEXT NOT NULL" +
                ")"
            );
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS Hausaufgaben (" +
                "  id TEXT PRIMARY KEY," +
                "  fach_id TEXT NOT NULL REFERENCES Faecher(id)," +
                "  erstelldatum INTEGER," +
                "  ablaufdatum INTEGER," +
                "  titel TEXT," +
                "  status INTEGER NOT NULL DEFAULT 0" +
                ")"
            );
        }
    }
}
