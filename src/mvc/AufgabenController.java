
package mvc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;
import mvc.db.SqliteModel;
import mvc.shared.Aufgabe;
import mvc.shared.Fach;


public class AufgabenController implements Controller{
    // Attribute
    private final View view;
    private final Model model;
    // Konstruktor
    public AufgabenController(){
        model = new SqliteModel();
        view = new View(this);
        model.registrieren(view);
        view.setVisible(true);
    }
    //Methoden aus Interface
    @Override
    public void fachHinzufügen(String name, String kürzel){
        if (kürzel != null && kürzel.trim().length() > 0) {
            String fachName = name == null || name.trim().length() == 0 ? kürzel.trim() : name.trim();
            model.fachHinzufügen(new Fach(fachName, kürzel.trim()));
        }
    }

    @Override
    public void aufgabeHinzufügen(UUID fach, String titel, Date ablaufdatum){
        if (titel == null || titel.trim().length() == 0) {
            return;
        }
        Aufgabe a = new Aufgabe();
        String name = titel.trim();
        for (Fach f : model.fächerZurückgeben()) {
            if (f.gibId() != null && f.gibId().equals(fach)) {
                a = new Aufgabe(f, name, ablaufdatum);
                break;
            }
        }
        if (a.fach == null) {
            throw new IllegalArgumentException("Unbekanntes Fach: " + fach);
        }
        model.aufgabeHinzufügen(a);
    }

    @Override
    public void statusÄndernButtonGedrueckt(UUID aufgabe){
        model.aufgabenStatusÄndern(aufgabe);
    }

    @Override
    public Aufgabe[] aufgabenZurückgeben() {
        return model.aufgabenZurückgeben();
    }

    @Override
    public Aufgabe[] aufgabenNachDatumSortiertZurückgeben() {
        Aufgabe[] aufgaben = model.aufgabenZurückgeben();
        Aufgabe[] sortierteAufgaben = Arrays.copyOf(aufgaben, aufgaben.length);
        Arrays.sort(sortierteAufgaben, Comparator.comparing(Aufgabe::gibAblaufdatum));
        return sortierteAufgaben;
    }

    @Override
    public Aufgabe[] aufgabenNachNameSortiertZurückgeben() {
        Aufgabe[] aufgaben = model.aufgabenZurückgeben();
        Aufgabe[] sortierteAufgaben = Arrays.copyOf(aufgaben, aufgaben.length);
        Arrays.sort(sortierteAufgaben, Comparator.comparing(Aufgabe::gibTitel));
        return sortierteAufgaben;
    }

    @Override
    public Aufgabe[] aufgabenNachFälligkeitsdatumZurückgeben(Date datum) {
        return model.aufgabenNachFälligkeitsdatumZurückgeben(datum);
    }

    @Override
    public Fach[] fächerZurückgeben() {
        return model.fächerZurückgeben();
    }

}
