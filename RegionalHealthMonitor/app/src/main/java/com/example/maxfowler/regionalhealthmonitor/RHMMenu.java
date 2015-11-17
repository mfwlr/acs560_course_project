package com.example.maxfowler.regionalhealthmonitor;


import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.content.Intent;
import android.widget.TabHost.OnTabChangeListener;


public class RHMMenu extends TabActivity implements OnTabChangeListener{

    TabHost hoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rhmmenu);

        hoster = getTabHost();
        hoster.setOnTabChangedListener(this);

        TabHost.TabSpec ts;
        Intent intent;

        /*****Code establishing the first tab - for our map*****/
        intent = new Intent().setClass(this, RHMMap.class);
        ts = hoster.newTabSpec("Regional Health Map").setIndicator("").setContent(intent);

        hoster.addTab(ts);

        /*****Favorites List*****/

        intent = new Intent().setClass(this, RHMFav.class);
        ts = hoster.newTabSpec("Favorite's List").setIndicator("").setContent(intent);
        hoster.addTab(ts);


        //Drawable status?
        hoster.getTabWidget().getChildAt(1).setBackgroundResource(android.R.drawable.ic_menu_save);

        hoster.getTabWidget().setCurrentTab(0);
        hoster.getTabWidget().getChildAt(0).setBackgroundResource(android.R.drawable.ic_menu_mapmode);
    }

    public void onTabChanged(String tabID){
        for(int i=0; i < hoster.getTabWidget().getChildCount();i++){
            if(i == 0){
                hoster.getTabWidget().getChildAt(i).setBackgroundResource(android.R.drawable.ic_menu_mapmode);
            } else if(i == 1){
                hoster.getTabWidget().getChildAt(i).setBackgroundResource(android.R.drawable.ic_menu_save);
            }
        }

        if(hoster.getCurrentTab() == 0) {
            hoster.getTabWidget().getChildAt(hoster.getCurrentTab()).setBackgroundResource(android.R.drawable.ic_menu_mapmode);
        }else {
            hoster.getTabWidget().getChildAt(hoster.getCurrentTab()).setBackgroundResource(android.R.drawable.ic_menu_save);
        }
    }


}
