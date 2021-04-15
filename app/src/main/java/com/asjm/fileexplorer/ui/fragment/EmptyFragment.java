package com.asjm.fileexplorer.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.asjm.fileexplorer.databinding.FragmentEmptyBinding;
import com.asjm.fileexplorer.hook.HookSetOnClickListenerHelper;
import com.asjm.fileexplorer.hook.HookUtil;
import com.asjm.lib.util.ALog;

public class EmptyFragment extends BaseFragment implements View.OnClickListener {

    FragmentEmptyBinding binding;

    public EmptyFragment() {
        this("empty fragment");
    }

    public EmptyFragment(String name) {
        super(name);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ALog.getInstance().d("onCreateView");
        binding = FragmentEmptyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ALog.getInstance().d("onViewCreated");
        binding.hook.setOnClickListener(this);

        HookUtil.hook(binding.hook);
//        HookSetOnClickListenerHelper.hook(getContext(), binding.hook);
    }

    @Override
    public void onClick(View v) {
        ALog.getInstance().d("hook");

    }
}
