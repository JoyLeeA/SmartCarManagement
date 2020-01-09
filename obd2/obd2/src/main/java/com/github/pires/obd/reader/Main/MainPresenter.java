package com.github.pires.obd.reader.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.reader.API.APIHelper.OBDAPI;
import com.github.pires.obd.reader.Connect.ConnectActivity;
import com.github.pires.obd.reader.Data.OBD.OBDDataModel;
import com.github.pires.obd.reader.Data.OBD.OBDDataRepository;
import com.github.pires.obd.reader.Data.OBD.OBDObserver;
import com.github.pires.obd.reader.R;
import com.github.pires.obd.reader.Record.RecordActivity;
import com.github.pires.obd.reader.Util.ObdConfig;
import com.github.pires.obd.reader.Service.AbstractGatewayService;
import com.github.pires.obd.reader.Util.ObdCommandJob;
import com.github.pires.obd.reader.Service.ObdGatewayService;

import java.io.IOException;

public class MainPresenter implements MainContract.Presenter,OBDObserver {

    public static  AbstractGatewayService service;
    public static boolean isServiceBound;
    public static BluetoothDevice item;
    private MainContract.View view;
    private Handler hd = new Handler();
    private Activity activity;
    public static String id;
    private int i=0;

    @Override
    public void attachView(MainContract.View view) {
        this.view = view;
    }

    private final Runnable mQueueCommands = new Runnable() {
        public void run() {
            if (service != null && service.isRunning() && service.queueEmpty()) {
                queueCommands();
            }

            new Handler().postDelayed(mQueueCommands, 1000);
        }
    };



    @Override
    public void initialize(Activity activity) {

        this.activity = activity;
        OBDDataRepository.getInstance().add(this);
        new Handler().post(mQueueCommands);

    }

    private void queueCommands() {
        if (isServiceBound) {
            for (ObdCommand Command : ObdConfig.getCommands()) {
                service.queueJob(new ObdCommandJob(Command));
            }
        }
    }


    @Override
    public void clickMenu(final Context context, View view) {

        PopupMenu popup = new PopupMenu(context, view);

        MenuInflater inflater = popup.getMenuInflater();
        Menu menu = popup.getMenu();

        inflater.inflate(R.menu.drive_main_menu, menu);


        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch(item.getItemId()){
                    case R.id.drive_main_menu_connect:
                        context.startActivity(new Intent(context,ConnectActivity.class));
                        break;

                    case R.id.drive_main_menu_logout:

                        AlertDialog.Builder alertdialog = new AlertDialog.Builder(context);

                        alertdialog.setTitle("로그아웃");
                        alertdialog.setMessage("\n 로그아웃 하시겠습니까?");

                        alertdialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        alertdialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertDialog alert = alertdialog.create();
                        alert.show();

                        break;


                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void checkConnect(final Context context) {

        if(item != null &&  !isServiceBound ){

            Log.d("LDK", "checkConnect: ");
            Intent serviceIntent = new Intent(context, ObdGatewayService.class);
            context.bindService(serviceIntent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder binder) {
                    MainPresenter.isServiceBound = true;

                    service = ((AbstractGatewayService.AbstractGatewayServiceBinder) binder).getService();
                    service.setContext(context);

                    try {
                        service.startService(id);

                    } catch (IOException ioe) {
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            }, Context.BIND_AUTO_CREATE);
        }

    }

    @Override
    public void clickRecord(Context context) {
        context.startActivity(new Intent(context, RecordActivity.class));
    }


    @Override
    public void update(final OBDDataModel data) {

        hd.post(new Runnable() {
            @Override
            public void run() {

                i++;
                if(i > 10){
                    OBDAPI.getInstance().call(activity,data);
                    i =0;
                }

                view.setSpeed(data.getSpeed());
                view.setVoltage(data.getVoltage());
                view.setRPM(data.getRpm());
                view.setCoolTem(data.getCoolantTemperature());
                view.setEngineLoad(data.getEngineLoad());

            }
        });
    }

}
