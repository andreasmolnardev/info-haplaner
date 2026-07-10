/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.util.Date;
import java.util.UUID;
import mvc.shared.Aufgabe;
import mvc.shared.Fach;

/**
 *
 * @author So
 */
public class ZaehlerController implements Controller{
    // Attribute
    private View view;
    // Konstruktor
    public ZaehlerController(){
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
    public void plusButtonGedrueckt(){
        // UI uses direct form actions.
    }

    @Override
    public void umschaltButtonGedrueckt(){
        // UI no longer toggles counter view.
    }

    @Override
    public Aufgabe[] aufgabenZurückgeben() {
        return Wert.geben().aufgabenZurückgeben();
    }

    public void aufgabeHinzufügen(Aufgabe a){
        Wert.geben().aufgabeHinzufügen(a);
    }

}
