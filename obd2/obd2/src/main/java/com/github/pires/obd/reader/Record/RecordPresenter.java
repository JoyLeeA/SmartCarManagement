package com.github.pires.obd.reader.Record;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.pires.obd.reader.API.APIHelper.RecordAPI;
import com.github.pires.obd.reader.Data.OBD.OBDDataModel;
import com.github.pires.obd.reader.Util.DateManager;

import java.util.ArrayList;
import java.util.Calendar;

import pl.rafman.scrollcalendar.contract.State;
import pl.rafman.scrollcalendar.data.CalendarDay;
import pl.rafman.scrollcalendar.data.getLanguageMonth;

public class RecordPresenter implements RecordContract.Presenter {

    private RecordContract.View view;
    private String currentDate;
    private int curHolderIndex = 0;
    private Context context;

    @Nullable
    private Calendar selected;


    @Override
    public void attachView(RecordContract.View view) {
        this.view = view;

    }

    @Override
    public void initialize(Context context) {
        setTodayData(context);
        this.context = context;
    }

    private void setTodayData(Context context){

        currentDate = DateManager.getInstance().getCurrentDate();

        String year = DateManager.getInstance().getCurrentYear();
        String month = DateManager.getInstance().getCurrentMonth();
        String day = DateManager.getInstance().getCurrentDay();

        view.setDate(day+", "+ getLanguageMonth.getInstance().getMonth(context,Integer.parseInt(month))+" "+year);

    }

    @State
    @Override
    public int doGetStateForDate(int year, int month, int day) {

        if (isSelected(selected, year, month, day)) {
            return CalendarDay.SELECTED;
        }
        else if (isToday(year, month, day)) {
            return CalendarDay.TODAY;
        }

        return CalendarDay.DEFAULT;
    }

    @Override
    public void onCalendarItemClick(Context context,int year, int month, int day) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        month++;

        String Year = String.valueOf(year);
        String Month = String.valueOf(month);
        String Day = String.valueOf(day);




        if( month < 10){
            Month = "0"+month;
        }

        if(day < 10 ){
            Day = "0"+day;
        }

        String date = Year + "-" + Month + "-" + Day;

        RecordAPI.getInstance().call(this.context, date, new RecordAPI.callback() {
            @Override
            public void callbackMethod(boolean isSuccessful, ArrayList<OBDDataModel> data) {

                if(isSuccessful){

                    if(data.size() != 0){
                        view.setCoolTem(data.get(0).getCoolantTemperature());
                        view.setVoltage(data.get(0).getVoltage());
                    }


                }
                else{
                    view.setCoolTem("0");
                    view.setVoltage("0");
                }

            }
        });

        currentDate = date;

        view.setDate(day+", "+ getLanguageMonth.getInstance().getMonth(context,month)+" "+year);

        selected = calendar;

    }



    @Override
    public boolean doShouldAddPreviousMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -60);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long target = calendar.getTimeInMillis();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);

        return calendar.getTimeInMillis() > target;
    }


    public boolean isSelected(Calendar selected, int year, int month, int day) {
        if (selected == null) {
            return false;
        }
        //noinspection UnnecessaryLocalVariable
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selected.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, selected.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, selected.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long millis = calendar.getTimeInMillis();

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        long millis2 = calendar.getTimeInMillis();

        return millis == millis2;
    }

    public boolean isToday(int year, int month, int day) {

        if(selected == null){
            //noinspection UnnecessaryLocalVariable
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            // Today in milliseconds
            long today = calendar.getTime().getTime();

            // Given day in milliseconds
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);

            long calendarMillis = calendar.getTime().getTime();

            return today == calendarMillis;
        }

        return false;

    }


    @Override
    public void moveToday(Context context) {
        selected = null;
        view.refreshCalendar();
        setTodayData(context);

    }

    @Override
    public void setPreviousHolder(Context context) {
        if(curHolderIndex>0){
            curHolderIndex--;
            curHolderIndex %= 2;
            view.setPreviousHolderView(curHolderIndex);

        }
    }

    @Override
    public void setNextHolder(Context context) {
        if(curHolderIndex<1){
            curHolderIndex++;
            curHolderIndex %= 2;
            view.setNextHolderView(curHolderIndex);
        }
    }

    @Override
    public void finish(Context context) {
        view.finishActivity();
    }

}
