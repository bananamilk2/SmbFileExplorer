package com.asjm.fileexplorer.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.asjm.fileexplorer.entity.Server;
import com.asjm.fileexplorer.manager.DaoManager;
import com.asjm.fileexplorer.ui.adapter.ServerAdapter;
import com.asjm.lib.util.ALog;
import com.google.common.collect.Lists;

import java.util.List;

public class ServerListFragment extends ListFragment {

    private AsyncTask<Void, Void, List<Server>> task;
    private Listener listener;
    private List<Server> serverList;
    private BaseAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.getInstance().d("onCreate");
        serverList = DaoManager.getInstance().getDaoSession().getServerDao().loadAll();
        ALog.getInstance().d("list size = " + serverList.size());
        adapter = new ServerAdapter(getActivity(), serverList);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ALog.getInstance().d("onAttach");
        listener = (Listener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ALog.getInstance().d("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ALog.getInstance().d("onViewCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ALog.getInstance().d("onDestroyView");
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(false);
        }
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        ALog.getInstance().d("onListItemClick: " + position);
        if (listener != null) {
            listener.onServerClick((Server) getListAdapter().getItem(position));
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        ALog.getInstance().d("onAttachFragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ALog.getInstance().d("onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ALog.getInstance().d("onDestroy");
    }

    public interface Listener {
        void onServerClick(Server server);
    }
}
