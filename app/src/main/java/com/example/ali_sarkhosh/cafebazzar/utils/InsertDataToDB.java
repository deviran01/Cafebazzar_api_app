package com.example.ali_sarkhosh.cafebazzar.utils;


import com.example.ali_sarkhosh.cafebazzar.G;
import com.example.ali_sarkhosh.cafebazzar.activitys.ActivityMain;
import com.example.ali_sarkhosh.cafebazzar.database.DBHelper;
import com.example.ali_sarkhosh.cafebazzar.model.DataItem;

public class InsertDataToDB {
    public static void insertFromMemoryToDB(){
        G.db.deleteAllData();
        for (DataItem item: G.dI
             ) {
            G.db.insertLocations(item.getId() , item.getName() , item.getAddress() , item.getCrossStreet() , item.getDistance() , item.getCity() , item.getCountry());
        }
        ActivityMain main  =new ActivityMain();
        main.updateParameters();
        main.showDataToUser();
    }
}
