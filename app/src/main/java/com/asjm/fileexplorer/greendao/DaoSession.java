package com.asjm.fileexplorer.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.asjm.fileexplorer.entity.FileSmb;
import com.asjm.fileexplorer.entity.Server;

import com.asjm.fileexplorer.greendao.FileSmbDao;
import com.asjm.fileexplorer.greendao.ServerDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig fileSmbDaoConfig;
    private final DaoConfig serverDaoConfig;

    private final FileSmbDao fileSmbDao;
    private final ServerDao serverDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        fileSmbDaoConfig = daoConfigMap.get(FileSmbDao.class).clone();
        fileSmbDaoConfig.initIdentityScope(type);

        serverDaoConfig = daoConfigMap.get(ServerDao.class).clone();
        serverDaoConfig.initIdentityScope(type);

        fileSmbDao = new FileSmbDao(fileSmbDaoConfig, this);
        serverDao = new ServerDao(serverDaoConfig, this);

        registerDao(FileSmb.class, fileSmbDao);
        registerDao(Server.class, serverDao);
    }
    
    public void clear() {
        fileSmbDaoConfig.clearIdentityScope();
        serverDaoConfig.clearIdentityScope();
    }

    public FileSmbDao getFileSmbDao() {
        return fileSmbDao;
    }

    public ServerDao getServerDao() {
        return serverDao;
    }

}
