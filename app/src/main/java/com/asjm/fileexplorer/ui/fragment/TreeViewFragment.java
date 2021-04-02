package com.asjm.fileexplorer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asjm.fileexplorer.R;
import com.asjm.fileexplorer.databinding.FragmentTreeViewBinding;
import com.asjm.fileexplorer.holder.tree.FileListHolder;
import com.asjm.fileexplorer.holder.tree.SelectableHeaderHolder;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TreeViewFragment extends BaseFragment {

    private FragmentTreeViewBinding fragmentTreeViewBinding;

    public TreeViewFragment() {
        this("TreeViewFragment");
    }

    public TreeViewFragment(String name) {
        super(name);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentTreeViewBinding = FragmentTreeViewBinding.inflate(inflater);
        return fragmentTreeViewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TreeNode root = TreeNode.root();

        TreeNode parent1 = new TreeNode(new FileListHolder.IconTreeItem(1, "parent1")).setViewHolder(new SelectableHeaderHolder(getActivity()));
//        TreeNode parent1 = new TreeNode(new FileListHolder.IconTreeItem(1, "parent1"));
        TreeNode parent2 = new TreeNode(new FileListHolder.IconTreeItem(1, "parent2")).setViewHolder(new SelectableHeaderHolder(getActivity()));

        fillFolder(parent1);
        root.addChild(parent1);
        root.addChild(parent2);

        AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
        tView.setDefaultAnimation(true);
//        tView.setUse2dScroll(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
        tView.setSelectionModeEnabled(true);

        fragmentTreeViewBinding.content.addView(tView.getView());
    }


    private static final String NAME = "Very long name for folder";

    private void fillFolder(TreeNode folder) {
        TreeNode currentNode = folder;
        for (int i = 0; i < 10; i++) {
            TreeNode file = new TreeNode(new FileListHolder.IconTreeItem(1, NAME)).setViewHolder(new SelectableHeaderHolder(getActivity()));
            currentNode.addChild(file);
//            currentNode = file;
        }
    }

}
