package com.asjm.fileexplorer.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asjm.fileexplorer.BuildConfig;
import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.databinding.ActivityFragmentBinding;
import com.asjm.fileexplorer.entity.Server;
import com.asjm.fileexplorer.service.TestIntentService;
import com.asjm.fileexplorer.ui.AddServerDialog;
import com.asjm.fileexplorer.ui.fragment.ExpandListFragment;
import com.asjm.fileexplorer.ui.fragment.ServerListFragment;
import com.asjm.lib.util.ALog;

import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

public class FragmentActivity extends BaseActivity implements AddServerDialog.Listener, ServerListFragment.Listener, View.OnClickListener {

    private ListFragment listFragment;
    private ActivityFragmentBinding activityFragmentBinding;
    private String[] menuList = {"server list", "test expand layout"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.getInstance().d("onCreate");

        activityFragmentBinding = ActivityFragmentBinding.inflate(getLayoutInflater());
        setContentView(activityFragmentBinding.getRoot());

        listFragment = new ServerListFragment();
        getSupportFragmentManager().beginTransaction().replace(activityFragmentBinding.content.getId(), listFragment).commit();

        initMenu();
    }

    private void initMenu() {
        for (String s : menuList) {
            TextView textView = new TextView(this);
            textView.setText(s);
            textView.setTextSize(18);
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setOnClickListener(this);
            textView.setTag(s);
            activityFragmentBinding.leftContent.addView(textView);
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
        ALog.getInstance().d(v.getTag());
        if (menuList[1].equals(v.getTag())) {
            ExpandListFragment expandListFragment = new ExpandListFragment();
            getSupportFragmentManager().beginTransaction().replace(activityFragmentBinding.content.getId(), expandListFragment).commit();
            activityFragmentBinding.mainSlideMenu.closeLeftSlide();
        }
    }
}
