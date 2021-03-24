package com.czbix.smbsteamer.service;

import android.app.IntentService;
import android.content.Intent;

import com.asjm.lib.util.ALog;
import com.czbix.smbsteamer.helper.HttpServer;

import java.io.IOException;

import androidx.annotation.Nullable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class StreamService extends IntentService {
    private static final int SLEEP_SECONDS = 60 * 1000;
    private static boolean mRunning;
    private static volatile boolean mStop;

    public StreamService() {
        super("StreamService");
        ALog.getInstance().d("StreamService");
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
        ALog.getInstance().d("onHandleIntent");
        mRunning = true;
        final HttpServer httpServer = new HttpServer();
        try {
            httpServer.start();
            while (true) {
                if (mStop) {
                    mStop = false;
                    break;
                }
                Thread.sleep(SLEEP_SECONDS);
                ALog.getInstance().d("loop");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            // ignore it
        }
        ALog.getInstance().d("stop");
        httpServer.stop();
        mRunning = false;
    }

    public static void stop() {
        mStop = true;
    }

    public static boolean isRunning() {
        return mRunning;
    }
}
