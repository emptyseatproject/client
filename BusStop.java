package com.example.splendor.bustest;

/**
 * Created by Splendor on 2017-03-27.
 */

public class BusStop {

    public String sID;
    public String sName;
    public String sPassable;
    public String sPredictable;
    public int sIndex;

    public String getsID(){
        return this.sID;
    }
    public String getsName(){
        return  this.sName;
    }
    public String getsPassable(){
        return this.sPassable;
    }
    public String getsPredictable(){return this.sPredictable;}
    public int getsIndex(){
        return  this.sIndex;
    }
}
