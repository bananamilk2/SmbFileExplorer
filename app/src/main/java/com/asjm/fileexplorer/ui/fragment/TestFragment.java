package com.asjm.fileexplorer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.asjm.fileexplorer.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TestFragment extends BaseFragment {

    public TestFragment() {
        this("TestFragment");
    }

    public TestFragment(String fragmentName) {
        super(fragmentName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_expand_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expandableListView = view.findViewById(R.id.expand_list);
    }

    @Override
    public void onStart() {
        super.onStart();
        //expandableListView.setGroupIndicator(null);
        adapter = new Adapter();
        initData();
        expandableListView.setAdapter(adapter);
    }

    private ExpandableListView expandableListView;
    private Adapter adapter;
    private List<String> group;
    private List<List<String>> child;

    public void initData() {
        group = new ArrayList<String>();
        child = new ArrayList<List<String>>();
        addInfo("jack", new String[]{"a", "aa", "aaa"});
        addInfo("rose", new String[]{"b", "bb", "bbb"});
        addInfo("tom", new String[]{"c", "cc", "ccc"});
    }

    public void addInfo(String g, String[] c) {
        group.add(g);
        List<String> item = new ArrayList<>();
        for (int i = 0; i < c.length; i++) {
            item.add(c[i]);
        }
        child.add(item);
    }

    public class Adapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return group.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return child.size();
        }

        @Override
        public Object getGroup(int i) {
            return group.get(i);
        }

        @Override
        public Object getChild(int i, int i1) {
            return child.get(i).get(i1);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

            TextView textView = null;
            if (view == null) {
                textView = new TextView(getContext());
            } else {
                textView = (TextView) view;
            }
            textView.setText(group.get(i));
            textView.setTextSize(20);
            textView.setPadding(70, 0, 0, 40);
            return textView;
        }

        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {

            TextView textView = null;
            if (view == null) {
                textView = new TextView(getContext());
            } else {
                textView = (TextView) view;
            }
            textView.setText(child.get(i).get(i1));
            textView.setTextSize(20);
            textView.setPadding(70, 0, 0, 40);
            return textView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
