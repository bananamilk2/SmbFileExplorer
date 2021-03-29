package com.asjm.fileexplorer.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.asjm.lib.util.ALog;

public class TestIntentService extends IntentService {

    public TestIntentService() {
        super("");
    }

    private int count;

    public TestIntentService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ALog.getInstance().d("onCreate");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        ALog.getInstance().d("onStartCommand: " + flags + " " + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        ALog.getInstance().d("onStart: " +startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        ALog.getInstance().d("onBind");
        return super.onBind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        ALog.getInstance().d("onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ALog.getInstance().d("onDestroy");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ALog.getInstance().d("onHandleIntent:" + intent.getAction());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        count++;
        ALog.getInstance().d("handlerFinish");
    }
}
