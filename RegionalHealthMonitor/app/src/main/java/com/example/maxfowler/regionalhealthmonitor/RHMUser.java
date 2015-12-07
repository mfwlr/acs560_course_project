package com.example.maxfowler.regionalhealthmonitor;

import android.os.Parcelable;
import android.os.Parcel;

import com.google.android.gms.drive.metadata.internal.ParentDriveIdSet;


public class RHMUser implements Parcelable {
    private String name;
    private String pass;
    public RHMUser(String name, String pass){
        this.name = name;
        this.pass = pass;
    }

    public String getName(){
        return name;
    }

    public String getPass(){
        return pass;
    }

    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeString(name);
        out.writeString(pass);
    }

    public static final Parcelable.Creator<RHMUser> CREATOR = new Parcelable.Creator<RHMUser>(){

        public RHMUser createFromParcel(Parcel in){
            return new RHMUser(in);
        }

        public RHMUser[] newArray(int size){
            return new RHMUser[size];
        }

    };

    private RHMUser(Parcel in){
        name = in.readString();
        pass = in.readString();
    }

    public String toString(){
        return name + "    " + pass;
    }
}
