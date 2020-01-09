package com.github.pires.obd.reader.Splash;

import android.app.Activity;

public interface SplashContract {

    interface View{
        void finishActivityView();
    }

    interface  Presenter{
        void attachView(SplashContract.View view);
        void initialize(Activity activity);
    }
}
