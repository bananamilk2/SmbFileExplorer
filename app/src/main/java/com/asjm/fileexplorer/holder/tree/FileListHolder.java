package com.asjm.fileexplorer.holder.tree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.asjm.fileexplorer.R;
import com.asjm.lib.util.ALog;
import com.unnamed.b.atv.model.TreeNode;

public class FileListHolder extends TreeNode.BaseNodeViewHolder<FileListHolder.IconTreeItem> {


    public FileListHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, IconTreeItem value) {
        ALog.getInstance().d("createNodeView");
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.item_node, null, false);
        TextView tvValue = view.findViewById(R.id.node_value);
        tvValue.setText(value.text);
        return view;
    }

    @Override
    public void toggle(boolean active) {
        super.toggle(active);
        ALog.getInstance().d("toggle: " + active);
    }

    public static class IconTreeItem {
        public int icon;
        public String text;

        public IconTreeItem(int icon, String text) {
            this.icon = icon;
            this.text = text;
        }
    }
}
