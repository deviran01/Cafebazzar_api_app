package com.example.ali_sarkhosh.cafebazzar.activitys;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ali_sarkhosh.cafebazzar.Adapter.AdapterRVList;
import com.example.ali_sarkhosh.cafebazzar.G;
import com.example.ali_sarkhosh.cafebazzar.R;
import com.example.ali_sarkhosh.cafebazzar.database.DBHelper;
import com.example.ali_sarkhosh.cafebazzar.model.DataItem;
import com.example.ali_sarkhosh.cafebazzar.services.ServiceCommunication;
import com.example.ali_sarkhosh.cafebazzar.services.ServiceGetCurrentLocation;
import com.example.ali_sarkhosh.cafebazzar.utils.CalculateDistance;
import com.example.ali_sarkhosh.cafebazzar.utils.GetCurrentDate;
import com.example.ali_sarkhosh.cafebazzar.utils.LocationManager;
import com.example.ali_sarkhosh.cafebazzar.utils.NetworkHelper;
import com.example.ali_sarkhosh.cafebazzar.utils.ParsJsonFormatToDataModel;

import java.util.HashMap;
import java.util.List;

public class ActivityMain extends AppCompatActivity {

    public static int PERMISSION_LOCATION_REQUEST_CODE = 75;
    private BroadcastReceiver communicationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(ServiceCommunication.SERVICE_PAYLOAD);
            if (message != null) {
                ParsJsonFormatToDataModel.parser(message);
            } else {
                //      Toast.makeText(G.context,getString(R.string.check_connection), Toast.LENGTH_SHORT).show();
                getDataFromServer();
                G.finalFlag = false;

            }

        }


    };


    private BroadcastReceiver locationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //compare locations
            compareLocation(intent.getExtras());
        }


    };

    private void compareLocation(Bundle location) {
        //check previous location if exist


        //  save static data , send request to server
        G.lat = String.valueOf(location.get("Latitude"));
        G.log = String.valueOf(location.get("Longitude"));

        if (G.locationManager.isLocated()) {
            //previous location
            HashMap map = G.locationManager.getLocationDetails();
            //compare distance between locations with class CalculateDistance
            CalculateDistance distance = new CalculateDistance();
            if (distance.distance(
                    Double.parseDouble(String.valueOf(location.get("Latitude"))),
                    Double.parseDouble(String.valueOf(location.get("Longitude"))),
                    Double.parseDouble(String.valueOf(map.get(LocationManager.KEY_LAT))),
                    Double.parseDouble(String.valueOf(map.get(LocationManager.KEY_LOG)))) > 100) {


                //  create and submit new location
                G.locationManager.createLocationSession(Float.parseFloat(String.valueOf(location.get("Latitude"))), Float.parseFloat(String.valueOf(location.get("Longitude"))));


                getDataFromServer();

            } else {
                //  check offline db
                //  if exist show to user
                //  else get data from server

                G.locationManager.createLocationSession(Float.parseFloat(String.valueOf(location.get("Latitude"))), Float.parseFloat(String.valueOf(location.get("Longitude"))));


                DBHelper dbHelper = new DBHelper(G.context);

                if (dbHelper.numberOfRows() > 0) {
                    //     Toast.makeText(G.context , dbHelper.numberOfRows() +" " , Toast.LENGTH_SHORT).show();

                    //use static var to use every where

                    //clean array
                    G.dI.clear();
                    G.dI = dbHelper.getAllLocations();
                    G.finalFlag = false;
                    updateReceiptsList(G.dI);
                    // showDataToUser();

                } else {
                    getDataFromServer();
                }


            }

        } else {
            getDataFromServer();
        }
    }

    public void updateParameters() {
        G.DEF_LIMIT += 10;
        //    G.DEF_RADIUS *= 2;
        if (G.DEF_LIMIT > 50) G.finalFlag = false;
    }

    public static int listViewPosition = 0;
    public static boolean flagCheckReload = false;

    public void showDataToUser() {
        flagCheckReload = true;
        listData.setSelection(listViewPosition);
        listData.removeFooterView(footerView);
        adapter.notifyDataSetChanged();
        dismissAlert();
    }

    public void updateReceiptsList(List<DataItem> newlist) {
        flagCheckReload = true;
        listData.setSelection(listViewPosition);
        listData.removeFooterView(footerView);
        adapter.clear();
        adapter.addAll(newlist);
        adapter.notifyDataSetChanged();
        dismissAlert();
    }


    private void dismissAlert() {
        dialog.dismiss();
    }

    public void getDataFromServer() {
        String JSON_URL = G.PREFIX_URL + G.EXPLORE_DIR + G.PREFIX_CLIENT_ID_KEYWORD + G.FOURSQUARE_CLIENT_KEY + G.AND_KEYWORD + G.PREFIX_CLIENT_SECRET + G.FOURSQUARE_CLIENT_SECRET + G.AND_KEYWORD + G.PREFIX_VERSIONING + GetCurrentDate.getCurrentDate() + G.AND_KEYWORD + G.PREFIX_LL + G.lat + "," + G.log + G.AND_KEYWORD + G.PREFIX_RADIUS + G.DEF_RADIUS + G.AND_KEYWORD + G.PREFIX_LIMIT + G.DEF_LIMIT;
        Intent i = new Intent(G.context, ServiceCommunication.class);
        i.setData(Uri.parse(JSON_URL));
        startService(i);
    }


    public static ArrayAdapter adapter;
    public static ListView listData;

    private static Dialog dialog;
    public static View footerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        boolean networkStatus = NetworkHelper.hasNetworkAccess(G.context);


        //check network connection

        if (networkStatus) {

            /*  stars service to get current location
                for use less the battery and data , service check every 30 sec or 100 meter change location
                for find fine location used two method 1-GPS_PROVIDER 2-NETWORK_PROVIDER
                if want to use less battery and data turn off one of the methods
             */

            //get permission in android API 21 >

            //for startServiceLocation must be register and unregister LocalBroadcastManager

            registerLocalBroadcastManagerLocationService();
            registerLocalBroadcastManagerCommunicationService();
            startAlert();
            showPermissionDialog();


           // startServiceLocation();




            //start Alert


        } else {

            //  firstOffline mode
            /*  check offline data and location
                if found offline data ,location Tracked with GPS and compare to previous location
                if compare data > 100 meters offline date isn't valid.
                else data is valid and show to user
             */
            Toast.makeText(G.context, getString(R.string.error_first_offline), Toast.LENGTH_SHORT).show();



            //for startServiceLocation must be register and unregister LocalBroadcastManager

            registerLocalBroadcastManagerLocationService();
            registerLocalBroadcastManagerCommunicationService();
            showPermissionDialog();

        }





        listData.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //  if (totalItemCount < 9) G.finalFlag =false;

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (G.finalFlag) {

                        if (flagCheckReload) {
                            flagCheckReload = false;
                            listData.addFooterView(footerView);
                            listViewPosition = totalItemCount;
                            getDataFromServer();
                        }
                    } else {
                        listData.removeFooterView(footerView);
                        Toast.makeText(G.context, getString(R.string.end_list), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }

    private void registerLocalBroadcastManagerCommunicationService() {
        LocalBroadcastManager.getInstance(G.context).registerReceiver(communicationBroadcastReceiver,
                new IntentFilter(ServiceCommunication.SERVICE_MESSAGE));
    }

    public boolean startAlert() {
        dialog.setContentView(R.layout.custom_alert);
        dialog.setTitle("دریافت اطلاعات از سرور");
        dialog.show();
        return true;
    }

    private void registerLocalBroadcastManagerLocationService() {
        LocalBroadcastManager.getInstance(G.context).registerReceiver(locationBroadcastReceiver,
                new IntentFilter(ServiceGetCurrentLocation.BROADCAST_ACTION));
    }

    private void showPermissionDialog() {
        if (!ServiceGetCurrentLocation.checkPermission(this)) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_LOCATION_REQUEST_CODE);
        }
        startServiceLocation();
    }

    public void startServiceLocation() {
        Intent intent = new Intent(G.context, ServiceGetCurrentLocation.class);
        startService(intent);
    }

    private void init() {
        dialog = new Dialog(this);
        listData = findViewById(R.id.list1);
        adapter = new AdapterRVList(G.dI);
        listData.setAdapter(adapter);
        footerView = G.inflater.inflate(R.layout.loading_footer, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //       dialog.dismiss();
        LocalBroadcastManager.getInstance(G.context).unregisterReceiver(locationBroadcastReceiver);
        LocalBroadcastManager.getInstance(G.context).unregisterReceiver(communicationBroadcastReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_LOCATION_REQUEST_CODE)
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startServiceLocation();
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                //Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        return;


        // other 'case' lines to check for other
        // permissions this app might request

    }
}
