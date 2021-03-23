package com.asjm.fileexplorer.service;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import com.asjm.fileexplorer.http.HttpServer;
import com.asjm.lib.util.ALog;

import java.io.IOException;

public class StreamService extends IntentService {

    private boolean running = false;
    private static boolean flagRunning;
    private static volatile boolean stop;
    private static final int SLEEP_SECONDS = 60 * 1000;

    public StreamService(String name) {
        super(name);
    }

    public StreamService() {
        super("");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ALog.getInstance().d("onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ALog.getInstance().d("onDestroy");
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        ALog.getInstance().d("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        ALog.getInstance().d("onHandleWork");
        running = true;
        final HttpServer httpServer = new HttpServer();
        try {
            ALog.getInstance().d("onHandleWork");
            httpServer.start();
            while (true) {
                if (stop) {
                    stop = false;
                    break;
                }
                Thread.sleep(SLEEP_SECONDS);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            // ignore it
        }
        httpServer.stop();
        flagRunning = false;
    }

    public static void stop() {
        stop = true;
    }

    public static boolean isRunning() {
        return flagRunning;
    }
}
