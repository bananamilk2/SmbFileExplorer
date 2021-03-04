package com.asjm.fileexplorer.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.base.BaseRecycleAdapter;
import com.asjm.fileexplorer.databinding.ActivityBrowseBinding;
import com.asjm.fileexplorer.entity.FileSmb;
import com.asjm.fileexplorer.entity.Server;
import com.asjm.fileexplorer.holder.FileViewHolder;
import com.asjm.fileexplorer.listener.IProgressListener;
import com.asjm.fileexplorer.listener.OnItemClickListener;
import com.asjm.fileexplorer.listener.OnItemLongClickListener;
import com.asjm.fileexplorer.utils.DialogHelper;
import com.asjm.lib.util.ALog;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.MyImageMsgDialog;
import com.litesuits.common.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import hugo.weaving.DebugLog;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

//import com.litesuits.common.io.IOUtils;

public class BrowseActivity extends AppCompatActivity implements OnItemClickListener<FileSmb>, OnItemLongClickListener<FileSmb> {

    private ActivityBrowseBinding activityBrowseBinding;
    private Server server;
    private List<FileSmb> fileList = new ArrayList<>();
    private BaseRecycleAdapter<FileSmb, FileViewHolder> adapter;

    private String baseUrl = "";        //保存根目录地址
    private SmbFile smbFile = null;     //当前目录
    private SmbFile[] subFiles = null;      //当前目录下的子文件、目录
    private HandlerThread workThread;
    private Handler workHandler;

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
        adapter.setOnItemLongClickListener(this);

        workThread = new HandlerThread("browse_thread");
        workThread.start();
        workHandler = new Handler(workThread.getLooper());

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

                ALog.getInstance().d("file count : " + smbFile.listFiles().length + "  " + smbFile.isDirectory());
                if (smbFile.isDirectory()) {
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

                    ALog.getInstance().d("sort finish");


                    List<FileSmb> fileSmbs = copyList(subFiles);

                    ALog.getInstance().d("finish " + fileSmbs.size());
                    fileList.clear();
                    fileList.addAll(fileSmbs);
//                    Collections.addAll(fileList, subFiles);
                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                        activityBrowseBinding.recycler.scrollToPosition(0);
                    });

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

    @DebugLog
    private List<FileSmb> copyList(SmbFile[] subFiles) {
        ALog.getInstance().e("copylist : " + subFiles.length);
        long start = System.currentTimeMillis();
        ArrayList<FileSmb> list = new ArrayList<>();
        FileSmb f = new FileSmb();
        f.setDir(true);
        f.setName("../");
        f.setIndex(-1);
        f.setDate(new Date().getTime());
        f.setSize(0L);
        list.add(f);
        try {
            for (int i = 0; i < subFiles.length; i++) {
                SmbFile sf = subFiles[i];
                FileSmb fs = new FileSmb();
                fs.setIndex(i);
                fs.setName(sf.getName());
                fs.setDate(sf.getDate());
                fs.setDir(sf.isDirectory());
                fs.setSize(sf.getContentLengthLong());
                fs.setPath(sf.getPath());
                list.add(fs);
            }
        } catch (Exception e) {
            ALog.getInstance().e(e.toString());
        }
        ALog.getInstance().i("time = " + (System.currentTimeMillis() - start));
        return list;
    }

    private void play(FileSmb file) {
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(android.content.Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.parse(file.getPath()), "image/*");
//        startActivity(intent);

        Intent it = new Intent(Intent.ACTION_SEND);
        it.putExtra(Intent.EXTRA_TEXT, "分享测试");
        it.setType("text/plain");
        startActivity(Intent.createChooser(it, "分享内容到"));

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
                    LoadingDialog loadingDialog = DialogHelper.loadDialog(BrowseActivity.this, "加载中...");
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
                LoadingDialog loadingDialog = DialogHelper.loadDialog(BrowseActivity.this, "加载中...");
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
            play(file);
        }
    }

    @Override
    public boolean onItemLongClick(FileSmb file, View view, int position) {
        ALog.getInstance().d("onItemLongClick: " + position);
        if (!file.isDir()) {
            DialogHelper.bottomDialog(BrowseActivity.this, file.getName(),
                    new DialogHelper.SubItem("下载", (w1) -> {
                        //弹出下载进度框
                        LoadingDialog loadingDialog = DialogHelper.loadDialog(BrowseActivity.this, "下载中...");

                        int REQUEST_EXTERNAL_STORAGE = 1;
                        String[] PERMISSIONS_STORAGE = {
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        };
                        int permission = ActivityCompat.checkSelfPermission(BrowseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            // We don't have permission so prompt the user
                            ActivityCompat.requestPermissions(
                                    BrowseActivity.this,
                                    PERMISSIONS_STORAGE,
                                    REQUEST_EXTERNAL_STORAGE
                            );
                            ALog.getInstance().d("requestPermissions");
                        } else
                            ALog.getInstance().d("permission");


                        File myRoot = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "smbClient");
                        if (!myRoot.exists()) {
                            boolean re = myRoot.mkdirs();
                            ALog.getInstance().d(re + "");
                        }
                        new Thread(() -> {
                            try (SmbFileInputStream fi = new SmbFileInputStream(subFiles[file.getIndex()]);
                                 FileOutputStream fo = new FileOutputStream(new File(myRoot, file.getName()))) {
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
        return true;
    }
}
