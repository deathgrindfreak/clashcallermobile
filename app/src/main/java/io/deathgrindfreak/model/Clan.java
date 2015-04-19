package io.deathgrindfreak.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by jcbell on 4/18/2015.
 */
public class Clan implements Parcelable {
    private General general;
    private ClanMember[] calls;
    private Targets[] targets;
    private String[] log;

    private Clan(Parcel in) {
        general = in.readParcelable(General.class.getClassLoader());

        int cSize = in.readInt();
        calls = new ClanMember[cSize];
        in.readTypedArray(calls, ClanMember.CREATOR);

        int tSize = in.readInt();
        targets = new Targets[tSize];
        in.readTypedArray(targets, Targets.CREATOR);

        int lSize = in.readInt();
        log = new String[lSize];
        in.readStringArray(log);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(general, flags);
        dest.writeInt(calls.length);
        dest.writeTypedArray(calls, flags);
        dest.writeInt(targets.length);
        dest.writeTypedArray(targets, flags);
        dest.writeInt(log.length);
        dest.writeStringArray(log);
    }

    public static final Parcelable.Creator<Clan> CREATOR = new Creator<Clan>() {

        @Override
        public Clan createFromParcel(Parcel source) {
            return new Clan(source);
        }

        @Override
        public Clan[] newArray(int size) {
            return new Clan[size];
        }
    };

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        ret.append(general.toString());

        for (ClanMember cm : calls) {
            ret.append(cm);
            ret.append("\n");
        }

        for (Targets tg : targets) {
            ret.append(tg);
            ret.append("\n");
        }

        ret.append("log: {\n");
        for (String l : log) {
            ret.append(l);
            ret.append("\n");
        }
        ret.append("}\n");

        return ret.toString();
    }

    public General getGeneral() {
        return general;
    }

    public void setGeneral(General general) {
        this.general = general;
    }

    public ClanMember[] getCalls() {
        return calls;
    }

    public void setCalls(ClanMember[] calls) {
        this.calls = calls;
    }

    public Targets[] getTargets() {
        return targets;
    }

    public void setTargets(Targets[] targets) {
        this.targets = targets;
    }

    public String[] getLog() {
        return log;
    }

    public void setLog(String[] log) {
        this.log = log;
    }
}
