package com.github.pires.obd.reader.Splash;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.github.pires.obd.reader.R;

public class   SplashActivity extends AppCompatActivity implements SplashContract.View {

    private SplashPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        presenter = new SplashPresenter();
        presenter.attachView(this);
        presenter.initialize(this);
    }


    @Override
    public void finishActivityView() {
        finish();
    }
}
