package com.asjm.fileexplorer.holder;

import android.annotation.SuppressLint;
import android.view.View;

import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.base.BaseViewHolder;
import com.asjm.fileexplorer.databinding.ItemFileBinding;
import com.asjm.fileexplorer.entity.FileSmb;
import com.asjm.fileexplorer.utils.FileUtil;
import com.asjm.lib.util.ALog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FileViewHolder extends BaseViewHolder<FileSmb> {

    private ItemFileBinding view;

    @SuppressLint("SimpleDateFormat")
    public final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FileViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void initView(View view) {
        this.view = ItemFileBinding.bind(view);
    }

    @Override
    public void loadItemData(FileSmb data, int position) {
        view.name.setText(data.getName());
        view.time.setText(df.format(data.getDate()));
        view.size.setText(data.isDir() ? view.getRoot().getContext().getResources().getString(R.string.text_directory)
                : FileUtil.formatFileSize(data.getSize()));
        try {
            if (data.isDir())
                view.type.setImageResource(R.drawable.ic_folder);
            else
                view.type.setImageResource(R.drawable.ic_file);
        } catch (Exception e) {
            ALog.getInstance().e(e.toString());
        }
    }
}