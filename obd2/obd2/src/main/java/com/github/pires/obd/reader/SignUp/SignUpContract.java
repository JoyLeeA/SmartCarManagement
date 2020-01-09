package com.github.pires.obd.reader.SignUp;

import android.app.Activity;
import android.content.Context;

import com.github.pires.obd.reader.Splash.SplashContract;

public interface SignUpContract {

    interface View{
        void finishActivityView();
    }

    interface  Presenter{

        void attachView(SignUpContract.View view);
        void signup(Context context,String id,String password,String name,String car);
    }
}
