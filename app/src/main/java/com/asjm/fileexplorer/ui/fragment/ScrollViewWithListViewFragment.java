package com.asjm.fileexplorer.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.asjm.fileexplorer.databinding.FragmentScrollListviewBinding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ScrollViewWithListViewFragment extends BaseFragment {

    private FragmentScrollListviewBinding binding;

    public ScrollViewWithListViewFragment() {
        this("ScrollViewWithListViewFragment");
    }

    public ScrollViewWithListViewFragment(String fragmentName) {
        super(fragmentName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentScrollListviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listview = binding.listview;
        binding.scrollview.setListView(listview);

        final LinkedHashMap<String, Class<?>> listItems = new LinkedHashMap<>();
        listItems.put("ExpandListFragment", ExpandListFragment.class);
        listItems.put("TestFragment", TestFragment.class);
        listItems.put("TreeViewFragment", TreeViewFragment.class);
        listItems.put("111", ExpandListFragment.class);
        listItems.put("222", TestFragment.class);
        listItems.put("333", TreeViewFragment.class);
        listItems.put("444", ExpandListFragment.class);
        listItems.put("555", TestFragment.class);
        listItems.put("666", TreeViewFragment.class);
        listItems.put("77", TestFragment.class);
        listItems.put("888", TreeViewFragment.class);
        listItems.put("999", ExpandListFragment.class);
        listItems.put("123", TestFragment.class);
        listItems.put("1234", TreeViewFragment.class);
        listItems.put("545", TreeViewFragment.class);
        listItems.put("653", TreeViewFragment.class);

        final List<String> list = new ArrayList(listItems.keySet());
        final SimpleArrayAdapter adapter = new SimpleArrayAdapter(getContext(), list);
        listview.setAdapter(adapter);
//        setListViewHeightBasedOnChildren(listview);
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    private class SimpleArrayAdapter extends ArrayAdapter<String> {

        public SimpleArrayAdapter(Context context, List<String> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);

        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}
