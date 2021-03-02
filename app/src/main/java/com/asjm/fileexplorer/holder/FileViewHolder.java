package com.asjm.fileexplorer.holder;

import android.view.View;
import android.widget.TextView;

import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.base.BaseViewHolder;
import com.asjm.fileexplorer.databinding.ItemFileBinding;
import com.asjm.fileexplorer.entity.FileSmb;
import com.asjm.lib.util.ALog;

public class FileViewHolder extends BaseViewHolder<FileSmb> {

    private TextView name;
    private TextView time;
    private ItemFileBinding itemFileBinding;

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
        itemFileBinding.time.setText(data.getDownloadTime() + "");
        if (data.isDir())
            itemFileBinding.type.setImageResource(R.drawable.ic_folder);
        else
            itemFileBinding.type.setImageResource(R.drawable.ic_file);
    }
}