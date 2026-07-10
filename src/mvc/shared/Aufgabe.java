package mvc.shared;

import java.util.Date;
import java.util.UUID;

public class Aufgabe{
    UUID id;
    Fach fach;
    Date erstelldatum;
    Date ablaufdatum;
    String titel;
    boolean status;

    public Aufgabe() {
    }

    public Aufgabe(Fach fach, String titel, Date ablaufdatum) {
        this.id = UUID.randomUUID();
        this.fach = fach;
        this.titel = titel;
        this.ablaufdatum = ablaufdatum;
        this.erstelldatum = new Date();
        this.status = false;
    }

    public UUID gibId() {
        return id;
    }

    public Fach gibFach() {
        return fach;
    }

    public Date gibAblaufdatum() {
        return ablaufdatum;
    }

    public String gibTitel() {
        return titel;
    }

    public boolean istErledigt() {
        return status;
    }

    public void statusUmschalten() {
        status = !status;
    }
}
