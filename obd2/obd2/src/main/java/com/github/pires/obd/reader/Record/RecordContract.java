package com.github.pires.obd.reader.Record;

import android.app.Activity;
import android.content.Context;

import com.github.pires.obd.reader.Main.MainContract;

public interface RecordContract {

    interface View{

        void finishActivity();
        void setPreviousHolderView(int index);
        void setNextHolderView(int index);
        void refreshCalendar();
        void setDate(String date);
        void setCoolTem(String date);
        void setVoltage(String voltage);

    }

    interface Presenter{
        void attachView(RecordContract.View view);
        void initialize(Context context);

        void onCalendarItemClick(Context context,int year, int month, int day);
        int doGetStateForDate(int year, int month, int day);
        boolean doShouldAddPreviousMonth(int year, int month);

        void moveToday(Context context);
        void setPreviousHolder(Context context);
        void setNextHolder(Context context);
        void finish(Context context);

    }
}
