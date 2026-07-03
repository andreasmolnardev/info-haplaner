/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.util.UUID;
import mvc.shared.Aufgabe;

/**
 *
 * @author So
 */
public class ZaehlerController implements Controller{
    // Attribute
    private View view;
    private boolean enabled;
    // Konstruktor
    public ZaehlerController(){
        enabled = true;
        view = new View(this);
        view.setVisible(true);
        
    }
    //Methoden aus Interface
    public void fachHinzufügen(String name, String kürzel){
    }

    public void aufgabeHinzufügen(Aufgabe a){
        Wert.geben().aufgabeHinzufügen(a);
    }

    public void aufgabenStatusÄndern(UUID id){
        Wert.geben().aufgabenStatusÄndern(id);
    }

    public void plusButtonGedrueckt(){
        Wert.geben().zahlErhoehen();
    }
    public void umschaltButtonGedrueckt(){
        enabled = !enabled;
        view.setzeEnabled(enabled);
    }
}
