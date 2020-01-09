package com.github.pires.obd.reader.Connect;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.pires.obd.reader.R;

public class ConnectActivity extends AppCompatActivity implements ConnectContract.View {


    private RecyclerView connectDeviceRecyclerview;
    private LinearLayoutManager connectDeviceLayoutManager;
    private ConnectAdapter connectDeviceAdapter;

    private ConnectPresenter connectDevicePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        /* adapter */
        connectDeviceRecyclerview =  findViewById(R.id.connect_rectclerview);
        connectDeviceLayoutManager = new LinearLayoutManager(this);
        connectDeviceLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        connectDeviceAdapter = new ConnectAdapter(this);
        connectDeviceRecyclerview.setLayoutManager(connectDeviceLayoutManager);
        connectDeviceRecyclerview.setItemAnimator(new DefaultItemAnimator());
        connectDeviceRecyclerview.setAdapter(connectDeviceAdapter);


        connectDevicePresenter = new ConnectPresenter();
        connectDevicePresenter.attachView(this);

        connectDevicePresenter.attachAdapterModel(connectDeviceAdapter);
        connectDevicePresenter.attachAdapterView(connectDeviceAdapter);

        connectDevicePresenter.initialize(this);

    }



    @Override
    public void finishActivityView() {

        finish();

    }


}
