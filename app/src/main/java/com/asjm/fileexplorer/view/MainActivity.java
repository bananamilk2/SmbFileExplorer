package com.asjm.fileexplorer.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.base.BaseRecycleAdapter;
import com.asjm.fileexplorer.databinding.ActivityMainBinding;
import com.asjm.fileexplorer.entity.Server;
import com.asjm.fileexplorer.holder.ServerViewHolder;
import com.asjm.fileexplorer.listener.OnItemClickListener;
import com.asjm.fileexplorer.listener.OnPermissionCallBack;
import com.asjm.fileexplorer.manager.DaoManager;
import com.asjm.fileexplorer.utils.OSUtils;
import com.asjm.fileexplorer.utils.PermissionHelper;
import com.asjm.fileexplorer.utils.SystemBarTintManager;
import com.asjm.lib.util.ALog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnItemClickListener<Server>, OnPermissionCallBack {

    private List<Server> serverList = new ArrayList<>();
    private ActivityMainBinding activityMainBinding;
    private BaseRecycleAdapter<Server, ServerViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        activityMainBinding.recycler.setLayoutManager(layoutManager);

        adapter = new BaseRecycleAdapter<>(serverList, R.layout.item_link, ServerViewHolder.class);
        activityMainBinding.recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        PermissionHelper.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, this);
        PermissionHelper.requestPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onShow();
    }

    private void onShow() {
        ALog.getInstance().d("onShow");
        //TODO async
        List<Server> list = DaoManager.getInstance().getDaoSession().getServerDao().loadAll();
        ALog.getInstance().d("list size = " + list.size());
        serverList.clear();
        serverList.addAll(list);
        adapter.notifyDataSetChanged();
    }

    public void add(View view) {
        turnTo(this, ServerEditActivity.class, null, true);
    }

    public void openMenu(View view) {
        activityMainBinding.mainSlideMenu.openLeftSlide();
    }

    public void linkTo(View view) {
//        ALog.getInstance().d(view.toString());
//        int tag = (int) view.getTag();
//        ALog.getInstance().d(tag + "");
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("server", serverList.get(tag));
//        turnTo(this, BrowseActivity.class, null, true);
    }

    public void toEdit(View view) {
        int tag = (int) view.getTag();
        ALog.getInstance().d(tag + "");
        Bundle bundle = new Bundle();
        bundle.putSerializable("server", serverList.get(tag));
        turnTo(this, EditLinkActivity.class, bundle, true);
    }

    public <K> void turnTo(Activity activity, Class<K> kClass, Bundle bundle, boolean isNeedReturn) {
        Intent intent = new Intent(activity, kClass);
        if (bundle != null)
            intent.putExtra("extra", bundle);
        if (isNeedReturn) {
            activity.startActivityForResult(intent, 0);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(Server data, View view, int position) {
        ALog.getInstance().d(data + " " + position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("server", data);
        turnTo(this, BrowseActivity.class, bundle, true);
    }

    @Override
    public void success() {
        ALog.getInstance().d("success");
    }
}