package com.example.ali_sarkhosh.cafebazzar.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ali_sarkhosh.cafebazzar.model.DataItem;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cafebazzarapi.db";
    public static final String LOCATION_TABLE_NAME = "offline_data";
    public static final String LOCATION_COLUMN_ID = "id";
    public static final String LOCATION_COLUMN_NAME = "name";
    public static final String LOCATION_COLUMN_ADDRESS = "address";
    public static final String LOCATION_COLUMN_CROSS_STREET = "crossStreet";
    public static final String LOCATION_COLUMN_DISTANCE = "distance";
    public static final String LOCATION_COLUMN_CITY = "city";
    public static final String LOCATION_COLUMN_COUNTRY = "country";




    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table offline_data " +
                        "(id text, name text,address text,crossStreet text, distance text,city text ,country text )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS offline_data");
        onCreate(db);
    }

    public boolean insertLocations(String id , String name, String address, String crossStreet, String distance, String city , String country ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues locationValues = new ContentValues();
        locationValues.put("id", id);
        locationValues.put("name", name);
        locationValues.put("address", address);
        locationValues.put("crossStreet", crossStreet);
        locationValues.put("distance", distance);
        locationValues.put("city", city);
        locationValues.put("country", country);
        db.insert("offline_data", null, locationValues);
        return true;
    }


    public boolean deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ LOCATION_TABLE_NAME);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from offline_data where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LOCATION_TABLE_NAME);
        return numRows;
    }

    public boolean updateLocations(String id , String name, String address, String crossStreet, String distance, String city , String country ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("address", address);
        contentValues.put("crossStreet", crossStreet);
        contentValues.put("distance", distance);
        contentValues.put("city", city);
        contentValues.put("country", country);
        db.update("offline_data", contentValues, "id = ? ", new String[] { id } );
        return true;
    }

    public Integer deleteLocations(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("offline_data",
                "id = ? ",
                new String[] { id });
    }

    public ArrayList<DataItem> getAllLocations() {
        ArrayList<DataItem> di = new ArrayList<DataItem>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from offline_data", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
           // array_list.add(res.getString(res.getColumnIndex(LOCATION_COLUMN_NAME)));
            DataItem item = new DataItem();
            item.setId(res.getString(res.getColumnIndex(LOCATION_COLUMN_ID)));
            item.setName(res.getString(res.getColumnIndex(LOCATION_COLUMN_NAME)));
            item.setCity(res.getString(res.getColumnIndex(LOCATION_COLUMN_CITY)));
            item.setCountry(res.getString(res.getColumnIndex(LOCATION_COLUMN_COUNTRY)));
            item.setAddress(res.getString(res.getColumnIndex(LOCATION_COLUMN_ADDRESS)));
            item.setDistance(res.getString(res.getColumnIndex(LOCATION_COLUMN_DISTANCE)));
            item.setCrossStreet(res.getString(res.getColumnIndex(LOCATION_COLUMN_CROSS_STREET)));

            di.add(item);
            res.moveToNext();
        }
        return di;
    }


    //save db on SD card

//    public void exportDatabse(String databaseName) {
//        try {
//            File sd = Environment.getExternalStorageDirectory();
//            File data = Environment.getDataDirectory();
//
//            if (sd.canWrite()) {
//                String currentDBPath = "//data//"+ G.PACKAGE_NAME +"//databases//"+databaseName+"";
//                String backupDBPath = "backupname.db";
//                File currentDB = new File(data, currentDBPath);
//                File backupDB = new File(sd, backupDBPath);
//
//                if (currentDB.exists()) {
//                    FileChannel src = new FileInputStream(currentDB).getChannel();
//                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
//                    dst.transferFrom(src, 0, src.size());
//                    src.close();
//                    dst.close();
//                }
//            }
//        } catch (Exception e) {
//
//        }
//    }
}