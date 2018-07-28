package com.example.ali_sarkhosh.cafebazzar.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.example.ali_sarkhosh.cafebazzar.G;
import com.example.ali_sarkhosh.cafebazzar.utils.HttpHelper;

import java.io.IOException;

public class ServiceCommunication extends IntentService {

    public static final String SERVICE_MESSAGE = "serviceMassage";
    public static final String SERVICE_PAYLOAD  = "servicePayload";

    public ServiceCommunication() {
        super("ServiceCommunication");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Uri uri = intent.getData();

        String response = null;
        try {
            response = HttpHelper.downloadUrl(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent messageIntent = new Intent(SERVICE_MESSAGE);
        messageIntent.putExtra(SERVICE_PAYLOAD , response);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(G.context);
        manager.sendBroadcast(messageIntent);
    }

}
