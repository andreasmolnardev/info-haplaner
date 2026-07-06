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
        Wert.geben().fachHinzufügen(new Fach("Englisch", "English"));
        Wert.geben().aufgabeHinzufügen(new Aufgabe(Wert.geben().fächerZurückgeben()[0], "Arbeitsblatt bearbeiten", new Date()));
        view = new View(this);
        view.setVisible(true);
        
    }
    //Methoden aus Interface
    public void fachHinzufügen(String name, String kürzel){
        if (kürzel != null && kürzel.trim().length() > 0) {
            String fachName = name == null || name.trim().length() == 0 ? kürzel.trim() : name.trim();
            Wert.geben().fachHinzufügen(new Fach(fachName, kürzel.trim()));
        }
    }

    public void aufgabeHinzufügen(UUID fach, String titel, Date ablaufdatum){
        if (titel == null || titel.trim().length() == 0) {
            return;
        }
        Fach gefundenesFach = null;
        for (Fach f : Wert.geben().fächerZurückgeben()) {
            if (f.gibId().equals(fach)) {
                gefundenesFach = f;
                break;
            }
        }
        if (gefundenesFach != null) {
            Wert.geben().aufgabeHinzufügen(new Aufgabe(gefundenesFach, titel.trim(), ablaufdatum));
        }
    }

    public void statusÄndernButtonGedrueckt(UUID id){
        Wert.geben().aufgabenStatusÄndern(id);
    }
}
