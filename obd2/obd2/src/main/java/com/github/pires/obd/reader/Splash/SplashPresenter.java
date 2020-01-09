package com.github.pires.obd.reader.Splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import com.github.pires.obd.reader.Login.LoginActivity;

public class  SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View view;

    @Override
    public void attachView(SplashContract.View view) {
        this.view = view;
    }

    @Override
    public void initialize(final Activity activity) {

        final Handler hd = new Handler();

        new Thread(new Runnable() {
            @Override
            public void run() {

                hd.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        view.finishActivityView();

                    }
                },2000);


            }
        }).start();

    }
}
