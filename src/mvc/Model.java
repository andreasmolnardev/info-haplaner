/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

import java.util.UUID;
import java.util.Date;
import mvc.shared.Aufgabe;
import mvc.shared.Fach;

public interface Model {
    void aufgabeHinzufügen(Aufgabe a);
    Aufgabe[] aufgabenZurückgeben();
    Aufgabe[] aufgabenNachFälligkeitsdatumZurückgeben(Date datum);
    boolean aufgabenStatusÄndern(UUID id);

    Fach[] fächerZurückgeben();
    void fachHinzufügen(Fach f);

    void registrieren(Beobachter b);
    void abmelden(Beobachter b);
}
