package com.prototype.shoutout.shoutoutprototype;

import com.google.android.gms.location.Geofence;

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

    private Geofence mLocation;

    //Default Constructor
    public Shouts() {

    }

    //Constructor
    public Shouts(String id, String title, String shout, String user, Geofence location){
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

    public Geofence getLocation(){
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

    public final void setLocation(Geofence location){
        mLocation = location;
    }
}
