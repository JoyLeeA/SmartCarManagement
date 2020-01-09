package pl.rafman.scrollcalendar.data;

import android.content.Context;

import pl.rafman.scrollcalendar.R;

public class getLanguageMonth {

    private static getLanguageMonth instance;

    public static getLanguageMonth getInstance() {

        if(instance == null)
            instance = new getLanguageMonth();

        return instance;
    }

    public String getMonth(Context context, int month){
        switch (month){
            case 1 :
                return context.getString(R.string.MONTH_JANUARY);
            case 2 :
                return context.getString(R.string.MONTH_FEBRUARY);
            case 3 :
                return context.getString(R.string.MONTH_MARCH);
            case 4 :
                return context.getString(R.string.MONTH_APRIL);
            case 5 :
                return context.getString(R.string.MONTH_MAY);
            case 6 :
                return context.getString(R.string.MONTH_JUNE);
            case 7 :
                return context.getString(R.string.MONTH_JULY);
            case 8 :
                return context.getString(R.string.MONTH_AUGUST);
            case 9 :
                return context.getString(R.string.MONTH_SEPTEMBER);
            case 10 :
                return context.getString(R.string.MONTH_OCTOBER);
            case 11 :
                return context.getString(R.string.MONTH_NOVEMBER);
            case 12 :
                return context.getString(R.string.MONTH_DECEMBER);
        }
        return "";
    }
}
