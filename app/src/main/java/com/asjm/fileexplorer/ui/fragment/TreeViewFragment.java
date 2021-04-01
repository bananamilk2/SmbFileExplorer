package com.asjm.fileexplorer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asjm.fileexplorer.databinding.FragmentTreeViewBinding;
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
        TreeNode parent = new TreeNode("MyParentNode");
        TreeNode child0 = new TreeNode("ChildNode0");
        TreeNode child1 = new TreeNode("ChildNode1");
        parent.addChildren(child0, child1);
        root.addChild(parent);

        AndroidTreeView tView = new AndroidTreeView(getActivity(), root);
        fragmentTreeViewBinding.content.addView(tView.getView());
    }

}
