package com.example.maxfowler.regionalhealthmonitor;

import android.app.Application;

/**
 * RHMAppData is a Single pattern class which holds app meta data that all applications need;
 * specifically, the logged in user and the desired favorite pin to display
 */
public class RHMAppData extends Application {

    private RHMUser loggedInUser;
    private int mark = -5;
    private RHMFavModel info;

    /**
     * Gets the logged in user
     * @return - logged in user
     */
    public RHMUser getUser(){
        return loggedInUser;
    }

    /**
     * Sets the logged in user
     * @param u - logged in user
     */
    public void setUser(RHMUser u){
        loggedInUser = u;
    }

    /**
     * Sets the info for a new map mark and which mark it should be
     * @param m - The mark info
     * @param m2 - Mark A(0) or B(1)
     */
    public void setInfoAndMark(RHMFavModel m, int m2){
        mark = m2;
        info = m;
    }

    /**
     * Fetch the desired mark info
     * @return the RHMFavModel representing the map mark info
     */
    public RHMFavModel fetchInfo(){
        RHMFavModel t = info;
        info = null;
        return t;
    }

    /**
     * Fetch the desired mark integer
     * @return - mark integer flag
     */
    public int fetchMark(){
        int t = mark;
        mark = -5;
        return t;
    }
}
