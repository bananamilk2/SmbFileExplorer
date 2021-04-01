package com.asjm.fileexplorer.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.asjm.fileexplorer.databinding.FragmentExpandListBinding;
import com.asjm.fileexplorer.ui.adapter.MyBaseExpandableListAdapter;
import com.asjm.fileexplorer.ui.adapter.bean.Group;
import com.asjm.fileexplorer.ui.adapter.bean.Item;
import com.asjm.lib.util.ALog;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ExpandListFragment extends BaseFragment {

    private FragmentExpandListBinding fragmentExpandListBinding;

    public ExpandListFragment() {
        this("ExpandListFragment");
    }


    public ExpandListFragment(String name) {
        super(name);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentExpandListBinding = FragmentExpandListBinding.inflate(inflater);
        return fragmentExpandListBinding.getRoot();
    }

    private ArrayList<Group> gData = null;
    private ArrayList<ArrayList<Item>> iData = null;
    private ArrayList<Item> lData = null;
    private ExpandableListView exlist_lol;
    private MyBaseExpandableListAdapter myAdapter = null;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        exlist_lol = fragmentExpandListBinding.expandList;
        ALog.getInstance().d(exlist_lol);
        //数据准备
        gData = new ArrayList<Group>();
        iData = new ArrayList<ArrayList<Item>>();
        gData.add(new Group("AD"));
        gData.add(new Group("AP"));
        gData.add(new Group("TANK"));

        lData = new ArrayList<Item>();
        //AD组
        lData.add(new Item(3, "剑圣"));
        lData.add(new Item(4, "德莱文"));
        lData.add(new Item(13, "男枪"));
        lData.add(new Item(14, "韦鲁斯"));
        iData.add(lData);
        //AP组
        lData = new ArrayList<Item>();
        lData.add(new Item(1, "提莫"));
        lData.add(new Item(7, "安妮"));
        lData.add(new Item(8, "天使"));
        lData.add(new Item(9, "泽拉斯"));
        lData.add(new Item(11, "狐狸"));
        iData.add(lData);
        //TANK组
        lData = new ArrayList<Item>();
        lData.add(new Item(2, "诺手"));
        lData.add(new Item(5, "德邦"));
        lData.add(new Item(6, "奥拉夫"));
        lData.add(new Item(10, "龙女"));
        lData.add(new Item(12, "狗熊"));
        lData.add(new Item(2, "诺手"));
        lData.add(new Item(5, "德邦"));
        lData.add(new Item(6, "奥拉夫"));
        lData.add(new Item(10, "龙女"));
        lData.add(new Item(12, "狗熊"));
        lData.add(new Item(2, "诺手"));
        lData.add(new Item(5, "德邦"));
        lData.add(new Item(6, "奥拉夫"));
        lData.add(new Item(10, "龙女"));
        lData.add(new Item(12, "狗熊"));
        lData.add(new Item(2, "诺手"));
        lData.add(new Item(5, "德邦"));
        lData.add(new Item(6, "奥拉夫"));
        lData.add(new Item(10, "龙女"));
        lData.add(new Item(12, "狗熊"));
        iData.add(lData);

        myAdapter = new MyBaseExpandableListAdapter(gData, iData, getContext());
        exlist_lol.setAdapter(myAdapter);

        //为列表设置点击事件
        exlist_lol.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getContext(), "你点击了：" + iData.get(groupPosition).get(childPosition).getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
