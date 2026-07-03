/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.util.UUID;
import mvc.shared.Aufgabe;

public interface Controller {
    void fachHinzufügen(String name, String kürzel);
    
    void aufgabeHinzufügen(Aufgabe a);
    void aufgabenStatusÄndern(UUID id);

    void plusButtonGedrueckt();
    void umschaltButtonGedrueckt();
}
