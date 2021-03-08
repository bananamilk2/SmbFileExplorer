package com.asjm.fileexplorer.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.entity.Server;
import com.asjm.lib.util.ALog;

import java.util.List;

public class ServerAdapter extends BaseAdapter {
    private Context context;
    private List<Server> serverList;

    public ServerAdapter(Context context, List<Server> list) {
        this.context = context;
        this.serverList = list;
    }

    @Override
    public int getCount() {
        return serverList.size();
    }

    @Override
    public Object getItem(int position) {
        return serverList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ALog.getInstance().d("position: " + position + " convertview: " + convertView + " parent: " + parent);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_link, parent, false);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ip = convertView.findViewById(R.id.ip);
        viewHolder.user = convertView.findViewById(R.id.user);
        viewHolder.ip.setText(serverList.get(position).getAddress());
        viewHolder.user.setText(serverList.get(position).getName());
        return convertView;
    }

    private static class ViewHolder {
        public TextView ip;
        public TextView user;
    }
}
