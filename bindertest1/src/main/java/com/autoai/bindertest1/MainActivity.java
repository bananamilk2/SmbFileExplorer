package com.autoai.bindertest1;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;

import com.asjm.lib.aidl.ICallback;
import com.asjm.lib.aidl.IMediaManagerInterface;
import com.asjm.lib.entity.MediaBean;
import com.asjm.lib.util.ALog;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private IMediaManagerInterface mediaManagerInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.getInstance().d("onCreate");
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ALog.getInstance().d("bindService");
                Intent i = new Intent();
//                i.setComponent(new ComponentName("com.asjm.fileexplorer", "com.asjm.fileexplorer.service.MediaScanService"));
                i.setPackage("com.asjm.fileexplorer");
                i.setAction("com.asjm.service.MEDIA_SERVICE");
                getApplicationContext().bindService(i, conn, BIND_AUTO_CREATE);
            }
        }, 2000);
    }

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ALog.getInstance().d("onServiceConnected");
            mediaManagerInterface = IMediaManagerInterface.Stub.asInterface(service);

            try {
                int size = mediaManagerInterface.getSize();
                ALog.getInstance().d(size);
                MediaBean mediaBean = mediaManagerInterface.getMedia();
                ALog.getInstance().d(mediaBean.toString());
                mediaManagerInterface.regist(callback);
                mediaManagerInterface.scan();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            ALog.getInstance().d("onServiceDisconnected");
        }
    };

    private ICallback.Stub callback = new ICallback.Stub() {
        @Override
        public void onCallback(String str) throws RemoteException {
            ALog.getInstance().d(str);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mediaManagerInterface.unRegist(callback);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        getApplicationContext().unbindService(conn);
        finish();
    }
}
