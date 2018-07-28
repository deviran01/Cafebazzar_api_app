package com.example.ali_sarkhosh.cafebazzar;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.ali_sarkhosh.cafebazzar.database.DBHelper;
import com.example.ali_sarkhosh.cafebazzar.model.DataItem;
import com.example.ali_sarkhosh.cafebazzar.utils.LocationManager;

import java.util.ArrayList;

public class G extends Application {


    public static Context context;
    public static final String PREFIX_URL = "https://api.foursquare.com";
    public static final String EXPLORE_DIR = "/v2/venues/explore?";
    public static final String PREFIX_CLIENT_ID_KEYWORD = "client_id=";
    public static final String AND_KEYWORD = "&";
    public static final String PREFIX_CLIENT_SECRET = "client_secret=";
    public static final String PREFIX_VERSIONING = "v=";
    public static final String PREFIX_LL = "ll=";
    public static final String PREFIX_RADIUS = "radius=";
    public static final String PREFIX_LIMIT = "limit=";
    public static final String FOURSQUARE_CLIENT_KEY = "1X5OSG12DIMHLSZDKRNJQWK1C5CHV1FNISS5PSSWVIH52VMV";
    public static final String FOURSQUARE_CLIENT_SECRET = "0XNUTNYT3ZB2X5IBU440A4K3JFJN2XPDGJPNS5L5MMPR1EAG";




    public static String lat ;
    public static String log ;

    public static ArrayList<DataItem> dI = new ArrayList<>();

    public static int DEF_RADIUS = 10000;
    public static int DEF_LIMIT = 10;

    public static int COUNTER = 0;
    public static boolean flag = false;
    public static boolean finalFlag = true;

    public static String PACKAGE_NAME = null;

    public static LayoutInflater inflater;

    public static Typeface tf ;

    public static Animation animationLst;

    public static DBHelper db;

    public static LocationManager locationManager ;

    @Override
    public void onCreate() {
        super.onCreate();
        //use less memory
        context = getApplicationContext();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tf = Typeface.createFromAsset(G.context.getAssets(), "fonts/vazir.ttf");
        PACKAGE_NAME = getPackageName();
        animationLst = AnimationUtils.loadAnimation(G.context , R.anim.list_animation);
        db = new DBHelper(G.context);
        locationManager = new LocationManager(G.context);
    }
}
