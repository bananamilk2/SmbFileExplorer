package com.asjm.fileexplorer.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.base.BaseRecycleAdapter;
import com.asjm.fileexplorer.databinding.ActivityBrowseBinding;
import com.asjm.fileexplorer.entity.FileSmb;
import com.asjm.fileexplorer.entity.Server;
import com.asjm.fileexplorer.holder.FileViewHolder;
import com.asjm.fileexplorer.listener.OnItemClickListener;
import com.asjm.lib.util.ALog;
import com.litesuits.common.io.IOUtils;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

public class BrowseActivity extends AppCompatActivity implements OnItemClickListener<FileSmb> {

    private ActivityBrowseBinding activityBrowseBinding;
    private Server server;
    private List<FileSmb> fileList = new ArrayList<>();
    private BaseRecycleAdapter<FileSmb, FileViewHolder> adapter;

    private String baseUrl = "";        //保存根目录地址
    private SmbFile smbFile = null;     //当前目录
    private SmbFile[] subFiles = null;      //当前目录下的子文件、目录

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityBrowseBinding = ActivityBrowseBinding.inflate(getLayoutInflater());
        setContentView(activityBrowseBinding.getRoot());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        activityBrowseBinding.recycler.setLayoutManager(layoutManager);

        adapter = new BaseRecycleAdapter<>(fileList, R.layout.item_file, FileViewHolder.class);
        activityBrowseBinding.recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extra = intent.getBundleExtra("extra");
            if (extra != null) {
                server = (Server) extra.getSerializable("server");
                ALog.getInstance().d(server.toString());
            }
        }

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

        baseUrl = baseUrlBuilder.toString();

        getRemoteDir(baseUrl);
    }

    public void getRemoteDir(String url) {
        //开启链接服务器的线程
        new Thread(() -> {
            try {
                ALog.getInstance().d("getRemoteDir: " + url);

                smbFile = new SmbFile(url);

                long contentLengthLong = smbFile.getContentLengthLong();
                ALog.getInstance().d(contentLengthLong + "");
                //判断是否是一个目录
                if (smbFile.isDirectory()) {
                    ArrayList<FileSmb> list = new ArrayList<>();
                    ALog.getInstance().d("");
                    //添加返回上级目录

                    FileSmb f = new FileSmb();
                    f.setDir(true);
                    f.setFileName("../");
                    f.setIndex(-1);
                    list.add(f);

                    //获取当前目录下的文件、目录列表(仅存在的)
                    SmbFile[] smbFiles = smbFile.listFiles();
                    ALog.getInstance().d(smbFiles.length + "");

                    subFiles = Arrays.stream(smbFile.listFiles()).filter((e) -> {
                        try {
                            if (e.exists())
                                return true;
                            else
                                return false;
                        } catch (SmbException ex) {
                            return false;
                        }
                    }).sorted((sf1, sf2) -> {
                        //进行排序，目录在前，文件在后。按名字排序
                        try {
                            //同为目录或者文件
                            if (sf1.isDirectory() == sf2.isDirectory()) {
                                return sf1.getName().compareTo(sf2.getName());
                            } else {
                                //一个是目录一个是文件，则目录在前
                                return sf1.isDirectory() ? -1 : 1;
                            }
                        } catch (SmbException e) {
                            return 0;
                        }
                    }).toArray(SmbFile[]::new);
                    //依次加入显示列表
                    for (int i = 0; i < subFiles.length; i++) {
                        SmbFile sf = subFiles[i];
                        FileSmb fs = new FileSmb();
                        fs.setIndex(i);
                        fs.setFileName(sf.getName());
                        fs.setFileTime(new Date(sf.createTime()));
                        fs.setDir(sf.isDirectory());
                        list.add(fs);
                        ALog.getInstance().d("addfile" + sf.toString());
                    }
                    //更新界面
                    fileList.clear();
                    fileList.addAll(list);
                    runOnUiThread(() -> adapter.notifyDataSetChanged());

                } else {
                    throw new Exception("这不是一个目录");
                }
            } catch (Exception e) {

            } finally {

            }
        }).start();
    }

    public void add(View view) {

    }

    @Override
    public void onItemClick(FileSmb file, View view, int position) {
        ALog.getInstance().d(position + "");
        if (file.isDir()) {
            if (file.getIndex() == -1) {      //点击的返回上一层
                //将当前目录和根目录比较，如果相等则停止返回上一级
                if (!smbFile.getPath().equals(baseUrl)) {
                    //弹出加载进度框
//                    LoadingDialog loadingDialog = DialogHelper.loadDialog(view, "加载中...");
                    getRemoteDir(smbFile.getParent());
                }
//                    Rxzmvvm.toastShow("已经是顶级目录了");
            } else { //点击目录进入到子目录
                //弹出加载进度框
//                LoadingDialog loadingDialog = DialogHelper.loadDialog(view, "加载中...");
                getRemoteDir(subFiles[file.getIndex()].getPath());
            }
        } else {
            //点击文件,弹出选项框
//            DialogHelper.bottomDialog(view, file.getName(),
//                    new DialogHelper.SubItem("下载", (w1) -> {
//
//
//                        //弹出下载进度框
//                        LoadingDialog loadingDialog = DialogHelper.loadDialog(view, "下载中...");
//
//                        java.io.File myRoot = new java.io.File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "smbClient");
//                        if (!myRoot.exists()) {
//                            boolean re = myRoot.mkdirs();
//                            System.out.println(re);
//                        }
//
//
//                        new Thread(() -> {
//                            try (SmbFileInputStream fi = new SmbFileInputStream(subFiles[file.getIndex()]);
//                                 FileOutputStream fo = new FileOutputStream(new java.io.File(myRoot, file.getName()))) {
//
//                                IOUtils.copy(fi, fo);
//                                subFiles[file.getIndex()].close();
//                                view.runOnUiThread(() -> Rxzmvvm.toastShow("下载成功:" + myRoot.getPath() + file.getName()));
//                            } catch (Exception e) {
//
//                                view.runOnUiThread(() -> Rxzmvvm.toastShow("下载失败"));
//                            } finally {
//                                loadingDialog.dismiss();
//                            }
//                        }).start();
//
//
//                    }),
//                    new DialogHelper.SubItem("删除", (w1) -> {
//                        //打开确认弹出
//                        DialogHelper.verifyDialog(view, "删除 " + file.getName(), () -> {
//                            //弹出删除进度框
//                            LoadingDialog loadingDialog = DialogHelper.loadDialog(view, "删除中...");
//                            new Thread(() -> {
//                                try {
//                                    subFiles[file.getIndex()].delete();
//                                    getRemoteDir(smbFile.getPath(), null, null);
//                                    view.runOnUiThread(() -> Rxzmvvm.toastShow("删除成功"));
//                                } catch (SmbException e) {
//                                    view.runOnUiThread(() -> Rxzmvvm.toastShow("删除失败"));
//                                } finally {
//                                    loadingDialog.dismiss();
//                                }
//                            }).start();
//                        });
//
//
//                    }));


        }
    }
}
