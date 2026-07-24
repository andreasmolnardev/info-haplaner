package mvc.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import mvc.shared.Aufgabe;
import mvc.shared.Fach;

/**
 * CRUD-Zugriff auf die Tabelle Hausaufgaben. Löst die Fremdschlüsselbeziehung
 * fach_id -> Faecher.id über {@link FachDAO} zu einem echten Fach-Objekt auf.
 */
public class AufgabeDAO {

    private final Connection connection;
    private final FachDAO fachDAO;

    public AufgabeDAO() {
        this.connection = DatabaseConnection.geben().getConnection();
        this.fachDAO = new FachDAO();
    }

    /** Create */
    public void insert(Aufgabe a) {
        if (a.id == null) {
            a.id = UUID.randomUUID();
        }
        if (a.fach == null || a.fach.id == null) {
            throw new IllegalArgumentException("Aufgabe benötigt ein gültiges Fach mit id");
        }
        String sql = "INSERT INTO Hausaufgaben (id, fach_id, erstelldatum, ablaufdatum, titel, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, a.id.toString());
            ps.setString(2, a.fach.id.toString());
            ps.setLong(3, a.erstelldatum != null ? a.erstelldatum.getTime() : 0L);
            ps.setLong(4, a.ablaufdatum != null ? a.ablaufdatum.getTime() : 0L);
            ps.setString(5, a.titel);
            ps.setInt(6, a.status ? 1 : 0);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Einfügen der Aufgabe", e);
        }
    }

    /** Read (einzeln) */
    public Aufgabe findById(UUID id) {
        String sql = "SELECT id, fach_id, erstelldatum, ablaufdatum, titel, status "
                + "FROM Hausaufgaben WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Lesen der Aufgabe", e);
        }
    }
    

    public List<Aufgabe> findByDatum(Date datum) {
        List<Aufgabe> aufgaben = new ArrayList<>();
        Calendar start = Calendar.getInstance();
        start.setTime(datum);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        Calendar ende = (Calendar) start.clone();
        ende.add(Calendar.DAY_OF_YEAR, 1);

        String sql = "SELECT id, fach_id, erstelldatum, ablaufdatum, titel, status "
                + "FROM Hausaufgaben WHERE ablaufdatum >= ? AND ablaufdatum < ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, start.getTimeInMillis());
            ps.setLong(2, ende.getTimeInMillis());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    aufgaben.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Lesen der Aufgaben", e);
        }
        return aufgaben;
    }

    /** Read (alle) */
    public List<Aufgabe> findAll() {
        List<Aufgabe> ergebnis = new ArrayList<>();
        String sql = "SELECT id, fach_id, erstelldatum, ablaufdatum, titel, status FROM Hausaufgaben";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ergebnis.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Lesen der Aufgaben", e);
        }
        return ergebnis;
    }

    /** Update */
    public void update(Aufgabe a) {
        String sql = "UPDATE Hausaufgaben SET fach_id = ?, erstelldatum = ?, ablaufdatum = ?, "
                + "titel = ?, status = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, a.fach.id.toString());
            ps.setLong(2, a.erstelldatum != null ? a.erstelldatum.getTime() : 0L);
            ps.setLong(3, a.ablaufdatum != null ? a.ablaufdatum.getTime() : 0L);
            ps.setString(4, a.titel);
            ps.setInt(5, a.status ? 1 : 0);
            ps.setString(6, a.id.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Aktualisieren der Aufgabe", e);
        }
    }

    /** Status umschalten (true <-> false). Gibt zurück, ob eine Zeile betroffen war. */
    public boolean toggleStatus(UUID id) {
        String sql = "UPDATE Hausaufgaben SET status = 1 - status WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Ändern des Aufgabenstatus", e);
        }
    }

    /** Delete */
    public void delete(UUID id) {
        String sql = "DELETE FROM Hausaufgaben WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Löschen der Aufgabe", e);
        }
    }

    /**
     * Mapping ResultSet -> Aufgabe-Objekt. Löst fach_id über FachDAO zu
     * einem vollständig rekonstruierten Fach-Objekt auf.
     */
    private Aufgabe mapRow(ResultSet rs) throws SQLException {
        Aufgabe a = new Aufgabe();
        a.id = UUID.fromString(rs.getString("id"));
        a.fach = fachDAO.findById(UUID.fromString(rs.getString("fach_id")));
        long erstellt = rs.getLong("erstelldatum");
        a.erstelldatum = erstellt != 0L ? new Date(erstellt) : null;
        long ablauf = rs.getLong("ablaufdatum");
        a.ablaufdatum = ablauf != 0L ? new Date(ablauf) : null;
        a.titel = rs.getString("titel");
        a.status = rs.getInt("status") != 0;
        return a;
    }
}
