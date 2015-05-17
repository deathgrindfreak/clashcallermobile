package io.deathgrindfreak.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jcbell on 4/18/2015.
 */
public class Clan implements Parcelable {
    private General general;
    private ArrayList<ClanMember> calls;
    private ArrayList<Target> targets;
    private String[] log;

    private Clan(Parcel in) {
        general = in.readParcelable(General.class.getClassLoader());

        int cSize = in.readInt();
        ClanMember[] c = new ClanMember[cSize];
        in.readTypedArray(c, ClanMember.CREATOR);
        calls = new ArrayList<>(Arrays.asList(c));

        int tSize = in.readInt();
        Target[] t = new Target[tSize];
        in.readTypedArray(t, Target.CREATOR);
        targets = new ArrayList<>(Arrays.asList(t));

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

        ClanMember[] c = new ClanMember[calls.size()];
        dest.writeInt(calls.size());
        dest.writeTypedArray(calls.toArray(c), flags);

        Target[] t = new Target[targets.size()];
        dest.writeInt(targets.size());
        dest.writeTypedArray(targets.toArray(t), flags);

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

        for (Target tg : targets) {
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

    public void addClanMember(ClanMember member) {
        calls.add(member);
    }

    public void removeClanMember(ClanMember member) {
        calls.remove(member);
    }

    public General getGeneral() {
        return general;
    }

    public void setGeneral(General general) {
        this.general = general;
    }

    public ArrayList<ClanMember> getCalls() { return calls; }

    public void setCalls(ArrayList<ClanMember> calls) {
        this.calls = calls;
    }

    public ArrayList<Target> getTargets() {
        return targets;
    }

    public void setTargets(ArrayList<Target> targets) {
        this.targets = targets;
    }

    public String[] getLog() {
        return log;
    }

    public void setLog(String[] log) {
        this.log = log;
    }
}
