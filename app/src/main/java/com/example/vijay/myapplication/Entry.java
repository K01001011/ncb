package com.example.vijay.myapplication;

import android.location.LocationListener;
import android.location.LocationManager;

public class Entry {
    private String location;
    private String id0;
    private String id1;


    public Entry(String name1,String name2,String lat,String lon){
        id0 = name1;
        id1 = name2;
        this.location = "Latitude = " + lat + " Longitude = " +lon;
    }

    public String getId0() {
        return id0;

    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId1() {
        return id1;
    }

    public void setId0(String id0) {
        this.id0 = id0;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

}
