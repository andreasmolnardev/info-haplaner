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

    /**
     * Neue Aufgabe ohne id (wird beim Einfügen von {@link AufgabeDAO} vergeben).
     * Das Erstelldatum wird auf jetzt gesetzt, der Status auf offen (false).
     */
    public Aufgabe(Fach fach, String titel, Date ablaufdatum) {
        this.fach = fach;
        this.titel = titel;
        this.ablaufdatum = ablaufdatum;
        this.erstelldatum = new Date();
        this.status = false;
    }
}
