/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc;
import java.util.LinkedList;
import java.util.UUID;
import mvc.shared.Aufgabe;
import mvc.shared.Fach;
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
    private LinkedList<Aufgabe> aufgaben;
    private LinkedList<Fach> fächer;
    // Konstruktor
    private Wert(){
        zahl = 0;
        beobachter = new LinkedList<Beobachter>();
        aufgaben = new LinkedList<Aufgabe>();
        fächer = new LinkedList<Fach>();
    }
    // Ausgabe für Singleton
    static Wert geben(){
        return w;
    }
    // Methoden aus Model
    public int gibZahl(){ return zahl; }

    public void aufgabeHinzufügen(Aufgabe a){
        aufgaben.add(a);
    }

    public Aufgabe[] aufgabenZurückgeben(){
        return aufgaben.toArray(new Aufgabe[aufgaben.size()]);
    }

    public boolean aufgabenStatusÄndern(UUID id){
        return false;
    }

    public Fach[] fächerZurückgeben(){
        return fächer.toArray(new Fach[fächer.size()]);
    }

    public void fachHinzufügen(Fach f){
        fächer.add(f);
    }
    
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
