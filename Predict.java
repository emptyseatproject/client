package com.example.splendor.bustest;

/**
 * Created by Splendor on 2017-05-28.
 */

public class Predict {
    private String route;
    private String stationid;
    private String ten_minutes;
    private int pred_empty_seat;

    public Predict(String route, String stationid, String ten_minutes, int pred_empty_seat) {
        this.route = route;
        this.stationid = stationid;
        this.ten_minutes = ten_minutes;
        this.pred_empty_seat = pred_empty_seat;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getStationid() {
        return stationid;
    }

    public void setStationid(String stationid) {
        this.stationid = stationid;
    }

    public String getTen_minutes() {
        return ten_minutes;
    }

    public void setTen_minutes(String ten_minutes) {
        this.ten_minutes = ten_minutes;
    }

    public int getPred_empty_seat() {
        return pred_empty_seat;
    }

    public void setPred_empty_seat(int pred_empty_seat) {
        this.pred_empty_seat = pred_empty_seat;
    }
}
