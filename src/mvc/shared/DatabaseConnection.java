package mvc.shared;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Kapselt die JDBC-Verbindung zur SQLite-Datenbank und legt das Schema
 * (Tabellen Faecher und Hausaufgaben) bei Bedarf an.
 * Die Klasse übernimmt die Herstellung einer Verbindung zur Datenbank.
 */
public final class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:haplaner.db";
    private static DatabaseConnection instance;

    private final Connection connection;

    /**
     * Herstellung einer Verbindung zur Datenbank.
     * Erstellt die JDBC-Verbindung und aktiviert Foreign Key Constraints.
     * 
     * @throws RuntimeException Falls die Verbindung nicht hergestellt werden kann
     */
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

    /**
     * Überprüfung ob Database vorhanden ist, wenn ja gibt sie die zurück,
     * wenn nein wird eine erstellt.
     * Implementiert das Singleton-Pattern für die Datenbankverbindung.
     * 
     * @return Die bestehende oder neu erstellte DatabaseConnection-Instanz
     */
    public static synchronized DatabaseConnection geben() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Gibt die Verbindung zurück.
     * 
     * @return Die JDBC Connection zur Datenbank
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Erstellt Struktur für Hausaufgabendokumentierung.
     * Legt die Tabellen Faecher und Hausaufgaben an, falls sie nicht bereits existieren.
     * 
     * @throws SQLException Falls beim Ausführen der SQL-Befehle ein Fehler auftritt
     */
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
