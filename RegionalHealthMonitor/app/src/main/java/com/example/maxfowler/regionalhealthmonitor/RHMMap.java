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



public class RHMMap extends Activity implements OnMapReadyCallback, LocationListener, GoogleMap.OnMapClickListener, PopupMenu.OnMenuItemClickListener{


    private boolean usePosition;
    private int curMark;
    private LatLng pos;

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

        System.out.println("Current user " + ((RHMAppData)this.getApplication()).getUser());


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

    public void markerAUseLoc(){
        Location location = getLocation();


        //View file for pin
        RHMInfoPane rip = new RHMInfoPane(getBaseContext());
        mMap.setInfoWindowAdapter(rip);


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

        if(lat > curLat + 10 || lat < curLat - 10 || lon > curLon+10 || lon < curLon-10) {

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

        if(curMark == 0){
            if(curMarkerA !=  null) {
                curMarkerA.remove();
            }
            currentRPDA = RHMDataCenter.makeRPD(countyName, cancerName, cancerType, stateName, lat, lon);
            curMarkerA = mMap.addMarker(new MarkerOptions().position(pos).title(currentRPDA.buildTitle()).snippet(currentRPDA.buildSnippet()).icon(BitmapDescriptorFactory.defaultMarker(currentRPDA.severityHue())));
        }else{
            if(curMarkerB !=  null) {
                curMarkerB.remove();
            }
            currentRPDB = RHMDataCenter.makeRPD(countyName, cancerName, cancerType, stateName, lat, lon);
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
                cancerType = 76;
                break;
            case R.id.two:
                cancerName = "Breast Cancer";
                cancerType = 55;
                break;
            case R.id.three:
                cancerName = "Lung";
                cancerType = 47;
                break;
            case R.id.four:
                cancerName = "Cervix";
                cancerType = 57;
                break;
            case R.id.five:
                cancerName = "Colon and Rectum";
                cancerType = 20;
                break;
            case R.id.six:
                cancerName = "Esophagus";
                cancerType = 17;
                break;
            case R.id.seven:
                cancerName = "Kidney and Pelvis";
                cancerType = 72;
                return true;
            case R.id.eight:
                cancerName = "Leukemia";
                cancerType = 90;
                break;

            case R.id.nine:
                cancerName = "Pancreas";
                cancerType = 40;
                break;
            default:
                return false;
        }
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


}


