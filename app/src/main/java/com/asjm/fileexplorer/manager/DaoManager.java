package com.asjm.fileexplorer.manager;

import android.app.Application;

import com.asjm.fileexplorer.greendao.DaoMaster;
import com.asjm.fileexplorer.greendao.DaoSession;

public class DaoManager {
    private static final String DB_NAME = "file_db";
    private static DaoManager instance;
    private Application applicationContext;

    private DaoMaster daoMaster;
    private DaoMaster.DevOpenHelper helper;
    private DaoSession daoSession;

    public static DaoManager getInstance() {
        if (instance == null) {
            synchronized (DaoManager.class) {
                if (instance == null) {
                    instance = new DaoManager();
                }
            }
        }
        return instance;
    }

    private DaoManager() {

    }

    public void init(Application app) {
        this.applicationContext = app;
    }

    public DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            synchronized (DaoManager.this) {
                helper = new DaoMaster.DevOpenHelper(applicationContext, DB_NAME);
                daoMaster = new DaoMaster(helper.getWritableDatabase());
            }
        }
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public void closeConnection() {
        closeHelper();
        closeDaoSession();
    }

    private void closeDaoSession() {
        if (daoSession != null) {
            daoSession.clear();
        }
    }

    private void closeHelper() {
        if (helper != null) {
            helper.close();
        }
    }
}
