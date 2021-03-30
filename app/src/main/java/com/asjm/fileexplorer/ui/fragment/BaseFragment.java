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

    public BaseFragment(){}

    public BaseFragment(String fragmentName) {
        this.fragmentName = fragmentName;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ALog.getInstance().d("onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ALog.getInstance().d("onCreate");
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
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
    public void onDestroyView() {
        super.onDestroyView();
        ALog.getInstance().d("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ALog.getInstance().d("onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ALog.getInstance().d("onDetach");
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        ALog.getInstance().d("onAttachFragment");
    }
}
