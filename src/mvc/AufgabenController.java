
package mvc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;
import mvc.db.DatabaseConnection;
import mvc.db.SqliteModel;
import mvc.shared.Aufgabe;
import mvc.shared.Fach;


public class AufgabenController implements Controller{
    // Attribute
    private View view;
    private final SqliteModel sqliteModel;
    // Konstruktor
    public AufgabenController(){
        DatabaseConnection.geben();
        sqliteModel = new SqliteModel();
        view = new View(this);
        view.setVisible(true);
    }
    //Methoden aus Interface
    @Override
    public void fachHinzufügen(String name, String kürzel){
        if (kürzel != null && kürzel.trim().length() > 0) {
            String fachName = name == null || name.trim().length() == 0 ? kürzel.trim() : name.trim();
            sqliteModel.fachHinzufügen(new Fach(fachName, kürzel.trim()));
        }
    }

    @Override
    public void aufgabeHinzufügen(UUID fach, String titel, Date ablaufdatum){
        if (titel == null || titel.trim().length() == 0) {
            return;
        }
        Aufgabe a = new Aufgabe();
        String name = titel.trim();
        for (Fach f : sqliteModel.fächerZurückgeben()) {
            if (f.gibId() != null && f.gibId().equals(fach)) {
                a = new Aufgabe(f, name, ablaufdatum);
                break;
            }
        }
        if (a.fach == null) {
            throw new IllegalArgumentException("Unbekanntes Fach: " + fach);
        }
        sqliteModel.aufgabeHinzufügen(a);
    }

    @Override
    public void statusÄndernButtonGedrueckt(UUID aufgabe){
        sqliteModel.aufgabenStatusÄndern(aufgabe);
    }

    @Override
    public Aufgabe[] aufgabenZurückgeben() {
        return sqliteModel.aufgabenZurückgeben();
    }

    @Override
    public Aufgabe[] aufgabenNachDatumSortiertZurückgeben() {
        Aufgabe[] aufgaben = sqliteModel.aufgabenZurückgeben();
        Aufgabe[] sortierteAufgaben = Arrays.copyOf(aufgaben, aufgaben.length);
        Arrays.sort(sortierteAufgaben, Comparator.comparing(Aufgabe::gibAblaufdatum));
        return sortierteAufgaben;
    }

    @Override
    public Aufgabe[] aufgabenNachNameSortiertZurückgeben() {
        Aufgabe[] aufgaben = sqliteModel.aufgabenZurückgeben();
        Aufgabe[] sortierteAufgaben = Arrays.copyOf(aufgaben, aufgaben.length);
        Arrays.sort(sortierteAufgaben, Comparator.comparing(Aufgabe::gibTitel));
        return sortierteAufgaben;
    }

    @Override
    public Aufgabe[] aufgabenNachFälligkeitsdatumZurückgeben(Date datum) {
        return sqliteModel.aufgabenNachFälligkeitsdatumZurückgeben(datum);
    }

    public void aufgabeHinzufügen(Aufgabe a){
        sqliteModel.aufgabeHinzufügen(a);
    }

}
