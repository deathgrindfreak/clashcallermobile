package io.deathgrindfreak.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jcbell on 4/18/2015.
 */
public class Targets implements Parcelable {
    private int position;
    private String name;
    private String note;

    private Targets(Parcel in) {
        position = in.readInt();
        name = in.readString();
        note = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeString(name);
        dest.writeString(note);
    }

    public static final Creator<Targets> CREATOR = new Creator<Targets>() {
        @Override
        public Targets createFromParcel(Parcel source) {
            return new Targets(source);
        }

        @Override
        public Targets[] newArray(int size) {
            return new Targets[size];
        }
    };

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
