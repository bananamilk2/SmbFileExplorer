package com.asjm.fileexplorer.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.base.BaseRecycleAdapter;
import com.asjm.fileexplorer.databinding.ActivityBrowseBinding;
import com.asjm.fileexplorer.entity.File;
import com.asjm.fileexplorer.entity.Server;
import com.asjm.fileexplorer.holder.FileViewHolder;
import com.asjm.fileexplorer.holder.ServerViewHolder;
import com.asjm.fileexplorer.listener.OnItemClickListener;
import com.asjm.lib.util.ALog;

import java.util.List;

import jcifs.smb.SmbFile;

public class BrowseActivity extends AppCompatActivity implements OnItemClickListener<File> {

    private ActivityBrowseBinding activityBrowseBinding;
    private Server server;
    private List<File> fileList;
    private BaseRecycleAdapter<File, FileViewHolder> adapter;

    private String baseUrl = "";        //保存根目录地址
    private SmbFile smbFile = null;     //当前目录
    private SmbFile[] subFiles = null;      //当前目录下的子文件、目录

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityBrowseBinding = ActivityBrowseBinding.inflate(getLayoutInflater());
        setContentView(activityBrowseBinding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            Bundle extra = intent.getBundleExtra("extra");
            if (extra != null) {
                server = (Server) extra.getSerializable("server");
                ALog.getInstance().d(server.toString());
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        activityBrowseBinding.recycler.setLayoutManager(layoutManager);

        adapter = new BaseRecycleAdapter<>(fileList, R.layout.item_file, FileViewHolder.class);
        adapter.setOnItemClickListener(this);

        activityBrowseBinding.recycler.setAdapter(adapter);

        //组合链接
        StringBuilder baseUrlBuilder = new StringBuilder("smb://");

        //用户名密码
        if (!TextUtils.isEmpty(server.getUsername())) {
            baseUrlBuilder.append(server.getUsername());
            baseUrlBuilder.append(':');
            baseUrlBuilder.append(server.getPassword());
            baseUrlBuilder.append('@');
        }

        //去除多余的/符号，保证最后只有一位/磨耗
        baseUrlBuilder.append(server.getAddress());
        String s = baseUrlBuilder.toString();

        while (baseUrlBuilder.lastIndexOf("/") == baseUrlBuilder.length() - 1) {
            baseUrlBuilder.deleteCharAt(baseUrlBuilder.length() - 1);
        }
        baseUrlBuilder.append('/');

        ALog.getInstance().d(s);
    }

    public void add(View view) {


    }

    @Override
    public void onItemClick(File data, View view, int position) {

    }
}
