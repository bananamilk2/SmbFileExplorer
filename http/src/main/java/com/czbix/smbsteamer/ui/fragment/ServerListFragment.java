package com.czbix.smbsteamer.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.asjm.lib.util.ALog;
import com.czbix.smbsteamer.dao.ServerDao;
import com.czbix.smbsteamer.dao.model.Server;
import com.czbix.smbsteamer.ui.adapter.ServerAdapter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link Listener}
 * interface.
 */
public class ServerListFragment extends ListFragment {
    private Listener mListener;
    private ServerAdapter mAdapter;
    private AsyncTask<Void, Void, List<Server>> mTask;

    public static ServerListFragment newInstance() {
        return new ServerListFragment();
    }

    private ServerListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadServers();
        ALog.getInstance().d("onCreate");
    }

    public void reloadServers() {
        loadServers();
    }

    private void loadServers() {
        ALog.getInstance().d("loadServers");
        if (mTask != null) {
            mTask.cancel(false);
        }

        mTask = new AsyncTask<Void, Void, List<Server>>() {
            @Override
            protected List<Server> doInBackground(Void... params) {
                final Server server = ServerDao.getServer();
                ALog.getInstance().d("");
                if (server == null) {
                    return ImmutableList.of();
                }
                return ImmutableList.of(server);
            }

            @Override
            protected void onPostExecute(List<Server> servers) {
                ALog.getInstance().d("");
                if (mAdapter == null) {
                    mAdapter = new ServerAdapter(getActivity(), Lists.newArrayList(servers));
                    setListAdapter(mAdapter);
                } else {
                    mAdapter.clear();
                    mAdapter.addAll(servers);
                }
            }
        };
        mTask.execute();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ALog.getInstance().d("onAttach");
        try {
            mListener = (Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ALog.getInstance().d("onAttach");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ALog.getInstance().d("onViewCreated");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ALog.getInstance().d("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ALog.getInstance().d("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        ALog.getInstance().d("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        ALog.getInstance().d("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        ALog.getInstance().d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        ALog.getInstance().d("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ALog.getInstance().d("onDestroy");
    }


    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        ALog.getInstance().d("onAttachFragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mTask != null && mTask.getStatus() != AsyncTask.Status.FINISHED) {
            mTask.cancel(false);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mListener.onServerClick(mAdapter.getItem(position));
    }

    public interface Listener {
        void onServerClick(Server server);
    }
}
