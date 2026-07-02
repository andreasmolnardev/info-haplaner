/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;
import java.util.LinkedList;
/**
 *
 * @author So
 */
public class Wert implements Model{
    //ToDo: Implementiere das Model als Singleton
    // Attribute
    private static Wert w = new Wert();
    private int zahl;
    private LinkedList<Beobachter> beobachter;
    // Konstruktor
    private Wert(){
        zahl = 0;
        beobachter = new LinkedList<Beobachter>();
    }
    // Ausgabe für Singleton
    static Wert geben(){
        return w;
    }
    // Methoden aus Model
    public int gibZahl(){ return zahl; }
    
    public void registrieren(Beobachter b){
        beobachter.add(b); }
    
    public void abmelden(Beobachter b){
        beobachter.remove(b);  }
    
    // Methode für Beobachter
    public void zahlErhoehen(){
        zahl++;
        for(Beobachter b :beobachter){
            b.zahlGeaendert(); }
    }
}
