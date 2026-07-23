
package mvc;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.UUID;
import mvc.db.DatabaseConnection;
import mvc.shared.Aufgabe;
import mvc.shared.Fach;


public class AufgabenController implements Controller{
    // Attribute
    private View view;
    // Konstruktor
    public AufgabenController(){
        DatabaseConnection.geben();
        view = new View(this);
        view.setVisible(true);
    }
    //Methoden aus Interface
    @Override
    public void fachHinzufügen(String name, String kürzel){
        if (kürzel != null && kürzel.trim().length() > 0) {
            String fachName = name == null || name.trim().length() == 0 ? kürzel.trim() : name.trim();
            Wert.geben().fachHinzufügen(new Fach(fachName, kürzel.trim()));
        }
    }

    @Override
    public void aufgabeHinzufügen(UUID fach, String titel, Date ablaufdatum){
        if (titel == null || titel.trim().length() == 0) {
            return;
        }
        Aufgabe a = new Aufgabe();
        String name = titel.trim();
        for (Fach f : Wert.geben().fächerZurückgeben()) {
            if (f.gibId() != null && f.gibId().equals(fach)) {
                a = new Aufgabe(f, name, ablaufdatum);
                break;
            }
        }
        if (a.fach == null) {
            throw new IllegalArgumentException("Unbekanntes Fach: " + fach);
        }
        Wert.geben().aufgabeHinzufügen(a);
    }

    @Override
    public void statusÄndernButtonGedrueckt(UUID aufgabe){
        Wert.geben().aufgabenStatusÄndern(aufgabe);
    }

    @Override
    public Aufgabe[] aufgabenZurückgeben() {
        return Wert.geben().aufgabenZurückgeben();
    }

    @Override
    public Aufgabe[] aufgabenNachDatumSortiertZurückgeben() {
        Aufgabe[] aufgaben = Wert.geben().aufgabenZurückgeben();
        Aufgabe[] sortierteAufgaben = Arrays.copyOf(aufgaben, aufgaben.length);
        Arrays.sort(sortierteAufgaben, Comparator.comparing(Aufgabe::gibAblaufdatum));
        return sortierteAufgaben;
    }

    @Override
    public Aufgabe[] aufgabenNachNameSortiertZurückgeben() {
        Aufgabe[] aufgaben = Wert.geben().aufgabenZurückgeben();
        Aufgabe[] sortierteAufgaben = Arrays.copyOf(aufgaben, aufgaben.length);
        Arrays.sort(sortierteAufgaben, Comparator.comparing(Aufgabe::gibTitel));
        return sortierteAufgaben;
    }

    @Override
    public Aufgabe[] aufgabenNachFälligkeitsdatumZurückgeben(Date datum) {
        ArrayList<Aufgabe> gefilterteAufgaben = new ArrayList<Aufgabe>();
        if (datum == null) {
            return new Aufgabe[0];
        }
        Calendar gesuchtesDatum = Calendar.getInstance();
        gesuchtesDatum.setTime(datum);
        for (Aufgabe aufgabe : Wert.geben().aufgabenZurückgeben()) {
            if (aufgabe.gibAblaufdatum() == null) {
                continue;
            }
            Calendar ablaufdatum = Calendar.getInstance();
            ablaufdatum.setTime(aufgabe.gibAblaufdatum());
            if (gesuchtesDatum.get(Calendar.YEAR) == ablaufdatum.get(Calendar.YEAR)
                    && gesuchtesDatum.get(Calendar.DAY_OF_YEAR) == ablaufdatum.get(Calendar.DAY_OF_YEAR)) {
                gefilterteAufgaben.add(aufgabe);
            }
        }
        return gefilterteAufgaben.toArray(new Aufgabe[gefilterteAufgaben.size()]);
    }

    public void aufgabeHinzufügen(Aufgabe a){
        Wert.geben().aufgabeHinzufügen(a);
    }

}
