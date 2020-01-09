package com.github.pires.obd.reader.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.github.pires.obd.reader.Util.ObdCommandJob;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public abstract class AbstractGatewayService extends Service {

    private final IBinder binder = new AbstractGatewayServiceBinder();
    protected Context ctx;
    protected boolean isRunning = false;
    protected Long queueCounter = 0L;
    protected BlockingQueue<ObdCommandJob> jobsQueue = new LinkedBlockingQueue<>();

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                executeQueue();
            } catch (InterruptedException e) {
                t.interrupt();
            }
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        t.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        t.interrupt();
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean queueEmpty() {
        return jobsQueue.isEmpty();
    }

    public void queueJob(ObdCommandJob job) {
        queueCounter++;

        job.setId(queueCounter);
        try {
            jobsQueue.put(job);
        } catch (InterruptedException e) {
            job.setState(ObdCommandJob.ObdCommandJobState.QUEUE_ERROR);
        }
    }

    public void setContext(Context c) {
        ctx = c;
    }

    abstract protected void executeQueue() throws InterruptedException;

    abstract public void startService(String id) throws IOException;

    abstract public void stopService();

    public class AbstractGatewayServiceBinder extends Binder {
        public AbstractGatewayService getService() {
            return AbstractGatewayService.this;
        }
    }
}
