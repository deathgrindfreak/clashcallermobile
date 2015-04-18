package io.deathgrindfreak.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

/**
 * Created by jcbell on 4/18/2015.
 */
public class Clan implements Parcelable {
    private Map<String, String> general;
    private ClanMember[] calls;
    private Targets[] targets;
    private String[] log;

    private Clan(Parcel in) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Parcelable.Creator<Clan> CREATOR = new Creator<Clan>() {

        @Override
        public Clan createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Clan[] newArray(int size) {
            return new Clan[0];
        }
    };

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();

        ret.append("general: {\n");
        for (Map.Entry<String, String> entry : general.entrySet()) {
            ret.append("\t");
            ret.append(entry.getKey());
            ret.append(" = ");
            ret.append(entry.getValue());
            ret.append("\n");
        }
        ret.append("}");

        for (ClanMember cm : calls) {
            ret.append(cm);
            ret.append("\n");
        }

        for (Targets tg : targets) {
            ret.append(tg);
            ret.append("\n");
        }

        for (String l : log) {
            ret.append(l);
            ret.append("\n");
        }

        return ret.toString();
    }

    public Map<String, String> getGeneral() {
        return general;
    }

    public void setGeneral(Map<String, String> general) {
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
