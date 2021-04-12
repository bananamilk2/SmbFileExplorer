package com.asjm.fileexplorer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;

import com.asjm.lib.aidl.ICallback;
import com.asjm.lib.aidl.IMediaManagerInterface;
import com.asjm.lib.entity.MediaBean;
import com.asjm.lib.util.ALog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MediaScanService extends Service {

    private RemoteCallbackList<ICallback> remoteCallbackList = new RemoteCallbackList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        IBinder binder = new MyBinder();
        ALog.getInstance().d("onBind: " + binder);
        return binder;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        ALog.getInstance().d("onUnbindService");
        return super.onUnbind(intent);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        ALog.getInstance().d("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ALog.getInstance().d("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ALog.getInstance().d("onDestroy");
    }

    class MyBinder extends IMediaManagerInterface.Stub {

        @Override
        public void scan() throws RemoteException {
            ALog.getInstance().d("scan");

            new Handler(getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    int N = remoteCallbackList.beginBroadcast();
                    ALog.getInstance().d(N);
                    for (int i = 0; i < N; i++) {
                        try {
                            remoteCallbackList.getBroadcastItem(i).onCallback("call");
                        } catch (RemoteException e) {
                            ALog.getInstance().d(e.toString());
                        }
                    }
                }
            }, 1000);
        }

        @Override
        public boolean scanForResult() throws RemoteException {
            ALog.getInstance().d("scanForResult");
            return true;
        }

        @Override
        public int getSize() throws RemoteException {
            ALog.getInstance().d("getSize");
            return 1;
        }

        @Override
        public MediaBean getMedia() throws RemoteException {
            return new MediaBean(1, "howard");
        }

        @Override
        public void regist(ICallback callback) throws RemoteException {
            ALog.getInstance().d("regist");
            remoteCallbackList.register(callback);
        }

        @Override
        public void unRegist(ICallback callback) throws RemoteException {
            ALog.getInstance().d("unRegist");
            remoteCallbackList.unregister(callback);
        }

//        @Override
//        public IBinder asBinder() {
//            return null;
//        }
    }
}
