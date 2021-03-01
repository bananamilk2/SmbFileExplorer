package com.asjm.fileexplorer.view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.asjm.fileexplorer.databinding.ActivityServerBinding;
import com.asjm.fileexplorer.entity.Server;
import com.asjm.fileexplorer.manager.DaoManager;

public class ServerEditActivity extends Activity {

    private ActivityServerBinding activityServerBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityServerBinding = ActivityServerBinding.inflate(getLayoutInflater());
        setContentView(activityServerBinding.getRoot());
    }



    public void back(View v) {
        onBackPressed();
    }

    public void save( View v){
        Server s = new Server();
        s.setName(activityServerBinding.name.getText().toString().trim());
        s.setAddress(activityServerBinding.ip.getText().toString().trim());
        s.setUsername(activityServerBinding.username.getText().toString().trim());
        s.setPassword(activityServerBinding.password.getText().toString().trim());
        DaoManager.getInstance().getDaoSession().getServerDao().insert(s);
        finish();
    }
}