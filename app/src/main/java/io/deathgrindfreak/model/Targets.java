package io.deathgrindfreak.model;

/**
 * Created by jcbell on 4/18/2015.
 */
public class Targets {
    private int position;
    private String name;
    private String note;

    @Override
    public String toString() {
        return "target: {\n" +
                "\tposition: " + position + "\n" +
                "\tname: " + (name == null ? "null" : name) + "\n" +
                "\tnote: " + (note == null ? "null" : note) + "\n" +
                "}\n";
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
