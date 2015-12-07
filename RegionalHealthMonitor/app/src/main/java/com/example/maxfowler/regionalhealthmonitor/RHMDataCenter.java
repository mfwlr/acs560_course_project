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
import java.util.ArrayList;

import android.location.Geocoder;
import android.location.Address;

import android.content.Context;


import java.util.HashMap;
import java.util.Map;

/**
 * RHMDataCenter is a utility class full of statics that acts as the midpoint between
 * the client side and the server side
 */
public class RHMDataCenter {

    public static HashMap<String, Integer> stateLookUp = null;
    public static HashMap<String, Integer> cancerLookUp = null;

    /**
     * A convenient, although awful, way to initialize a list of state to int code lookups
     * and cancer to int code lookups.
     * Forgive the poor design
     */
    public static void initLookUpTables(){
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
        temp.put("Iowa", 19);
        temp.put("Kansas", 20);
        temp.put("Kentucky", 21);
        temp.put("Louisiana", 22);
        temp.put("Maine", 23);
        temp.put("Maryland", 24);
        temp.put("Massachusetts",25);
        temp.put("Michigan", 26);
        temp.put("Minnesota",27);
        temp.put("Mississippi",28);
        temp.put("Missouri", 29);
        temp.put("Montana",30);
        temp.put("Nebraska", 31);
        temp.put("Nevada", 32);
        temp.put("New Hampshire", 33);
        temp.put("New Jersey", 34);
        temp.put("New Mexico", 35);
        temp.put("New York", 36);
        temp.put("North Carolina", 37);
        temp.put("North Dakota", 38);
        temp.put("Ohio", 39);
        temp.put("Oklahoma", 40);
        temp.put("Oregon", 41);
        temp.put("Pennsylvania",42);
        temp.put("Rhode Island", 44);
        temp.put("South Carolina", 45);
        temp.put("South Dakota", 46);
        temp.put("Tennessee", 47);
        temp.put("Texas", 48);
        temp.put("Utah", 49);
        temp.put("Vermont", 50);
        temp.put("Virginia", 51);
        temp.put("Washington", 53);
        temp.put("West Virginia", 54);
        temp.put("Wisconsin", 55);
        temp.put("Wyoming", 56);



        stateLookUp = temp;

        HashMap<String, Integer> temp2 = new HashMap();
        temp2.put("Brain",76);
        temp2.put("Breast Cancer", 55);
        temp2.put("Lung", 47);
        temp2.put("Cervix", 57);
        temp2.put("Colon and Rectum", 20);
        temp2.put("Esophagus",17);
        temp2.put("Kidney and Pelvis",72);
        temp2.put("Leukemia", 90);
        temp2.put("Pancreas",40);

        cancerLookUp = temp2;


    }

    /**
     * loginResults returns the success or failure of a user's login.
     * @param email
     * @param pwd
     * @return
     */
    public static boolean loginResults(String email, String pwd){
        //http://ws.instrumentsafe.com/l/castonzo@gmail.com/foo
        enableStrictMode();
        HttpGet httpGet = new HttpGet("http://ws.instrumentsafe.com/l/"+email+"/"+pwd);
        System.out.println("http://ws.instrumentsafe.com/l/" + email + "/" + pwd);
        JSONObject res = httpToJson(httpGet);
        try {
            if (res.getInt("status") != 1) {
                return false;
            }
        }catch(JSONException e){
            return false;
        }
        return true;
    }

    /**
     * Get Cancer Data will obtain the data for a given county, cancer type, and state
     * @param cn
     * @param ct
     * @param sc
     * @return
     */
    public static JSONObject getCancerData(String cn, int ct, int sc){
        enableStrictMode();

        //http://ws.instrumentsafe.com/r/18/40/allen

        try {

            HttpGet httpGet = new HttpGet("http://ws.instrumentsafe.com/r/" + sc + "/" + ct + "/" + cn);
            System.out.println("http://ws.instrumentsafe.com/r/" + sc + "/" + ct + "/" + cn);

            return httpToJson(httpGet);
        }catch(IllegalArgumentException e){
            return null;
        }


    }

