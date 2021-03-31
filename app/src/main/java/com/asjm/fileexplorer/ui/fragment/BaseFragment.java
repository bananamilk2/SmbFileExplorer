package com.asjm.fileexplorer.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.asjm.lib.util.ALog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    private String fragmentName;
    private ALog log;

    public BaseFragment() {
        log = ALog.getInstance();
    }

    public BaseFragment(String fragmentName) {
        this.fragmentName = fragmentName;
        log = ALog.getLogger();
        log.setChildTag(fragmentName);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        log.d("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log.d("onCreate");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        log.d("onViewCreated");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        log.d("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log.d("onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        log.d("onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        log.d("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        log.d("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        log.d("onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        log.d("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log.d("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        log.d("onDetach");
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        log.d("onAttachFragment");
    }
}
