package mvc.db;

import java.util.List;
import java.util.UUID;
import mvc.Beobachter;
import mvc.Model;
import mvc.shared.Aufgabe;
import mvc.shared.Fach;

/**
 * SQLite-gestützte Implementierung von {@link Model}. Persistiert Aufgaben
 * und Fächer über {@link AufgabeDAO} und {@link FachDAO} und benachrichtigt
 * registrierte Beobachter nach jeder Änderung.
 */
public class SqliteModel implements Model {

    private final AufgabeDAO aufgabeDAO;
    private final FachDAO fachDAO;
    private Beobachter beobachter;

    public SqliteModel() {
        this.aufgabeDAO = new AufgabeDAO();
        this.fachDAO = new FachDAO();
        this.beobachter = null;
    }

    @Override
    public void aufgabeHinzufügen(Aufgabe a) {
        aufgabeDAO.insert(a);
        benachrichtigen();
    }

    @Override
    public Aufgabe[] aufgabenZurückgeben() {
        List<Aufgabe> aufgaben = aufgabeDAO.findAll();
        return aufgaben.toArray(new Aufgabe[aufgaben.size()]);
    }

    @Override
    public boolean aufgabenStatusÄndern(UUID id) {
        boolean geändert = aufgabeDAO.toggleStatus(id);
        if (geändert) {
            benachrichtigen();
        }
        return geändert;
    }

    @Override
    public Fach[] fächerZurückgeben() {
        List<Fach> fächer = fachDAO.findAll();
        return fächer.toArray(new Fach[fächer.size()]);
    }

    @Override
    public void fachHinzufügen(Fach f) {
        fachDAO.insert(f);
        benachrichtigen();
    }

    @Override
    public void registrieren(Beobachter b) {
        beobachter = b;
    }

    @Override
    public void abmelden(Beobachter b) {
        if (beobachter != null && beobachter.equals(b)) {
            beobachter = null;
        }
    }

    private void benachrichtigen() {
        if (beobachter != null) {
            beobachter.datenGeaendert();
        }
    }
}
