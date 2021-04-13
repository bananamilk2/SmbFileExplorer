package com.asjm.fileexplorer.hook;

import android.view.View;

import com.asjm.lib.util.ALog;

public class ProxyOnClickListener implements View.OnClickListener {
    private View.OnClickListener listener;

    public ProxyOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        ALog.getInstance().d("onclick");
        if (listener != null) {
            listener.onClick(v);
        }
    }
}
