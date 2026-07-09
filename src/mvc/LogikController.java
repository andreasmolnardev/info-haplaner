package mvc;

import java.util.Date;
import java.util.UUID;
import mvc.shared.Aufgabe;
import mvc.shared.Fach;
import mvc.shared.SqliteModel;

/**
 * Controller, der die Benutzeraktionen auf das persistente {@link SqliteModel}
 * abbildet. Jede Methode löst letztlich eine SQL-Query über die DAOs aus:
 * Fächer landen in der Tabelle Faecher, Aufgaben in Hausaufgaben.
 */
public class LogikController implements Controller {

    private final Model model;

    public LogikController(Model model) {
        this.model = model;
    }

    @Override
    public void fachHinzufügen(String name, String kürzel) {
        // -> FachDAO.insert -> INSERT INTO Faecher (id, kuerzel, name) VALUES (?, ?, ?)
        Fach f = new Fach(name, kürzel);
        model.fachHinzufügen(f);
    }

    @Override
    public void aufgabeHinzufügen(UUID fach, String titel, Date ablaufdatum) {
        Fach f = fachZuId(fach);
        if (f == null) {
            throw new IllegalArgumentException("Kein Fach mit id " + fach + " gefunden");
        }
        // -> AufgabeDAO.insert -> INSERT INTO Hausaufgaben (id, fach_id, ...) VALUES (...)
        Aufgabe a = new Aufgabe(f, titel, ablaufdatum);
        model.aufgabeHinzufügen(a);
    }

    @Override
    public void plusButtonGedrueckt() {
        // Reine UI-Aktion (Eingabemaske öffnen). Kein Datenbankzugriff nötig;
        // das eigentliche Anlegen läuft über fachHinzufügen/aufgabeHinzufügen.
    }

    @Override
    public void statusÄndernButtonGedrueckt(UUID id) {
        // -> AufgabeDAO.toggleStatus -> UPDATE Hausaufgaben SET status = 1 - status WHERE id = ?
        model.aufgabenStatusÄndern(id);
    }

    /**
     * Löst eine Fach-UUID über einen Lesezugriff auf die Datenbank
     * (FachDAO.findAll -> SELECT ... FROM Faecher) zu einem Fach-Objekt auf.
     */
    private Fach fachZuId(UUID id) {
        for (Fach f : model.fächerZurückgeben()) {
            if (id.equals(f.getId())) {
                return f;
            }
        }
        return null;
    }
}
