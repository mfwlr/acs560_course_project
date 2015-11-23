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

    public RHMPointData(double incidentRate, int starRank, String countyName, String cancerName){
        incRate = incidentRate;
        this.starRank = starRank;
        this.countyName = countyName;
        this.cancerName = cancerName;

        if(starRank == 1){
            severity = "Most severe";
        } else if(starRank == 2){
            severity = "Fairly severe";
        } else if(starRank == 3){
            severity = "Not severe";
        } else{
            severity = "Negligible";
        }
    }

    public String buildTitle(){
        return "County: " + countyName;
    }

    public String buildSnippet(){
        return cancerName + ": " + incRate +"\nSeverity: " + severity;
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
}
