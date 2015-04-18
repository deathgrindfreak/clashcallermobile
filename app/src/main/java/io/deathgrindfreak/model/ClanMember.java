package io.deathgrindfreak.model;

import java.util.Date;

/**
 * Created by jcbell on 4/18/2015.
 */
public class ClanMember {
    private int posy;
    private int posx;
    private int stars;
    private String playername;
    private Date calltime;
    private Date updatetime;
    private String note;
    private String last;

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