    /**
     * Get Location Info is how the application fetches the google maps
     * api information for a given latitude and longitude
     * @param lat
     * @param lng
     * @return
     */
    public static JSONObject getLocationInfo(double lat, double lng) {

        enableStrictMode();

        HttpGet httpGet = new HttpGet("http://maps.googleapis.com/maps/api/geocode/json?latlng="+ lat+","+lng +"&sensor=true");

        return httpToJson(httpGet);
    }

    /**
     * This method fetches the bookmarks for a given httpget string, that specifies the user whose
     * bookmarks we want
     * @param st
     * @return
     */
    private static JSONArray getBooks(String st){
        enableStrictMode();

        HttpGet httpGet = new HttpGet(st);

        return httpToJarray(httpGet);
    }

    /**
     * Determine favorite checks to see if a given county is favorited by a user
     * @param countyName
     * @param email
     * @param pwd
     * @return
     */
    public static boolean determineFav(String countyName, String email, String pwd){
        //http://ws.instrumentsafe.com/gb/get_bookmarks/wcastonzo5@comcast.net/foo

        JSONArray res = getBooks("http://ws.instrumentsafe.com/gb/get_bookmarks/"+email+"/"+pwd);
        try{
            for(int i = 0; i < res.length(); i++){
                if(res.getJSONObject(i).getString("county_name").toLowerCase().equals(countyName.toLowerCase())){
                    return true;
                }
            }
        }catch(JSONException e){
            System.out.println("JSON failed, internal error, ignore");
        }



        return false;
    }

    /**
     * prepBuilder preps the httpget and returns a stringbuilder for creating JSON
     * @param httpGet
     * @return
     */
    private static StringBuilder prepBuilder(HttpGet httpGet){
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

        return stringBuilder;
    }

