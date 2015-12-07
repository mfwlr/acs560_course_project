package com.example.maxfowler.regionalhealthmonitor;

import android.app.Application;

public class RHMAppData extends Application {

    private RHMUser loggedInUser;
    private int mark = -5;
    private RHMFavModel info;

    public RHMUser getUser(){
        return loggedInUser;
    }

    public void setUser(RHMUser u){
        loggedInUser = u;
    }

    public void setInfoAndMark(RHMFavModel m, int m2){
        mark = m2;
        info = m;
    }
    public RHMFavModel fetchInfo(){
        RHMFavModel t = info;
        info = null;
        return t;
    }
    public int fetchMark(){
        int t = mark;
        mark = -5;
        return t;
    }
}
