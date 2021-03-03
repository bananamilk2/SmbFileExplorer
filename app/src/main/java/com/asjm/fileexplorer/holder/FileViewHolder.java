package com.asjm.fileexplorer.holder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.base.BaseViewHolder;
import com.asjm.fileexplorer.databinding.ItemFileBinding;
import com.asjm.fileexplorer.entity.FileSmb;
import com.asjm.fileexplorer.utils.FileUtil;
import com.asjm.lib.util.ALog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FileViewHolder extends BaseViewHolder<FileSmb> {

    private ItemFileBinding itemFileBinding;
    @SuppressLint("SimpleDateFormat")
    public final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FileViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void initView(View view) {
        itemFileBinding = ItemFileBinding.bind(view);
    }

    @Override
    public void loadItemData(FileSmb data, int position) {
        itemFileBinding.name.setText(data.getFileName());
        itemFileBinding.time.setText(df.format(data.getFileTime()));
        itemFileBinding.size.setText(FileUtil.formatFileSize(data.getFileSize()));
        try {
            if (data.isDir())
                itemFileBinding.type.setImageResource(R.drawable.ic_folder);
            else
                itemFileBinding.type.setImageResource(R.drawable.ic_file);
        } catch (Exception e) {
            ALog.getInstance().e(e.toString());
        }
    }
}