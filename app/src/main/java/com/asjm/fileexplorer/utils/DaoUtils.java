package com.asjm.fileexplorer.utils;

import com.asjm.fileexplorer.entity.File;
import com.asjm.fileexplorer.greendao.FileDao;
import com.asjm.fileexplorer.manager.CommonDaoUtils;
import com.asjm.fileexplorer.manager.DaoManager;

public class DaoUtils {
    private volatile static DaoUtils instance = new DaoUtils();
    private CommonDaoUtils<File> daoUtils;

    public static DaoUtils getInstance() {
        return instance;
    }

    private DaoUtils() {
        DaoManager manager = DaoManager.getInstance();
        FileDao _UserDao = manager.getDaoSession().getFileDao();
        daoUtils = new CommonDaoUtils<>(File.class, _UserDao);
    }

    public CommonDaoUtils<File> getDaoUtils() {
        return daoUtils;
    }

}
