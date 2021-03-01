package com.asjm.fileexplorer.holder;

import android.view.View;
import android.widget.TextView;

import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.base.BaseViewHolder;
import com.asjm.fileexplorer.entity.File;
import com.asjm.lib.util.ALog;

public class FileViewHolder extends BaseViewHolder<File> {

    private TextView name;
    private TextView time;

    public FileViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void initView(View view) {
        name = itemView.findViewById(R.id.name);
        time = itemView.findViewById(R.id.time);
    }

    @Override
    public void loadItemData(File data, int position) {
        this.name.setText(data.getFileName());
        this.time.setText(data.getDownloadTime().toString());
        ALog.getInstance().d("position = " + position + " ");
    }
}