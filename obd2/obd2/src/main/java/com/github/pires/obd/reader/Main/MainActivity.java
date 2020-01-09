package com.github.pires.obd.reader.Main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.pires.obd.reader.R;


public class MainActivity extends AppCompatActivity implements MainContract.View,View.OnClickListener {


    private ImageView setting,record;
    private MainPresenter presenter;
    private TextView speed,vol,rpm,coolTem,engineLoad;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_driver_main);

        speed = findViewById(R.id.driver_main_speed);
        vol = findViewById(R.id.driver_main_vol);
        rpm = findViewById(R.id.driver_main_rpm);
        coolTem = findViewById(R.id.driver_main_cool_tem);
        engineLoad = findViewById(R.id.driver_main_engine_load);
        record = findViewById(R.id.driver_main_record);

        record.setOnClickListener(this);

        setting = findViewById(R.id.driver_main_setting);
        setting.setOnClickListener(this);

        presenter = new MainPresenter();
        presenter.attachView(this);
        presenter.initialize(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.checkConnect(this);
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.driver_main_setting:
                presenter.clickMenu(this, view);

                break;

            case R.id.driver_main_record :
                presenter.clickRecord(this);
                break;


        }

    }



    @Override
    public void finishActivityView() {
        finish();
    }


    @Override
    public void setSpeed(String speed) {
        this.speed.setText(speed);
    }

    @Override
    public void setVoltage(String voltage) {
        this.vol.setText(voltage);
    }

    @Override
    public void setRPM(String rpm) {
        this.rpm.setText(rpm);
    }

    @Override
    public void setCoolTem(String tem) {
        this.coolTem.setText(tem);
    }

    @Override
    public void setEngineLoad(String consume) {
        this.engineLoad.setText(consume);
    }


}
