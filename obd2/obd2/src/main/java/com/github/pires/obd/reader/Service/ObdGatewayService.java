package com.github.pires.obd.reader.Service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.ObdResetCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.temperature.AmbientAirTemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;
import com.github.pires.obd.exceptions.UnsupportedCommandException;
import com.github.pires.obd.reader.Data.OBD.OBDDataRepository;
import com.github.pires.obd.reader.Util.BluetoothManager;
import com.github.pires.obd.reader.Util.ObdCommandJob;
import com.github.pires.obd.reader.Util.ObdCommandJob.ObdCommandJobState;

import java.io.IOException;


public class ObdGatewayService extends AbstractGatewayService {


    private BluetoothDevice dev = null;
    private BluetoothSocket sock = null;

    public void startService(String id) throws IOException {


        //오토 페이링 핸들링 처리.

        // get the remote Bluetooth device
        final String remoteDevice = id;

        if (remoteDevice == null || "".equals(remoteDevice)) {
            stopService();
            throw new IOException();
        } else {

            final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();

            dev = btAdapter.getRemoteDevice(remoteDevice);

            btAdapter.cancelDiscovery();


            try {
                startObdConnection();
            } catch (Exception e) {
                stopService();
            }

        }
    }


    private void startObdConnection() throws IOException {

        isRunning = true;

        try {
            sock = BluetoothManager.connect(dev);
        } catch (Exception e2) {
            stopService();
            throw new IOException();
        }

        queueJob(new ObdCommandJob(new ObdResetCommand()));

        //초기화 시간.
        try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

        //더미데이터
        queueJob(new ObdCommandJob(new EchoOffCommand()));
        queueJob(new ObdCommandJob(new SelectProtocolCommand(ObdProtocols.valueOf("AUTO"))));

        //더미데이터
        queueJob(new ObdCommandJob(new AmbientAirTemperatureCommand()));


    }

    @Override
    public void queueJob(ObdCommandJob job) {
        job.getCommand().useImperialUnits(false);
        super.queueJob(job);
    }


    protected void executeQueue() throws InterruptedException {

        while (!Thread.currentThread().isInterrupted()) {
            ObdCommandJob job = null;
            try {
                job = jobsQueue.take();

                if (job.getState().equals(ObdCommandJobState.NEW)) {

                    job.setState(ObdCommandJobState.RUNNING);
                    if (sock.isConnected()) {
                        job.getCommand().run(sock.getInputStream(), sock.getOutputStream());
                    } else {
                        job.setState(ObdCommandJobState.EXECUTION_ERROR);

                    }
                }

            } catch (InterruptedException i) {
                Thread.currentThread().interrupt();
            } catch (UnsupportedCommandException u) {
                if (job != null) {
                    job.setState(ObdCommandJobState.NOT_SUPPORTED);
                }

            } catch (IOException io) {
                if (job != null) {
                    if(io.getMessage().contains("Broken pipe"))
                        job.setState(ObdCommandJobState.BROKEN_PIPE);
                    else
                        job.setState(ObdCommandJobState.EXECUTION_ERROR);
                }

            } catch (Exception e) {
                if (job != null) {
                    job.setState(ObdCommandJobState.EXECUTION_ERROR);
                }

            }

            if (job != null) {
                final ObdCommandJob job2 = job;
                OBDDataRepository.getInstance().addData(job2);
            }
        }
    }


    public void stopService() {

        jobsQueue.clear();
        isRunning = false;

        if (sock != null)
            // close socket
            try {
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        stopSelf();
    }

    public boolean isRunning() {
        return isRunning;
    }

}
