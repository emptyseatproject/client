package com.example.splendor.bustest;

/**
 * Created by Splendor on 2017-05-26.
 */

public class RunningBus {
    public String routeid;
    public String stationid;
    public String pno;
    public int seat;
    public int sta_order;
    public String route;

    public String toString(){
        String str = null;
        str = routeid + " | "
                + stationid  + " | "
                + pno  + " | "
                + seat  + " | "
                + sta_order  + " | "
                + route;
        return str;
    }
}
