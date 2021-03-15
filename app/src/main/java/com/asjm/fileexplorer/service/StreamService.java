package com.asjm.fileexplorer.service;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.asjm.lib.util.ALog;

public class StreamService extends JobIntentService {

    private boolean running = false;
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        ALog.getInstance().d("onHandleWork");
        running = true;
        final HttpServer httpServer = new HttpServer();

    }
}