    /**
     * This methods returns a JSON Array from an httpget
     * @param httpGet
     * @return
     */
    public static JSONArray httpToJarray(HttpGet httpGet){

        StringBuilder stringBuilder=prepBuilder(httpGet);
        JSONArray jsonObject = new JSONArray();
        try {
            jsonObject = new JSONArray(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * This method returns a JSON Object from an httpget
     * @param httpGet
     * @return
     */
    public static JSONObject httpToJson(HttpGet httpGet){
        StringBuilder stringBuilder=prepBuilder(httpGet);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * This method gets the county for a given latitude and longitude, leveraging the google maps API
     * through getLocationInfo
     * @param lat
     * @param lng
     * @return
     */
    public static String getCounty(double lat, double lng){
        JSONObject jsn = getLocationInfo(lat, lng);
        int selection = -1;
        String types = null;

        try{
            JSONArray res = jsn.getJSONArray("results");
            JSONObject r = res.getJSONObject(0);
            JSONArray typesArray = r.getJSONArray("address_components");
            for(int i = 0; i < typesArray.length(); i++){
                JSONObject intermed = typesArray.getJSONObject(i);
                JSONArray hold = intermed.getJSONArray("types");
                if(hold.get(0).equals("administrative_area_level_2")){
                    selection = i;
                    break;
                }

            }

            if(selection != -1) {
                JSONObject r2 = typesArray.getJSONObject(selection);
                types = r2.getString("long_name");
                String regex = "\\s*\\bCounty\\b\\s*";
                types = types.replaceAll(regex, "");

            }
        return types;
        } catch(JSONException e){
           System.out.println("JSON could not be parsed");
        }

        return null;
    }

    /**
     * enableStrictMode forces the main thread to deal with web stuff
     * This is not ideal, but it was much easier than dealing with Asynchronocity for tasks that
     * are needed before the app does anything else...
     */
    public static void enableStrictMode(){
        //Sloppy :(
        StrictMode.ThreadPolicy policy  = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /**
     * getState gets the state for a given latitude and longitude
     * @param lat
     * @param lng
     * @param base
     * @return
     */
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

    /**
     * This makes an RHMPointData to represent a given map pin
     * @param countyName
     * @param cancerName
     * @param cancerType
     * @param stateName
     * @param lat
     * @param lon
     * @return
     */
    public static RHMPointData makeRPD(String countyName, String cancerName,  int cancerType, String stateName, double lat, double lon){

        int stateCode = (Integer) stateLookUp.get(stateName);

        //DRY HERE - make a JSON handler that generically handles all disparate get requests

        JSONObject res = getCancerData(countyName, cancerType, stateCode);

        if(res == null){
            return null;
        }

        try {
            String incidentRate = (String) res.getString("user_incidence_rate");
            Integer starRank = (Integer) res.getInt("state_star_ranking");
            return new RHMPointData(Double.parseDouble(incidentRate), starRank, countyName,stateName,cancerName, lat, lon);
        }catch(JSONException e){
            System.out.println("Bad JSON - ignore and move on with your life");
        }
        catch(NumberFormatException e2){
            System.out.println("Failed to get incidence rate");
        }
        return null;
    }

    /**
     * This handles the httpget to add a new user
     * @param em
     * @param pwd
     */
    public static void addNewUser(String em, String pwd){
        //http://ws.instrumentsafe.com/u/add/wcastonzo2@comcast.net/foo
        enableStrictMode();
        HttpGet httpGet = new HttpGet("http://ws.instrumentsafe.com/u/add/"+em+"/"+pwd);
        System.out.println("http://ws.instrumentsafe.com/u/add/"+em+"/"+pwd);
        simpleExecute(httpGet);
    }

    /**
     * This handles the httpget to edit an existing user
     * @param em
     * @param pwd
     */
    public static void editExistingUser(String em, String pwd){
        enableStrictMode();
        HttpGet httpGet = new HttpGet("http://ws.instrumentsafe.com/u/update/"+em+"/"+pwd);
        System.out.println("http://ws.instrumentsafe.com/u/update/"+em+"/"+pwd);
        simpleExecute(httpGet);
    }

    /**
     * simpleExecute executes an http in a simple form, when more advanced execution is not needed
     * @param httpGet
     */
    private static void simpleExecute(HttpGet httpGet){
        HttpClient hc = new DefaultHttpClient();
        try{
            hc.execute(httpGet);
        }catch(Exception e){
            System.out.println("Internal error, ignore");
        }

    }

    /**
     * This method adds a given county to the logged in user's favorite list
     * @param name
     * @param pass
     * @param cancerName
     * @param lat
     * @param lon
     * @param countyName
     * @param stateName
     */
    public static void addFavorite(String name, String pass, String cancerName, double lat,
                                   double lon, String countyName, String stateName){
        //http://ws.instrumentsafe.com/ab/add_bookmark/wcastonzo5@comcast.net/foo/allen/18/53/41.0564660/-85.3312010

        int ct = cancerLookUp.get(cancerName);
        int sc = stateLookUp.get(stateName);

        HttpGet httpGet = new HttpGet("http://ws.instrumentsafe.com/ab/add_bookmark/"+name+"/"+pass+"/"+countyName.toLowerCase()+
                "/"+sc+"/"+ct+"/"+lat+"/"+lon);

        System.out.println("http://ws.instrumentsafe.com/ab/add_bookmark/"+name+"/"+pass+"/"+countyName+
                "/"+sc+"/"+ct+"/"+lat+"/"+lon);

        httpToJson(httpGet);

    }

    /**
     * This fetch's a user's favorite list, leveraging getBooks for the JSON
     * @param email
     * @param pwd
     * @return
     */
    public static ArrayList<RHMFavModel> fetchFavorites(String email, String pwd){

        ArrayList<RHMFavModel> list = new ArrayList<RHMFavModel>();

        JSONArray res = getBooks("http://ws.instrumentsafe.com/gb/get_bookmarks/"+email+"/"+pwd);

        for(int i = 0; i < res.length(); i++){
            try {
                JSONObject r = res.getJSONObject(i);
                int cType = r.getInt("cancer_type");
                String cTN = null;
                for(Map.Entry e:cancerLookUp.entrySet()){
                    if(cType == (e.getValue())){
                        cTN = (String) e.getKey();
                        break;
                    }
                }

                int sType = r.getInt("state_code");
                String sTT = null;
                for(Map.Entry e:stateLookUp.entrySet()){
                    if(sType == (e.getValue())){
                        sTT = (String) e.getKey();
                        break;
                    }
                }

                String county = r.getString("county_name");
                double lat = r.getDouble("latitude");
                double lon = r.getDouble("longitude");

                RHMFavModel mod = new RHMFavModel();
                mod.setCancerType(cTN); mod.setStateName(sTT);
                mod.setCountyName(county);
                mod.setLat(lat); mod.setLon(lon);
                list.add(mod);
            }catch(Exception e){
                System.out.println("Fav list failed, internal error, ignore");
            }
        }
        return list;
    }
}
