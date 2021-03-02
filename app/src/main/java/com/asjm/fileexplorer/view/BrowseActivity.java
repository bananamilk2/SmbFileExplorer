package com.asjm.fileexplorer.view;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
import com.asjm.fileexplorer.listener.IProgressListener;
import com.asjm.fileexplorer.listener.OnItemClickListener;
import com.asjm.fileexplorer.utils.DialogHelper;
import com.asjm.fileexplorer.utils.FileUtil;
import com.asjm.lib.util.ALog;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.MyImageMsgDialog;
import com.litesuits.common.io.IOUtils;
//import com.litesuits.common.io.IOUtils;

import java.io.File;
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

        MyImageMsgDialog myImageMsgDialog = new MyImageMsgDialog(this).builder()
                .setImageLogo(getResources().getDrawable(R.mipmap.ic_launcher))
                .setMsg("连接中...");
        ImageView logoImg = myImageMsgDialog.getLogoImg();
        logoImg.setImageResource(R.drawable.connect_anim);
        AnimationDrawable connectAnimation = (AnimationDrawable) logoImg.getDrawable();
        connectAnimation.start();

        myImageMsgDialog.setCancelable(false);
        myImageMsgDialog.show();

        getRemoteDir(baseUrl, new IProgressListener() {
            @Override
            public void onComplete() {
                ALog.getInstance().d("onComplete");
                myImageMsgDialog.dismiss();
            }

            @Override
            public void onStart() {
                ALog.getInstance().d("onStart");

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getRemoteDir(String url, IProgressListener listener) {
        new Thread(() -> {
            if (listener != null)
                listener.onStart();
            try {
                ALog.getInstance().d("getRemoteDir: " + url);
                smbFile = new SmbFile(url);


                if (smbFile.isDirectory()) {
                    ArrayList<FileSmb> list = new ArrayList<>();
                    //添加返回上级目录

                    FileSmb f = new FileSmb();
                    f.setDir(true);
                    f.setFileName("../");
                    f.setIndex(-1);
                    f.setFileTime(new Date());
                    list.add(f);

                    subFiles = Arrays.stream(smbFile.listFiles()).filter((e) -> {
                        try {
                            if (e.exists())
                                return true;
                            else
                                return false;
                        } catch (SmbException ex) {
                            return false;
                        }
                    })
                            .sorted((sf1, sf2) -> {
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
                            })
                            .toArray(SmbFile[]::new);

                    for (int i = 0; i < subFiles.length; i++) {
                        SmbFile sf = subFiles[i];
                        FileSmb fs = new FileSmb();
                        fs.setIndex(i);
                        fs.setFileName(sf.getName());
                        Date time = new Date(sf.createTime());
                        long l = sf.fileIndex();
                        String canonicalPath = sf.getCanonicalPath();
                        long contentLengthLong = sf.getContentLengthLong();
                        long date = sf.getDate();
                        String dfsPath = sf.getDfsPath();
                        long diskFreeSpace = sf.getDiskFreeSpace();
                        String path = sf.getPath();
                        int type = sf.getType();

                        fs.setFileTime(time);
                        fs.setDir(sf.isDirectory());
                        fs.setFileSize(contentLengthLong);

                        list.add(fs);
//                        ALog.getInstance().d("\n" + i + ", name = " + sf.getName() + ", createTime = " + time.toString()
//                                + ", fileIndex = " + l + ", canonicalPath = " + canonicalPath + "\n"
//                                + ", contentLengthLong = " + contentLengthLong +"  " + FileUtil.formatFileSize(contentLengthLong) + ", ");
                    }
                    fileList.clear();
                    fileList.addAll(list);
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                } else {
                    throw new Exception("这不是一个目录");
                }
            } catch (Exception e) {
                ALog.getInstance().d(e.toString());
            } finally {
                if (listener != null)
                    runOnUiThread(listener::onComplete);
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
                    LoadingDialog loadingDialog = DialogHelper.loadDialog(this, "加载中...");
                    getRemoteDir(smbFile.getParent(), new IProgressListener() {
                        @Override
                        public void onComplete() {
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onStart() {

                        }
                    });
                }
//                    Rxzmvvm.toastShow("已经是顶级目录了");
            } else { //点击目录进入到子目录
                //弹出加载进度框
                LoadingDialog loadingDialog = DialogHelper.loadDialog(this, "加载中...");
                getRemoteDir(subFiles[file.getIndex()].getPath(), new IProgressListener() {
                    @Override
                    public void onComplete() {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onStart() {

                    }
                });
            }
        } else {
            DialogHelper.bottomDialog(this, file.getFileName(),
                    new DialogHelper.SubItem("下载", (w1) -> {
                        //弹出下载进度框
                        LoadingDialog loadingDialog = DialogHelper.loadDialog(BrowseActivity.this, "下载中...");

                        File myRoot = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "smbClient");
                        if (!myRoot.exists()) {
                            boolean re = myRoot.mkdirs();
                            ALog.getInstance().d(re + "");
                        }
                        new Thread(() -> {
                            try (SmbFileInputStream fi = new SmbFileInputStream(subFiles[file.getIndex()]);
                                 FileOutputStream fo = new FileOutputStream(new File(myRoot, file.getFileName()))) {
                                IOUtils.copy(fi, fo);
                                subFiles[file.getIndex()].close();
                                ALog.getInstance().d("download success");
//                                view.runOnUiThread(() -> Rxzmvvm.toastShow("下载成功:" + myRoot.getPath() + file.getFileName()));
                            } catch (Exception e) {
                                ALog.getInstance().d("download fail:" + e.toString());
//                                view.runOnUiThread(() -> Rxzmvvm.toastShow("下载失败"));
                            } finally {
                                loadingDialog.dismiss();
                            }
                        }).start();

                    }));
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
//                    }));
        }
    }
}
