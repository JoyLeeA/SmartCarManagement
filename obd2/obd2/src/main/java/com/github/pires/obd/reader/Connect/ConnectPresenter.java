package com.github.pires.obd.reader.Connect;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.github.pires.obd.reader.Main.MainPresenter;

import java.util.ArrayList;

public class ConnectPresenter implements ConnectContract.Presenter,ConnectAdapterContract.OnItemClickListener {

    private ConnectAdapterContract.Model adapterModel;
    private ConnectAdapterContract.View adapterView;
    private ConnectContract.View view;

    private BroadcastReceiver bc = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int frag = 0;

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                Log.d("LDK", "onReceive: "+device.getName());

                if(device.getName() != null){

                    ArrayList<BluetoothDevice> datas = adapterModel.getItems();

                    for(int i=0;i<datas.size();i++){
                        if(datas.get(i).getName().equals(device.getName())){
                            frag= 1;
                        }
                    }

                    if(frag == 0){
                        adapterModel.addItem(device);
                        adapterView.notifyAdapter();
                    }

                    frag = 0;

                }
            }
        }
    };

    @Override
    public void attachView(ConnectContract.View view) {
        this.view = view;
    }

    @Override
    public void initialize(Activity activity) {

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(bc, filter);

    }

    @Override
    public void attachAdapterModel(ConnectAdapterContract.Model adapterModel) {
        this.adapterModel = adapterModel;
    }

    @Override
    public void attachAdapterView(ConnectAdapterContract.View adapterView) {
        this.adapterView = adapterView;
        this.adapterView.setOnClickListener(this);
    }


    @Override
    public void onConnectItemClick(Context context, BluetoothDevice item, int position) {
        MainPresenter.id = item.getAddress();
        context.unregisterReceiver(bc);
        MainPresenter.item = item;
        view.finishActivityView();
    }
}
