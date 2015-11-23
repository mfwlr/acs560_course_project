package com.example.maxfowler.regionalhealthmonitor;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.Manifest;
import android.content.pm.PackageManager;

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

import android.widget.PopupMenu;
import android.view.MenuItem;


/**
 * Created by maxfowler on 11/5/15.
 */
public class RHMMap extends Activity implements OnMapReadyCallback, LocationListener, PopupMenu.OnMenuItemClickListener{

    private String currentCounty;
    private boolean usePosition;
    private LatLng pos;
    private int cancerType;
    private String cancerName;
    private GoogleMap mMap;

    private PopupMenu popup;

    private Marker curMarker;

    private double curLat;
    private double curLon;

    private RHMPointData currentRPD;

    public void onCreate(Bundle instanceState){
        super.onCreate(instanceState);
        setContentView(R.layout.rhmmap);

        usePosition = true;
        cancerType = 40;
        cancerName = "Pancreas";

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

        placeMaker(lat, lon);
        curLat = lat;
        curLon = lon;


    }

    public void placeMaker(double lat, double lon){


        String stateName = RHMDataCenter.getState(lat, lon, getBaseContext());

        String countyName = RHMDataCenter.getCounty(lat, lon);

        String s = lat+"\n"+lon+
                "\n\nCounty name: " + countyName
                +"\n\nState name: " + stateName;

        // new AlertDialog.Builder(this).setTitle("Sample").setMessage(s).show();

        currentRPD = RHMDataCenter.makeRPD(countyName, cancerName, cancerType, stateName);
        curMarker = mMap.addMarker(new MarkerOptions().position(pos).title(currentRPD.buildTitle()).snippet(currentRPD.buildSnippet()).icon(BitmapDescriptorFactory.defaultMarker(currentRPD.severityHue())));
        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(pos, 12);
        mMap.moveCamera(cu);
    }

    @Override

    public void onLocationChanged(Location location) {

     /*   // Getting latitude of the current location

        double latitude = location.getLatitude();


        // Getting longitude of the current location

        double longitude = location.getLongitude();


        // Creating a LatLng object for the current location

        LatLng latLng = new LatLng(latitude, longitude);


        // Showing the current location in Google Map

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


        // Zoom in the Google Map

        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));*/


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
        popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
       inflater.inflate(R.menu.menu, popup.getMenu());
        popup.show();
        //Change cancer type
        cancerName = "Leukemia";
        cancerType = 90;
        curMarker.remove();
        System.out.println("Working?");
        placeMaker(curLat, curLon);
    }

    public boolean onMenuItemClick(MenuItem item) {
        System.out.println("HAHAHAHA");
        System.out.println(item.getItemId());
        switch (item.getItemId()) {
            case R.id.one:

               break;
            case R.id.two:

               break;
            case R.id.three:

                break;
            case R.id.four:

                break;
            case R.id.five:

                break;
            case R.id.six:

                break;
            case R.id.seven:

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
        placeMaker(curLat, curLon);
        return true;
    }




}


