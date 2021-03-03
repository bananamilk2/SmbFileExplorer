package com.asjm.fileexplorer;

import android.app.Application;

import com.asjm.fileexplorer.manager.DaoManager;
import com.asjm.lib.util.ALog;

import hugo.weaving.internal.Hugo;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DaoManager.getInstance().init(this);
        ALog.getInstance().init(this).setLogLevel(ALog.LOG_LEVEL_VERBOSE);
    }
}
