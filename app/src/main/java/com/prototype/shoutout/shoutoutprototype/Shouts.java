package com.prototype.shoutout.shoutoutprototype;

import android.location.Location;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;

//A shout is the following
public class Shouts {
    //Shout Id
    @com.google.gson.annotations.SerializedName("id")
    private String mId;

    //Title of shout
    @com.google.gson.annotations.SerializedName("title")
    private String mTitle;

    //Message of shout.  Or just the "shout"
    @com.google.gson.annotations.SerializedName("shout")
    private String mShout;

    //User shouting
    @com.google.gson.annotations.SerializedName("user")
    private String mUser;

    private google.maps.LatLng mLocation;

    //Default Constructor
    public Shouts() {

    }

    //Constructor
    public Shouts(String id, String title, String shout, String user, Location location){
        this.setId(id);
        this.setTitle(title);
        this.setShout(shout);
        this.setUser(user);
        this.setLocation(location);
    }


    @Override
    public String toString() {
        return getShout();
    }

    public String getShout() {
        return mShout;
    }

    public String getId(){
        return mId;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getUser(){
        return mUser;
    }

    public Location getLocation(){
        return mLocation;
    }

    public final void setId(String id) {
        mId = id;
    }

    public final void setTitle(String title) {
        mTitle = title;
    }

    public final void setUser(String user){
        mUser = user;
    }

    public final void setShout(String shout){
        mShout = shout;
    }

    public final void setLocation(Location location){
        mLocation = location;
    }
}
