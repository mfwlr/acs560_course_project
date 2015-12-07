package com.example.maxfowler.regionalhealthmonitor;

import android.os.Bundle;
import android.app.Activity;

import android.app.Dialog;
import android.app.AlertDialog;
import android.widget.TextView;
import android.view.View;
import android.widget.PopupMenu;
import android.view.MenuInflater;

import android.util.Log;

import android.location.Location;
import android.location.LocationManager;
import android.location.Criteria;
import android.location.LocationListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import android.view.MenuItem;

import android.widget.Button;
import android.widget.CheckBox;
import android.content.Intent;



public class RHMMap extends Activity implements OnMapReadyCallback, LocationListener,
        GoogleMap.OnMapClickListener, PopupMenu.OnMenuItemClickListener, GoogleMap.OnInfoWindowClickListener{


    private boolean usePosition;
    private int curMark;
    private LatLng pos;

    private static int LL_TOLERANCE = 10;

    private int cancerType;
    private String cancerName;
    private GoogleMap mMap;

    private PopupMenu cancerPopUp;

    private Marker curMarkerA;
    private Marker curMarkerB;

    private double curLat;
    private double curLon;

    private RHMPointData currentRPDA;
    private RHMPointData currentRPDB;


    public void onCreate(Bundle instanceState){
        super.onCreate(instanceState);
        setContentView(R.layout.rhmmap);

        System.out.println("Current user " + ((RHMAppData) this.getApplication()).getUser());


        usePosition = true;
        cancerType = 40;
        cancerName = "Pancreas";

        curMark = 0;

        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mf.getMapAsync(this);

    }

    public Location getLocation(){

        if(!usePosition){
            return null;
        }
        // Getting LocationManager object from System Service LOCATION_SERVICE

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        // Creating a criteria object to retrieve provider

        Criteria criteria = new Criteria();


        // Getting the name of the best provider

        String provider = locationManager.getBestProvider(criteria, true);

        // Getting Current Location

        Location location = null;
        try {
            location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 20000, 0, this);
        }catch(SecurityException e){
            Log.d("NoPerm", "Uhoh!");

            Dialog d = new Dialog(this);
            TextView tv = (TextView)d.findViewById(R.id.text_wrapper);
            tv.setText("No location?!");

            d.show();
        }

       return location;
    }

    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        markerAUseLoc();


    }

    public void checkFav(){
        RHMFavModel r = ((RHMAppData)this.getApplication()).fetchInfo();
        int mark = ((RHMAppData)this.getApplication()).fetchMark();
        if(mark == 0){
            curMark = 0;
            usePosition = false;
            CheckBox cb = (CheckBox) this.findViewById(R.id.LocationToggle);
            cb.setChecked(false);
            cancerName = r.getCancerType();
            cancerType = RHMDataCenter.cancerLookUp.get(cancerName);
            placeMarker(r.getLat(),r.getLon());
        }else if(mark == 1){
            curMark = 1;
            CheckBox cb = (CheckBox) this.findViewById(R.id.LocationToggle);
            cb.setChecked(false);
            cancerName = r.getCancerType();
            cancerType = RHMDataCenter.cancerLookUp.get(cancerName);
            placeMarker(r.getLat(),r.getLon());
        }
    }

    public void markerAUseLoc(){
        Location location = getLocation();


        //View file for pin

        RHMInfoPane rip = new RHMInfoPane(getBaseContext());
        mMap.setInfoWindowAdapter(rip);
        mMap.setOnInfoWindowClickListener(this);


        double lat = 0;
        double lon = 0;
        if(location != null) {
            pos = new LatLng(location.getLatitude(), location.getLongitude());
            lat = location.getLatitude();
            lon = location.getLongitude();
        }
        else if(usePosition == true){
            pos = new LatLng(41, -85);
            lat = 41;
            lon = -85;
        }

        if(lat > curLat + LL_TOLERANCE || lat < curLat - LL_TOLERANCE ||
                lon > curLon+LL_TOLERANCE || lon < curLon-LL_TOLERANCE) {

            placeMarker(lat, lon);
            curLat = lat;
            curLon = lon;
        }
    }

    public void placeMarker(double lat, double lon){


        String stateName = RHMDataCenter.getState(lat, lon, getBaseContext());

        String countyName = RHMDataCenter.getCounty(lat, lon);

        if(countyName == null){
            new AlertDialog.Builder(this).setTitle("Error").setMessage("There was an issue with the google API and it returned a null county").show();
        }

        System.out.println("This is the county name " + countyName);

        String s = lat+"\n"+lon+
                "\n\nCounty name: " + countyName
                +"\n\nState name: " + stateName;

        // new AlertDialog.Builder(this).setTitle("Sample").setMessage(s).show();

        //Use allen as a default if all else fails
        if(countyName == null){
            new AlertDialog.Builder(this).setTitle("Error Warning").setMessage("Failed to get county - ensure network connection.  Default to allen").show();
            countyName = "allen";
            lat = 45;
            lon = -80;
        }

        LatLng pos = new LatLng(lat, lon);

        RHMUser u = ((RHMAppData)this.getApplication()).getUser();

        RHMPointData temporary = RHMDataCenter.makeRPD(countyName, cancerName, cancerType, stateName, lat, lon);

        if(temporary == null){
            new AlertDialog.Builder(this).setTitle("Error").setMessage("Unsupported county name due to data format errors - sorry!").show();
            return;
        }

        if(curMark == 0){
            if(curMarkerA !=  null) {
                curMarkerA.remove();
            }
            currentRPDA = temporary;
            currentRPDA.determineFavorite(u.getName(), u.getPass());
            curMarkerA = mMap.addMarker(new MarkerOptions().position(pos).title(currentRPDA.buildTitle()).snippet(currentRPDA.buildSnippet()).icon(BitmapDescriptorFactory.defaultMarker(currentRPDA.severityHue())));
        }else{
            if(curMarkerB !=  null) {
                curMarkerB.remove();
            }
            currentRPDB = temporary;
            currentRPDB.determineFavorite(u.getName(), u.getPass());
            curMarkerB = mMap.addMarker(new MarkerOptions().position(pos).title(currentRPDB.buildTitle()).snippet(currentRPDB.buildSnippet()).icon(BitmapDescriptorFactory.defaultMarker(currentRPDB.severityHue())));
        }
        if(curMark == 0 && usePosition) {
            CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(pos, 12);
            mMap.moveCamera(cu);
        }
    }

    @Override

    public void onLocationChanged(Location location) {
        if(curMark == 0 && usePosition == true){
            markerAUseLoc();
        }
    }


    @Override

    public void onProviderDisabled(String provider) {

        // TODO Auto-generated method stub

    }


    @Override

    public void onProviderEnabled(String provider) {

        // TODO Auto-generated method stub

    }


    @Override

    public void onStatusChanged(String provider, int status, Bundle extras) {

        // TODO Auto-generated method stub

    }

    protected void onResume(){
        super.onResume();
        checkFav();
    }


    public void showPopup(View v) {
        cancerPopUp = new PopupMenu(this, v);
        MenuInflater inflater = cancerPopUp.getMenuInflater();
       inflater.inflate(R.menu.menu, cancerPopUp.getMenu());
        cancerPopUp.setOnMenuItemClickListener(this);
        cancerPopUp.show();
        curMarkerA.remove();
        curMarkerB.remove();
    }

    public void changeMarker(View v) {
        Button b = (Button) this.findViewById(R.id.MarkerToggle);
        if(curMark == 0){
            curMark =1 ;
            b.setText("Marker: B");
        } else{
            curMark = 0;
            b.setText("Marker: A");
        }

    }

    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.one:
                cancerName = "Brain";
                break;
            case R.id.two:
                cancerName = "Breast Cancer";
                break;
            case R.id.three:
                cancerName = "Lung";
                break;
            case R.id.four:
                cancerName = "Cervix";
                break;
            case R.id.five:
                cancerName = "Colon and Rectum";
                break;
            case R.id.six:
                cancerName = "Esophagus";
                break;
            case R.id.seven:
                cancerName = "Kidney and Pelvis";
               break;
            case R.id.eight:
                cancerName = "Leukemia";
                break;
            case R.id.nine:
                cancerName = "Pancreas";
                break;
            default:
                return false;
        }
        cancerType = RHMDataCenter.cancerLookUp.get(cancerName);
        int tcm = curMark;
        curMark = 0;
        if(currentRPDA != null) {
            placeMarker(currentRPDA.getLatitude(), currentRPDA.getLongitude());
        }
        curMark = 1;
        if(currentRPDB != null) {
            placeMarker(currentRPDB.getLatitude(), currentRPDB.getLongitude());
        }
        curMark = tcm;
        return true;
    }

    public void onMapClick(LatLng latlng){
        System.out.println(latlng.latitude + "     " + latlng.longitude);
        if(curMark == 1) {
            if (curMarkerB != null) {
                curMarkerB.remove();
            }
            placeMarker(latlng.latitude, latlng.longitude);
        }else if(curMark == 0 && !usePosition){
            if(curMarkerA != null){
                curMarkerA.remove();
        }
            placeMarker(latlng.latitude, latlng.longitude);

        }
    }

    public void toggleLocation(View v){
        CheckBox cb = (CheckBox) this.findViewById(R.id.LocationToggle);
        usePosition = cb.isChecked();
    }

    public void onInfoWindowClick(Marker marker){
        RHMUser u = ((RHMAppData)this.getApplication()).getUser();
       if(curMark == 0){
           if(currentRPDA != null){
               currentRPDA.addFavorite(u.getName(), u.getPass());
           }
       }else{
           if(currentRPDB != null){
               currentRPDB.addFavorite(u.getName(), u.getPass());
           }

       }

    }

}


