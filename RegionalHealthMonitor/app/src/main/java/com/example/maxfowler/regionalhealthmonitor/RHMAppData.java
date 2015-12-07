package com.example.maxfowler.regionalhealthmonitor;

import android.app.Application;

public class RHMAppData extends Application {

    private RHMUser loggedInUser;

    public RHMUser getUser(){
        return loggedInUser;
    }

    public void setUser(RHMUser u){
        loggedInUser = u;
    }
}
