package com.github.pires.obd.reader.Connect;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.util.ArrayList;

public interface ConnectAdapterContract {

    interface View {
        void setOnClickListener(ConnectAdapterContract.OnItemClickListener clickListener);
        void notifyAdapter();
    }

    interface Model {
        void addItems(ArrayList<BluetoothDevice> Items);
        void addItem(BluetoothDevice Item);
        void clearItem();
        BluetoothDevice getItem(int position);
        ArrayList<BluetoothDevice> getItems();
    }

    interface OnItemClickListener {
        void onConnectItemClick(Context context,BluetoothDevice item, int position);
    }

}
