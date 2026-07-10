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
 * CRUD-Zugriff auf die Tabelle Faecher.
 */
public class FachDAO {

    private final Connection connection;

    public FachDAO() {
        this.connection = DatabaseConnection.geben().getConnection();
    }

    /** Create */
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

    /** Read (einzeln) */
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

    /** Read (alle) */
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

    /** Update */
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

    /** Delete */
    public void delete(UUID id) {
        String sql = "DELETE FROM Faecher WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Löschen des Fachs", e);
        }
    }

    /** Mapping ResultSet -> Fach-Objekt (Zugriff auf paketprivate Felder) */
    Fach mapRow(ResultSet rs) throws SQLException {
        Fach f = new Fach();
        f.id = UUID.fromString(rs.getString("id"));
        f.kürzel = rs.getString("kuerzel");
        f.name = rs.getString("name");
        return f;
    }
}
