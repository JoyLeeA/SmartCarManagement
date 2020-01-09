package com.github.pires.obd.reader.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateManager {

    private static DateManager instance;

    public static DateManager getInstance() {

        if(instance == null)
            instance = new DateManager();

        return instance;
    }

    public String getCurrentDate(){
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd", Locale.KOREA );
        Date currentTime = new Date ();
        return  mSimpleDateFormat.format ( currentTime );
    }

    public String getCurrentDay(){
        return getCurrentDate().substring(8,10);
    }

    public String getCurrentMonth(){
        return getCurrentDate().substring(5,7);
    }

    public String getCurrentYear(){
        return getCurrentDate().substring(0,4);
    }
}
