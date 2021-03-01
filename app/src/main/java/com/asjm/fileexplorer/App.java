package com.asjm.fileexplorer;

import android.app.Application;

import com.asjm.fileexplorer.manager.DaoManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DaoManager.getInstance().init(this);
    }
}
