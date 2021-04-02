package com.asjm.fileexplorer.holder.tree;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.asjm.fileexplorer.R;
import com.asjm.lib.util.ALog;
import com.unnamed.b.atv.model.TreeNode;

public class SelectableHeaderHolder extends TreeNode.BaseNodeViewHolder<FileListHolder.IconTreeItem> {
    private TextView tvValue;
    private CheckBox nodeSelector;

    public SelectableHeaderHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, FileListHolder.IconTreeItem value) {
        ALog.getInstance().d("createNodeView");
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_selectable_header, null, false);

        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);

        if (node.isLeaf()) {
            ALog.getInstance().d("isLeaf");
        }

        nodeSelector = view.findViewById(R.id.node_selector);
        nodeSelector.setOnCheckedChangeListener((buttonView, isChecked) -> {
            node.setSelected(isChecked);
            for (TreeNode n : node.getChildren()) {
                getTreeView().selectNode(n, isChecked);
            }
        });
        nodeSelector.setChecked(node.isSelected());

        return view;
    }

    @Override
    public void toggle(boolean active) {
        ALog.getInstance().d("toggle: " + active);
//        arrowView.setIconText(context.getResources().getString(active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right));
    }

    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        nodeSelector.setVisibility(editModeEnabled ? View.VISIBLE : View.GONE);
        nodeSelector.setChecked(mNode.isSelected());
    }
}
