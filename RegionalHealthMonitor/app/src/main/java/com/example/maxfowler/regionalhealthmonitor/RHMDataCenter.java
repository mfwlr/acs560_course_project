package com.example.maxfowler.regionalhealthmonitor;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;

import android.os.StrictMode;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;

import java.util.Locale;
import java.util.List;
import java.io.IOException;

import android.location.Geocoder;
import android.location.Address;

import android.content.Context;

import java.util.HashMap;

public class RHMDataCenter {

    public static HashMap<String, Integer> stateLookUp = null;

    public static void initStateLookUp(){
        HashMap<String, Integer> temp = new HashMap();
        temp.put("Alabama", 1);
        temp.put("Alaska", 2);
        temp.put("Arizona", 4);
        temp.put("Arkansas", 5);
        temp.put("California", 6);
        temp.put("Colorado", 8);
        temp.put("Connecticut", 9);
        temp.put("Delware", 10);
        temp.put("District of Columbia", 11);
        temp.put("Florida", 12);
        temp.put("Georgia", 13);
        temp.put("Hawaii", 15);
        temp.put("Idaho", 16);
        temp.put("Illinois", 17);
        temp.put("Indiana", 18);
        temp.put("Ohio", 39);
        temp.put("Michigan", 26);

        stateLookUp = temp;

    }


    public static JSONObject getCancerData(String cn, int ct, int sc){
        enableStrictMode();

        //http://ws.instrumentsafe.com/r/18/40/allen

        HttpGet httpGet = new HttpGet("http://ws.instrumentsafe.com/r/"+sc+"/"+ct +"/"+cn);

        return httpToJson(httpGet);
    }

    public static JSONObject getLocationInfo(double lat, double lng) {

        enableStrictMode();

        HttpGet httpGet = new HttpGet("http://maps.googleapis.com/maps/api/geocode/json?latlng="+ lat+","+lng +"&sensor=true");

        return httpToJson(httpGet);
    }

    public static JSONObject httpToJson(HttpGet httpGet){
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static String getCounty(double lat, double lng){
        JSONObject jsn = getLocationInfo(lat, lng);

        try{
            JSONArray res = jsn.getJSONArray("results");
            JSONObject r = res.getJSONObject(0);
            JSONArray typesArray = r.getJSONArray("address_components");
            JSONObject r2 = typesArray.getJSONObject(5);
            String types = r2.getString("long_name");
            String regex = "\\s*\\bCounty\\b\\s*";
            types = types.replaceAll(regex,"");
            return types;
        } catch(JSONException e){
           System.out.println("bAD MOVE!!");
        }

        return null;
    }

    public static void enableStrictMode(){
        //Sloppy -> convert to Async task before final ideally
        StrictMode.ThreadPolicy policy  = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public static String getState(double lat, double lng, Context base){
        //Geocoder practice -> did not give me the county name :(
       Geocoder gcd = new Geocoder(base, Locale.getDefault());
        List<Address> addresses;
        String stateName = null;

        try {
            addresses = gcd.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0)
                System.out.println(addresses.get(0).getLocality());
           stateName = addresses.get(0).getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stateName;
    }

    public static RHMPointData makeRPD(String countyName, String cancerName,  int cancerType, String stateName){

        int stateCode = (Integer) stateLookUp.get(stateName);

        //DRY HERE - make a JSON handler that generically handles all disparate get requests

        JSONObject res = getCancerData(countyName, cancerType, stateCode);

        try {
            String incidentRate = (String) res.getString("user_incidence_rate");
            Integer starRank = (Integer) res.getInt("state_star_ranking");
            System.out.println("Yeees: " + incidentRate + "   " + starRank);
            return new RHMPointData(Double.parseDouble(incidentRate), starRank, countyName, cancerName);
        }catch(JSONException e){
            System.out.println("Bad JSON - ignore and move on with your life");
        }
        return null;
    }
}
