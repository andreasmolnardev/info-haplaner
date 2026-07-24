/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;
import java.util.LinkedList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import mvc.shared.Aufgabe;
import mvc.shared.Fach;
/**
 *
 * @author So
 */
public class Wert implements Model{
    // Attribute
    private static Wert w = new Wert();
    private Beobachter beobachter;
    private LinkedList<Aufgabe> aufgaben;
    private LinkedList<Fach> fächer;
    // Konstruktor
    private Wert(){
        beobachter = null;
        aufgaben = new LinkedList<Aufgabe>();
        fächer = new LinkedList<Fach>();
    }
    // Ausgabe für Singleton
    static Wert geben(){
        return w;
    }
    // Methoden aus Model
    public void aufgabeHinzufügen(Aufgabe a){
        aufgaben.add(a);
        benachrichtigen();
    }

    public Aufgabe[] aufgabenZurückgeben(){
        return aufgaben.toArray(new Aufgabe[aufgaben.size()]);
    }

    public Aufgabe[] aufgabenNachFälligkeitsdatumZurückgeben(Date datum) {
        if (datum == null) {
            return new Aufgabe[0];
        }
        LinkedList<Aufgabe> gefilterteAufgaben = new LinkedList<Aufgabe>();
        Calendar gewünschtesDatum = Calendar.getInstance();
        gewünschtesDatum.setTime(datum);
        for (Aufgabe aufgabe : aufgaben) {
            if (aufgabe.gibAblaufdatum() != null && gleichesKalenderdatum(
                    aufgabe.gibAblaufdatum(), gewünschtesDatum)) {
                gefilterteAufgaben.add(aufgabe);
            }
        }
        return gefilterteAufgaben.toArray(new Aufgabe[gefilterteAufgaben.size()]);
    }

    private boolean gleichesKalenderdatum(Date datum, Calendar vergleichsdatum) {
        Calendar kalender = Calendar.getInstance();
        kalender.setTime(datum);
        return kalender.get(Calendar.YEAR) == vergleichsdatum.get(Calendar.YEAR)
                && kalender.get(Calendar.DAY_OF_YEAR) == vergleichsdatum.get(Calendar.DAY_OF_YEAR);
    }

    public boolean aufgabenStatusÄndern(UUID id){
        for (Aufgabe aufgabe : aufgaben) {
            if (aufgabe.gibId() != null && aufgabe.gibId().equals(id)) {
                aufgabe.statusUmschalten();
                benachrichtigen();
                return true;
            }
        }
        return false;
    }

    public Fach[] fächerZurückgeben(){
        return fächer.toArray(new Fach[fächer.size()]);
    }

    public void fachHinzufügen(Fach f){
        fächer.add(f);
        benachrichtigen();
    }
    
    public void registrieren(Beobachter b){
        beobachter = b; }
    
    public void abmelden(Beobachter b){
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
