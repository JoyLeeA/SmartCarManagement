package pl.rafman.scrollcalendar.data;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import pl.rafman.scrollcalendar.R;

/**
 * Created by rafal.manka on 10/09/2017
 */

public class CalendarMonth implements Serializable {

    private static final String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};

    private final int year;
    private final int month;
    @NonNull
    private final CalendarDay[] days;

    public CalendarMonth(int year, int month) {
        this(year, month, makeDays(year, month));
    }

    private CalendarMonth(int year, int month, @NonNull CalendarDay[] days) {
        this.year = year;
        this.month = month;
        this.days = days;
    }

    @NonNull
    static CalendarDay[] makeDays(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        CalendarDay[] calendarDays = new CalendarDay[maxDays];

        for (int i = 1; i <= maxDays; i++) {
            calendarDays[i - 1] = new CalendarDay(i);
        }

        return calendarDays;
    }

    @NonNull
    public CalendarDay[] getDays() {
        return days;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }


    public CalendarMonth previous() {
        if (month == Calendar.JANUARY) {
            return new CalendarMonth(year - 1, Calendar.DECEMBER);
        } else {
            return new CalendarMonth(year, month - 1);
        }
    }

    public CalendarMonth next() {
        if (month == Calendar.DECEMBER) {
            return new CalendarMonth(year + 1, Calendar.JANUARY);
        } else {
            return new CalendarMonth(year, month + 1);
        }
    }

    public static CalendarMonth now() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        return new CalendarMonth(year, month);
    }

    private String getMonthForInt(Context context,int num) {
        if (num < 0) {
            return "";
        }
        if (num >= months.length) {
            return "";
        }


        return getLanguageMonth.getInstance().getMonth(context,num);
    }

    @NonNull
    public String getMonthNameWithYear(Context context) {
        return getMonthForInt(context,getMonth()) + " " + year;
    }
}
