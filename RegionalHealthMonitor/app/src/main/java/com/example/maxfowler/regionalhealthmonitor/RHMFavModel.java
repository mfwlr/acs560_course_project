package com.example.maxfowler.regionalhealthmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;

public class RHMFavModel{
    private String stateName;
    private String countyName;
    private double lat;
    private double lon;
    private String cancerType;

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setCancerType(String cancerType) {
        this.cancerType = cancerType;
    }

    public String getStateName() {
        return stateName;
    }

    public String getCountyName() {
        return countyName;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getCancerType() {
        return cancerType;
    }
}
