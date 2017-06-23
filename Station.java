package com.example.splendor.bustest;

/**
 * Created by Splendor on 2017-05-28.
 */

public class Station {
    private String route;

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    private String routeid;
    private String stationid;
    private String pno1;
    private int seat1;
    private int locno1;
    private int pred1;
    private String round_pred1;
    private String pno2;
    private int seat2;
    private int locno2;
    private int pred2;
    private String round_pred2;

    public String getRound_pred1() {
        return round_pred1;
    }

    public void setRound_pred1(String round_pred1) {
        this.round_pred1 = round_pred1;
    }

    public String getRound_pred2() {
        return round_pred2;
    }

    public void setRound_pred2(String round_pred2) {
        this.round_pred2 = round_pred2;
    }

    public String getRouteid() {
        return routeid;
    }

    public void setRouteid(String routeid) {
        this.routeid = routeid;
    }

    public String getStationid() {
        return stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid;
    }

    public String getPno1() {
        return pno1;
    }

    public void setPno1(String pno1) {
        this.pno1 = pno1;
    }

    public int getSeat1() {
        return seat1;
    }

    public void setSeat1(int seat1) {
        this.seat1 = seat1;
    }

    public int getLocno1() {
        return locno1;
    }

    public void setLocno1(int locno1) {
        this.locno1 = locno1;
    }

    public int getPred1() {
        return pred1;
    }

    public void setPred1(int pred1) {
        this.pred1 = pred1;
    }

    public String getPno2() {
        return pno2;
    }

    public void setPno2(String pno2) {
        this.pno2 = pno2;
    }

    public int getSeat2() {
        return seat2;
    }

    public void setSeat2(int seat2) {
        this.seat2 = seat2;
    }

    public int getLocno2() {
        return locno2;
    }

    public void setLocno2(int locno2) {
        this.locno2 = locno2;
    }

    public int getPred2() {
        return pred2;
    }

    public void setPred2(int pred2) {
        this.pred2 = pred2;
    }

    public String toString(){
        String str = null;

        str =  routeid + " | "
                + stationid  + " | "
                + pno1  + " | "
                + seat1  + " | "
                + locno1  + " | "
                + pred1;

        if(pno2!=null) {
            str += routeid + " | "
                    + stationid + " | "
                    + pno1 + " | "
                    + seat1 + " | "
                    + locno1 + " | "
                    + pred1 + " | "
                    + pno2 + " | "
                    + seat2 + " | "
                    + locno2 + " | "
                    + pred2;
        }
        return str;
    }
}
