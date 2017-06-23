package com.example.splendor.bustest;

/**
 * Created by Splendor on 2017-04-10.
 */

public class BusInfo extends Bus {
    public String busArrivalTime;
    public String busPrev;
    public String busEmptySeat;
    public String busExpectedEmptySeat;

    BusInfo(String busArrivalTime, String busPrev, String busEmptySeat, String busExpectedEmptySeat){
        this.busArrivalTime = busArrivalTime;
        this.busPrev = busPrev;
        this.busEmptySeat = busEmptySeat;
        this.busExpectedEmptySeat = busExpectedEmptySeat;
    }

    public String getBusArrivalTime(){
        return this.busArrivalTime;
    }
    public String getBusPrev(){
        return this.busPrev;
    }
    public String getBusEmptySeat(){

        return this.busEmptySeat;
    }
    public String getBusExpectedEmptySeat(){
        return this.busExpectedEmptySeat;
    }
}
