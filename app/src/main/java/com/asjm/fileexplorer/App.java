package com.asjm.fileexplorer;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.asjm.fileexplorer.manager.DaoManager;
import com.asjm.lib.util.ALog;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DaoManager.getInstance().init(this);
        ALog.getInstance().init(this).setLogLevel(ALog.LOG_LEVEL_VERBOSE);
        ALog.getInstance().d(isApkInDebug(this));

        System.setProperty("jcifs.smb.client.dfs.disabled", "true");
        System.setProperty("jcifs.smb.client.soTimeout", "1000000");
        System.setProperty("jcifs.smb.client.responseTimeout", "30000");
    }

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
