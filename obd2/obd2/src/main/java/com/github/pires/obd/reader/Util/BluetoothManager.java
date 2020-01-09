package com.github.pires.obd.reader.Util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class BluetoothManager {
	
    private static final String TAG = BluetoothManager.class.getName();

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static BluetoothSocket connect(BluetoothDevice dev) throws IOException {
    	BluetoothSocket sock = null;
        BluetoothSocket sockFallback = null;


    	try {
    		sock = dev.createRfcommSocketToServiceRecord(MY_UUID);
    		sock.connect();
        } catch (Exception e1) {

            Class<?> clazz = sock.getRemoteDevice().getClass();
            Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};

            try {

                Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                Object[] params = new Object[]{Integer.valueOf(1)};
                sockFallback = (BluetoothSocket) m.invoke(sock.getRemoteDevice(), params);
                sockFallback.connect();
                sock = sockFallback;

            } catch (Exception e2) {
                Log.d("LDK", "connect_error: ");
                throw new IOException(e2.getMessage());
            }
        }
    	return sock;
    }
}