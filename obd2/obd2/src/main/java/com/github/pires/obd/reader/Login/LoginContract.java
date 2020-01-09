package com.github.pires.obd.reader.Login;

import android.app.Activity;
import android.content.Context;

import com.github.pires.obd.reader.SignUp.SignUpContract;

public interface LoginContract {

    interface View{
        void finishActivityView();
    }

    interface  Presenter{

        void attachView(LoginContract.View view);
        void initialize(Activity activity);
        void login(Context context,String id,String password);
        void signup(Context context);

    }

}
