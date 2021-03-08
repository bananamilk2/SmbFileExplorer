package com.asjm.fileexplorer.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.asjm.fileexplorer.BuildConfig;
import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.entity.Server;
import com.asjm.fileexplorer.ui.AddServerDialog;
import com.asjm.fileexplorer.ui.fragment.ServerListFragment;
import com.asjm.lib.util.ALog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.ListFragment;

public class FragmentActivity extends AppCompatActivity implements AddServerDialog.Listener, ServerListFragment.Listener {

    private ListFragment listFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.getInstance().d("onCreate");
        listFragment = new ServerListFragment();
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, listFragment).commit();
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
}
