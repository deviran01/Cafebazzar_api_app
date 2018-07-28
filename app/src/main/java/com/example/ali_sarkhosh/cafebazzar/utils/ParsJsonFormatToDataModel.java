package com.example.ali_sarkhosh.cafebazzar.utils;

import android.util.Log;

import com.example.ali_sarkhosh.cafebazzar.G;
import com.example.ali_sarkhosh.cafebazzar.R;
import com.example.ali_sarkhosh.cafebazzar.model.DataItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParsJsonFormatToDataModel {
    public static String tempPre = "";
    private static String notFound = G.context.getString(R.string.not_found);
    public static void parser(String message) {


        try {
            JSONObject object = new JSONObject(message);
            JSONObject response = object.getJSONObject("response");
            JSONArray groups = response.getJSONArray("groups");
            JSONObject groups0 = groups.getJSONObject(0);
            JSONArray items = groups0.getJSONArray("items");


            if (!response.toString().equals(tempPre)) {



                tempPre = response.toString();


                if (!G.dI.isEmpty()) G.dI.clear();


                for (int i = 0; i < items.length(); i++) {
                    JSONObject arrayI = items.getJSONObject(i);
                    JSONObject venue = arrayI.getJSONObject("venue");
                    JSONObject location = venue.getJSONObject("location");
                    JSONArray categories = venue.getJSONArray("categories");
                    JSONObject categories0 = categories.getJSONObject(0);
                    DataItem di = new DataItem();
                    if (venue.has("id")) di.setId(venue.getString("id"));
                    else di.setId(notFound);
                    if (venue.has("name")) di.setName(venue.getString("name"));
                    else di.setName(notFound);
                    if (location.has("city")) di.setCity(location.getString("city"));
                    else di.setCity(notFound);
                    if (location.has("country")) di.setCountry(location.getString("country"));
                    else di.setCountry(notFound);
                    if (location.has("distance")) di.setDistance(location.getString("distance"));
                    else di.setDistance(notFound);
                    if (location.has("crossStreet"))
                        di.setCrossStreet(location.getString("crossStreet"));
                    else di.setCrossStreet(notFound);
                    G.dI.add(di);


                }

                InsertDataToDB.insertFromMemoryToDB();

            }else  G.finalFlag = false;
            G.COUNTER++;

          //  if(G.COUNTER >6) G.finalFlag =false;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("parser", "parser: " + e.toString());
        }finally {




        }

    }
}
