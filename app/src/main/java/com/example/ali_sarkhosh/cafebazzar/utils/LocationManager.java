package com.example.ali_sarkhosh.cafebazzar.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;


public class LocationManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "cafebazzar";

    // All Shared Preferences Keys
    private static final String GET_LOCATION = "location";

    // Location Latitude (make variable public to access from outside)
    public static final String KEY_LAT = "lat";

    // Location Longitude (make variable public to access from outside)
    public static final String KEY_LOG = "log";

    // Constructor
    public LocationManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        pref.edit().apply();
    }

    /**
     * Create login session
     * */
    public void createLocationSession(float lat, float log ){
        // Storing Location value as TRUE
        editor.putBoolean(GET_LOCATION, true);

        // Storing Latitude in pref
        editor.putFloat(KEY_LAT, lat);

        // Storing Longitude in pref
        editor.putFloat(KEY_LOG, log);


        // commit changes
        editor.commit();
    }


    /**
     * Get stored session data
     * */
    public HashMap<String, Float> getLocationDetails(){
        HashMap<String, Float> location = new HashMap<String, Float>();
        location.put(KEY_LAT, pref.getFloat(KEY_LAT , 0f));
        location.put(KEY_LOG, pref.getFloat(KEY_LOG, 0f));

        return location;
    }


    public boolean isLocated(){
        return pref.getBoolean(GET_LOCATION,false);
    }




}
