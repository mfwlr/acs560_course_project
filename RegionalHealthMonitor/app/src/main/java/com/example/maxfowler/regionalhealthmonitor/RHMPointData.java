package com.example.maxfowler.regionalhealthmonitor;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;


/**
 * RHMPointData stores the information about a given map point
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

    /**
     * Make a new map point
     * @param incidentRate
     * @param starRank
     * @param countyName
     * @param stateName
     * @param cancerName
     * @param lat
     * @param lon
     */
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

    /**
     * Determine if a given map point is a favorite or not
     * @param email
     * @param pwd
     */
    public void determineFavorite(String email, String pwd){
        isFav = RHMDataCenter.determineFav(countyName, email, pwd);
    }

    /**
     * Build the title for a map point
     * @return
     */
    public String buildTitle(){
        return "County: " + countyName;
    }

    /**
     * Build the info snippet for a map point
     * @return
     */
    public String buildSnippet(){
        String ret = cancerName + ": " + incRate +"\nSeverity: " + severity +"\tFavorite: ";
        if(isFav){
            return ret + "Y";
        }
        return ret + "N";
    }

    /**
     * Set the color hue for a map point
     * @return
     */
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

    /**
     * Add a map point as a favorite
     * @param name
     * @param pass
     */
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
