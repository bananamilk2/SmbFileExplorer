package com.asjm.fileexplorer.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.asjm.fileexplorer.BuildConfig;
import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.databinding.ActivityFragmentBinding;
import com.asjm.fileexplorer.entity.Server;
import com.asjm.fileexplorer.service.TestIntentService;
import com.asjm.fileexplorer.ui.AddServerDialog;
import com.asjm.fileexplorer.ui.fragment.ExpandListFragment;
import com.asjm.fileexplorer.ui.fragment.ScrollViewWithListViewFragment;
import com.asjm.fileexplorer.ui.fragment.ServerListFragment;
import com.asjm.fileexplorer.ui.fragment.TestFragment;
import com.asjm.fileexplorer.ui.fragment.TreeViewFragment;
import com.asjm.lib.aidl.IMediaManagerInterface;
import com.asjm.lib.util.ALog;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class FragmentActivity extends BaseActivity implements AddServerDialog.Listener, ServerListFragment.Listener, View.OnClickListener, AdapterView.OnItemClickListener {

    private ActivityFragmentBinding activityFragmentBinding;
    private Fragment currentFragment;
    private LinkedHashMap<String, Class<?>> listItems = new LinkedHashMap<>();
    private List<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.getInstance().d("onCreate");

        activityFragmentBinding = ActivityFragmentBinding.inflate(getLayoutInflater());
        setContentView(activityFragmentBinding.getRoot());
        initMenu();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        ALog.getInstance().d("dispatchTouchEvent: " + ev.getAction());
//        return super.dispatchTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        ALog.getInstance().d("onTouchEvent: " + event.getAction());
//        return super.onTouchEvent(event);
//    }

    @Override
    public void onStart() {
        super.onStart();
        ALog.getInstance().d("onStart");
    }



    @Override
    public void onResume() {
        super.onResume();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent("com.asjm.service.MEDIA_SERVICE");
                intent.setPackage("com.asjm.fileexplorer");
//                intent.setComponent(new ComponentName("com.asjm.fileexplorer", "com.asjm.fileexplorer.service.MediaScanService"));
                bindService(intent, new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        ALog.getInstance().d("onServiceConnected: " + service);
                        IMediaManagerInterface iMediaManagerInterface = IMediaManagerInterface.Stub.asInterface(service);

                        ALog.getInstance().d("onServiceConnected: " + iMediaManagerInterface);
                        int size = 0;
                        try {
                            size = iMediaManagerInterface.getSize();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        ALog.getInstance().d(size);
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                }, BIND_AUTO_CREATE);
            }
        }, 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        ALog.getInstance().d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        ALog.getInstance().d("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ALog.getInstance().d("onDestroy");
    }

    private void initMenu() {
        listItems.put("ExpandListFragment", ExpandListFragment.class);
        listItems.put("TestFragment", TestFragment.class);
        listItems.put("TreeViewFragment", TreeViewFragment.class);
        listItems.put("ScrollViewWithListViewFragment", ScrollViewWithListViewFragment.class);
        list = new ArrayList(listItems.keySet());

        activityFragmentBinding.leftList.setAdapter(new SimpleAdapter(this, list));
        activityFragmentBinding.leftList.setOnItemClickListener(this);
        activityFragmentBinding.leftList.setDividerHeight(1);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ALog.getInstance().d(position);
        Class<?> aClass = listItems.get(list.get(position));
        showAndHide(R.id.content, aClass);
        activityFragmentBinding.mainSlideMenu.closeLeftSlide();
    }

    private void showAndHide(int contentId, Class<?> clazz) {
        if (currentFragment != null && currentFragment.getClass().getSimpleName().equals(clazz.getSimpleName())) {
            return;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        try {
            Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag(clazz.getSimpleName());
            if (fragmentByTag != null) {
                transaction.show(fragmentByTag);
                transaction.hide(currentFragment);
                currentFragment = fragmentByTag;
            } else {
                Fragment fragment = (Fragment) clazz.newInstance();
                transaction.add(contentId, fragment, clazz.getSimpleName());
                if (currentFragment != null) {
                    transaction.hide(currentFragment);
                }
                currentFragment = fragment;
            }
            transaction.commit();
        } catch (Exception e) {
            ALog.getInstance().e(e.toString());
        }
    }

    private class SimpleAdapter extends ArrayAdapter<String> {

        public SimpleAdapter(Context context, List<String> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

    }

    public void openMenu(View view) {
        activityFragmentBinding.mainSlideMenu.openLeftSlide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_server, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                if (BuildConfig.DEBUG) {

                }
                return true;
            case R.id.action_add_server:
                final AddServerDialog dialog = new AddServerDialog();
                dialog.show(getSupportFragmentManager(), "add_server");
                return true;
            case R.id.action_test:

                Intent intent = new Intent(FragmentActivity.this, TestIntentService.class);
                startService(intent);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDismiss() {
        ALog.getInstance().d("onDismiss");
    }

    @Override
    public void onAdd(Server server) {
        ALog.getInstance().d("onAdd");
    }

    @Override
    public void onServerClick(Server server) {
        ALog.getInstance().d("onServerClick: " + server.toString());
    }

    @Override
    public void onClick(View v) {

    }
}
