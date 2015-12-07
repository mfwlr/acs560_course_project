package com.example.maxfowler.regionalhealthmonitor;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

/**
 * Created by maxfowler on 11/23/15.
 */
public class RHMPointData {

    private int starRank;
    private String severity;
    private double incRate;
    private String countyName;
    private String cancerName;
    private String stateName;
    private double lat;
    private double lon;
    private boolean isFav;

    public RHMPointData(double incidentRate, int starRank, String countyName, String stateName, String cancerName, double lat, double lon){
        incRate = incidentRate;
        this.starRank = starRank;
        this.countyName = countyName;
        this.cancerName = cancerName;
        this.stateName = stateName;

        if(starRank == 1){
            severity = "Most severe";
        } else if(starRank == 2){
            severity = "Fairly severe";
        } else if(starRank == 3){
            severity = "Not severe";
        } else{
            severity = "Negligible";
        }

        this.lat = lat;
        this.lon = lon;

    }

    public void determineFavorite(String email, String pwd){
        isFav = RHMDataCenter.determineFav(countyName, email, pwd);
    }

    public String buildTitle(){
        return "County: " + countyName;
    }

    public String buildSnippet(){
        String ret = cancerName + ": " + incRate +"\nSeverity: " + severity +"\tFavorite: ";
        if(isFav){
            return ret + "Y";
        }
        return ret + "N";
    }

    public float severityHue(){
        if(starRank == 1){
            return BitmapDescriptorFactory.HUE_RED;
        }
        else if(starRank == 2){
            return BitmapDescriptorFactory.HUE_ORANGE;
        }
        else if(starRank == 3){
            return BitmapDescriptorFactory.HUE_YELLOW;
        }
        else{
            return BitmapDescriptorFactory.HUE_GREEN;
        }
    }

    public void addFavorite(String name, String pass){
        if(!isFav) {
            RHMDataCenter.addFavorite(name, pass, cancerName, lat, lon, countyName, stateName);
            isFav = true;
        }
    }

    public double getLatitude(){
        return lat;
    }
    public double getLongitude(){
        return lon;
    }
}
