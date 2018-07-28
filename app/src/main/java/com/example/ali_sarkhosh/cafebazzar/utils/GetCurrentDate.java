package com.example.ali_sarkhosh.cafebazzar.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GetCurrentDate {
    public static String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyymmdd");
        return  new BigDecimal(df.format(c)).toString();
    }
}
