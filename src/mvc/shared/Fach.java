package mvc.shared;

import java.util.UUID;

public class Fach{
    UUID id;
    String kürzel;
    String name;

    public Fach() {
    }

    /** Neues Fach ohne id; die id wird beim Einfügen von {@link FachDAO} vergeben. */
    public Fach(String name, String kürzel) {
        this.name = name;
        this.kürzel = kürzel;
    }

    /** Erlaubt der Controller-Schicht (Package mvc) das Lesen der id. */
    public UUID getId() {
        return id;
    }
    public UUID id;
    public String kürzel;
    public String name;
}
