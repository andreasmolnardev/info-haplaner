package mvc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mvc.shared.Fach;

/**
 * Die Klasse übernimmt Zugriff auf Tabelle Fächer.
 * CRUD-Zugriff auf die Tabelle Faecher. Liegt im Package mvc.shared, um
 * direkt auf die paketweiten Felder von {@link Fach} zugreifen zu können,
 * ohne Fach.java um Getter/Setter erweitern zu müssen.
 */
public class FachDAO {

    private final Connection connection;

    /**
     * Erstellt Verbindung zu Datenbank, die von allen Methoden verwendet wird.
     */
    public FachDAO() {
        this.connection = DatabaseConnection.geben().getConnection();
    }

    /**
     * Fügt ein neues Fach in die Datenbank ein. Falls keine ID vorhanden ist,
     * wird automatisch eine UUID verwendet.
     * 
     * @param f Das zu einfügende Fach-Objekt
     */
    public void insert(Fach f) {
        if (f.id == null) {
            f.id = UUID.randomUUID();
        }
        String sql = "INSERT INTO Faecher (id, kuerzel, name) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, f.id.toString());
            ps.setString(2, f.kürzel);
            ps.setString(3, f.name);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Einfügen des Fachs", e);
        }
    }

    /**
     * Sucht ein Fach anhand seiner ID und gibt das entsprechende Fach-Objekt oder null zurück.
     * 
     * @param id Die UUID des gesuchten Fachs
     * @return Das gefundene Fach-Objekt oder null, falls nicht vorhanden
     */
    public Fach findById(UUID id) {
        String sql = "SELECT id, kuerzel, name FROM Faecher WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Lesen des Fachs", e);
        }
    }

    /**
     * Liest alle Fächer aus der Datenbank und gibt sie als Liste zurück.
     * 
     * @return Liste aller Fächer aus der Datenbank
     */
    public List<Fach> findAll() {
        List<Fach> ergebnis = new ArrayList<>();
        String sql = "SELECT id, kuerzel, name FROM Faecher";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ergebnis.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Lesen der Fächer", e);
        }
        return ergebnis;
    }

    /**
     * Ein vorhandenes Fach wird durch ein neues Objekt der Klasse Fach mit der gleichen UUID ersetzt.
     * 
     * @param f Das aktualisierte Fach-Objekt
     */
    public void update(Fach f) {
        String sql = "UPDATE Faecher SET kuerzel = ?, name = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, f.kürzel);
            ps.setString(2, f.name);
            ps.setString(3, f.id.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Aktualisieren des Fachs", e);
        }
    }

    /**
     * Löscht das Fach mit der angegebenen ID aus der Datenbank.
     * 
     * @param id Die UUID des zu löschenden Fachs
     */
    public void delete(UUID id) {
        String sql = "DELETE FROM Faecher WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Löschen des Fachs", e);
        }
    }

    /**
     * Wandelt einen Datensatz aus der Datenbank in ein Objekt der Klasse Fach um.
     * Mapping ResultSet -> Fach-Objekt (Zugriff auf paketprivate Felder).
     * 
     * @param rs Das ResultSet mit den Fach-Daten
     * @return Das erstellte Fach-Objekt
     * @throws SQLException Falls beim Zugriff auf das ResultSet ein Fehler auftritt
     */
    Fach mapRow(ResultSet rs) throws SQLException {
        Fach f = new Fach();
        f.id = UUID.fromString(rs.getString("id"));
        f.kürzel = rs.getString("kuerzel");
        f.name = rs.getString("name");
        return f;
    }
}
