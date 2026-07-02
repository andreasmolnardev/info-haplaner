/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

public interface Model {
    void aufgabeHinzufügen(aufgabe a);
    aufgabe[] aufgabenZurückgeben();
    boolean aufgabenStatusÄndern(UUID id);

    fach[] fächerZurückgeben();
    void fachHinzufügen(fach f);

    void registrieren(Beobachter b);
    void abmelden(Beobachter b);
}
