package mvc.shared;

import java.util.UUID;

public class Fach{
    public UUID id;
    public String kürzel;
    public String name;

    public Fach() {
    }

    public Fach(String name, String kürzel) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.kürzel = kürzel;
    }

    public UUID gibId() {
        return id;
    }

    public UUID getId() {
        return id;
    }

    public String gibKürzel() {
        return kürzel;
    }

    public String gibName() {
        return name;
    }

    @Override
    public String toString() {
        return kürzel;
    }
}
