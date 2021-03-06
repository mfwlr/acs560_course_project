package com.example.maxfowler.regionalhealthmonitor;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;


import android.view.View;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import android.graphics.Typeface;
import android.graphics.Color;

import android.content.Context;

/**
 * RHMInfoPane handles some of the information display for the google map markers
 */
public class RHMInfoPane implements GoogleMap.InfoWindowAdapter{

    private Context mContext;

    /**
     * Constructor, to set up the map context
     * @param mContext
     */
    public RHMInfoPane(Context mContext){
        this.mContext = mContext;

    }


        @Override
        /**
         * Basically does nothing D:
         */
        public View getInfoWindow(Marker arg0) {
            return null;
        }

        @Override
        /**
         * getInfoContents sets up the info display for a given marker
         */
        public View getInfoContents(Marker marker) {

            LinearLayout info = new LinearLayout(mContext);
            info.setOrientation(LinearLayout.VERTICAL);

            TextView title = new TextView(mContext);
            title.setTextColor(Color.BLACK);
            title.setGravity(Gravity.CENTER);
            title.setTypeface(null, Typeface.BOLD);
            title.setText(marker.getTitle());

            TextView snippet = new TextView(mContext);
            snippet.setTextColor(Color.GRAY);
            snippet.setText(marker.getSnippet());

            info.addView(title);
            info.addView(snippet);



            return info;
        }


}
