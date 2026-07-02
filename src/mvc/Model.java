/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;

/**
 *
 * @author So
 */
public interface Model {
    int gibZahl();
    void registrieren(Beobachter b);
    void abmelden(Beobachter b);
}
