/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.util.UUID;

import java.util.Date;

public interface Controller {
    void fachHinzufügen(String name, String kürzel);
    void aufgabeHinzufügen(UUID fach, String titel, Date ablaufdatum);

    void plusButtonGedrueckt();
    void statusÄndernButtonGedrueckt(UUID aufgabe);
    
    Aufgabe[] aufgabenZurückgeben();
    Aufgabe[] aufgabenNachDatumSortiertZurückgeben();
}
