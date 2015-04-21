package io.deathgrindfreak.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by jcbell on 4/18/2015.
 */
public class ClanMember implements Parcelable, Comparable<ClanMember> {
    private int posy;
    private int posx;
    private int stars;
    private String playername;
    private Date calltime;
    private Date updatetime;
    private String note;
    private String last;

    private ClanMember(Parcel in) {
        posy = in.readInt();
        posx = in.readInt();
        stars = in.readInt();
        playername = in.readString();

        // TODO replace serializable with long version
        calltime = (Date) in.readSerializable();
        updatetime = (Date) in.readSerializable();
        note = in.readString();
        last = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(posy);
        dest.writeInt(posx);
        dest.writeInt(stars);
        dest.writeString(playername);
        dest.writeSerializable(calltime);
        dest.writeSerializable(updatetime);
        dest.writeString(note);
        dest.writeString(last);
    }

    public static final Creator<ClanMember> CREATOR = new Creator<ClanMember>() {
        @Override
        public ClanMember createFromParcel(Parcel source) {
            return new ClanMember(source);
        }

        @Override
        public ClanMember[] newArray(int size) {
            return new ClanMember[size];
        }
    };


    @Override
    public int compareTo(ClanMember other) {
        if (this.posy != other.posy) {
            return Integer.compare(this.posy, other.posy);
        } else {
            return Integer.compare(this.posx, other.posx);
        }
    }

    @Override
    public String toString() {
        return "clan member: {\n" +
                "\tposy: " + posy + "\n" +
                "\tposx: " + posx + "\n" +
                "\tstars: " + stars + "\n" +
                "\tplayername: " + playername + "\n" +
                "\tcalltime: " + calltime + "\n" +
                "\tupdatetime: " + updatetime + "\n" +
                "\tnote: " + (note == null ? "null" : note) + "\n" +
                "\tlast: " + last + "\n" +
                "}\n";
    }

    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }

    public int getPosx() {
        return posx;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public Date getCalltime() {
        return calltime;
    }

    public void setCalltime(Date calltime) {
        this.calltime = calltime;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
}
