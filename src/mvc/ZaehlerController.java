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
    public void plusButtonGedrueckt(){
        Wert.geben().zahlErhoehen();
    }
    public void umschaltButtonGedrueckt(){
        enabled = !enabled;
        view.setzeEnabled(enabled);
    }
}
