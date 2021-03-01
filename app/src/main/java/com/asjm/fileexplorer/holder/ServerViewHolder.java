package com.asjm.fileexplorer.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.base.BaseViewHolder;
import com.asjm.fileexplorer.entity.Server;
import com.asjm.lib.util.ALog;

public class ServerViewHolder extends BaseViewHolder<Server> {

    private TextView ip;
    private TextView username;
    private ImageView image;
    private LinearLayout layout;

    public ServerViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void initView(View view) {
        ip = itemView.findViewById(R.id.ip);
        username = itemView.findViewById(R.id.user);
        image = itemView.findViewById(R.id.image);
        layout = itemView.findViewById(R.id.layout);
    }

    @Override
    public void loadItemData(Server data, int position) {
        this.username.setText(data.getUsername());
        this.ip.setText(data.getAddress());
        this.image.setTag(position);
        this.layout.setTag(position);
        ALog.getInstance().d("position = " + position + " ");
    }
}
