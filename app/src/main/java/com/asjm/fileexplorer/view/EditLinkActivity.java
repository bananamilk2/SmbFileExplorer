package com.asjm.fileexplorer.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.asjm.fileexplorer.databinding.ActivityServerBinding;
import com.asjm.fileexplorer.entity.Server;
import com.asjm.fileexplorer.manager.DaoManager;
import com.asjm.lib.util.ALog;

public class EditLinkActivity extends AppCompatActivity {

    private ActivityServerBinding activityServerBinding;
    private Server server;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityServerBinding = ActivityServerBinding.inflate(getLayoutInflater());
        setContentView(activityServerBinding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extra = intent.getBundleExtra("extra");
            if (extra != null) {
                server = (Server) extra.getSerializable("server");
                ALog.getInstance().d(server.toString());
                activityServerBinding.ip.setText(server.getAddress());
                activityServerBinding.name.setText(server.getName());
                activityServerBinding.username.setText(server.getUsername());
                activityServerBinding.password.setText(server.getPassword());
            }
        }
    }

    public void delete(View view){

    }

    public void save(View v) {
        ALog.getInstance().d("save");
        server.setName(activityServerBinding.name.getText().toString().trim());
        server.setAddress(activityServerBinding.ip.getText().toString().trim());
        server.setUsername(activityServerBinding.username.getText().toString().trim());
        server.setPassword(activityServerBinding.password.getText().toString().trim());
        DaoManager.getInstance().getDaoSession().getServerDao().update(server);
        finish();
    }
}
