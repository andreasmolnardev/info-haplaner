package mvc.shared;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import mvc.Beobachter;
import mvc.Model;

/**
 * SQLite-gestützte Implementierung von {@link Model}. Persistiert Aufgaben
 * und Fächer über {@link AufgabeDAO} und {@link FachDAO} und benachrichtigt
 * registrierte Beobachter nach jeder Änderung, analog zu Wert.zahlErhoehen().
 */
public class SqliteModel implements Model {

    private final AufgabeDAO aufgabeDAO;
    private final FachDAO fachDAO;
    private final List<Beobachter> beobachter;

    public SqliteModel() {
        this.aufgabeDAO = new AufgabeDAO();
        this.fachDAO = new FachDAO();
        this.beobachter = new LinkedList<>();
    }

    @Override
    public void aufgabeHinzufügen(Aufgabe a) {
        aufgabeDAO.insert(a);
        for (Beobachter b : beobachter) {
            b.aufgabeAnzeigen(a);
        }
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
            Aufgabe a = aufgabeDAO.findById(id);
            for (Beobachter b : beobachter) {
                b.aufgabenStatusGeaendert(a.fach);
            }
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
        for (Beobachter b : beobachter) {
            b.fachAnzeigen(f);
        }
    }

    @Override
    public void registrieren(Beobachter b) {
        beobachter.add(b);
    }

    @Override
    public void abmelden(Beobachter b) {
        beobachter.remove(b);
    }
}
