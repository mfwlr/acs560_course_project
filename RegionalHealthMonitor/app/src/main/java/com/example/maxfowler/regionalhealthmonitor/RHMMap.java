package com.example.maxfowler.regionalhealthmonitor;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.Manifest;
import android.content.pm.PackageManager;

import android.app.Dialog;
import android.widget.TextView;

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



/**
 * Created by maxfowler on 11/5/15.
 */
public class RHMMap extends Activity implements OnMapReadyCallback, LocationListener{
    private GoogleMap mMap;
    public void onCreate(Bundle instanceState){
        super.onCreate(instanceState);
        setContentView(R.layout.rhmmap);

        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mf.getMapAsync(this);





    }

    public Location getLocation(){
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

        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(pos).title("Current Location"));
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




}


