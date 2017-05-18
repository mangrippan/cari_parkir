package com.tomcat.parkir;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by albertbrucelee on 12/05/17.
 */

public class User {
    private String username;
    private String password;
    private LatLng latLng;

    //GPSTracker gps;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    /*
    public void setLatLng(Context context){
        gps = new GPSTracker(context);
        if (gps.canGetLocation()) {
            latLng = new LatLng(gps.getLatitude(), gps.getLongitude());
        }
    }
    public LatLng getLatLng(){
        return latLng;
    }
    */
}
