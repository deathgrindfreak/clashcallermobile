package io.deathgrindfreak.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by jcbell on 4/18/2015.
 */
public class General implements Parcelable {
    private String type;
    private int maxcol;
    private Date checktime;
    private String clanname;
    private String enemyname;
    private String warcode;
    private int size;
    private Date starttime;
    private String clanmessage;
    private int timerlength;
    private Date updatetime;

    private General(Parcel in) {
        type = in.readString();
        maxcol = in.readInt();
        checktime = (Date) in.readSerializable();
        clanname = in.readString();
        enemyname = in.readString();
        warcode = in.readString();
        size = in.readInt();
        starttime = (Date) in.readSerializable();
        clanmessage = in.readString();
        timerlength = in.readInt();
        updatetime = (Date) in.readSerializable();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeInt(maxcol);
        dest.writeSerializable(checktime);
        dest.writeString(clanname);
        dest.writeString(enemyname);
        dest.writeString(warcode);
        dest.writeInt(size);
        dest.writeSerializable(starttime);
        dest.writeString(clanmessage);
        dest.writeInt(timerlength);
        dest.writeSerializable(updatetime);
    }

    public static final Creator<General> CREATOR = new Creator<General>() {
        @Override
        public General createFromParcel(Parcel source) {
            return new General(source);
        }

        @Override
        public General[] newArray(int size) {
            return new General[size];
        }
    };

    @Override
    public String toString() {
        return "general: {\n" +
                "\tmaxcol: " + maxcol + "\n" +
                "\tchecktime: " + checktime + "\n" +
                "\tclanname: " + clanname + "\n" +
                "\tenemyname: " + enemyname + "\n" +
                "\twarcode: " + warcode + "\n" +
                "\tsize: " + size + "\n" +
                "\tstarttime: " + starttime + "\n" +
                "\tclanmessage: " + clanmessage + "\n" +
                "\ttimerlength: " + timerlength + "\n" +
                "\tupdatetime: " + updatetime + "\n" +
                "}\n";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaxcol() {
        return maxcol;
    }

    public void setMaxcol(int maxcol) {
        this.maxcol = maxcol;
    }

    public Date getChecktime() {
        return checktime;
    }

    public void setChecktime(Date checktime) {
        this.checktime = checktime;
    }

    public String getClanname() {
        return clanname;
    }

    public void setClanname(String clanname) {
        this.clanname = clanname;
    }

    public String getEnemyname() {
        return enemyname;
    }

    public void setEnemyname(String enemyname) {
        this.enemyname = enemyname;
    }

    public String getWarcode() {
        return warcode;
    }

    public void setWarcode(String warcode) {
        this.warcode = warcode;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public String getClanmessage() {
        return clanmessage;
    }

    public void setClanmessage(String clanmessage) {
        this.clanmessage = clanmessage;
    }

    public int getTimerlength() {
        return timerlength;
    }

    public void setTimerlength(int timerlength) {
        this.timerlength = timerlength;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}
