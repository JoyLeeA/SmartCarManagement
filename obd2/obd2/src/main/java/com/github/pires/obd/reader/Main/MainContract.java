package com.github.pires.obd.reader.Main;

import android.app.Activity;
import android.content.Context;

public interface MainContract {

    interface View{
        void finishActivityView();
        void setSpeed(String speed);
        void setVoltage(String voltage);
        void setRPM(String rpm);
        void setCoolTem(String tem);
        void setEngineLoad(String consume);

    }

    interface  Presenter{
        void attachView(MainContract.View view);
        void initialize(Activity activity);
        void clickMenu(Context context, android.view.View view);
        void checkConnect(Context context);
        void clickRecord(Context context);
    }
}
