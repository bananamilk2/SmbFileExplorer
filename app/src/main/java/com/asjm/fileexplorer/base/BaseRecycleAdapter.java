package com.asjm.fileexplorer.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asjm.fileexplorer.listener.OnItemClickListener;
import com.asjm.lib.util.ALog;

import java.lang.reflect.Constructor;
import java.util.List;

public class BaseRecycleAdapter<T, V extends BaseViewHolder<T>> extends RecyclerView.Adapter<V> {
    private List<T> dataList;
    private int layoutId;
    private Class<V> clazz;
    private OnItemClickListener<T> onItemClickListener;

    public BaseRecycleAdapter(List<T> list, int layout, Class<V> cClass) {
        this.dataList = list;
        this.layoutId = layout;
        this.clazz = cClass;
    }

    @NonNull
    @Override
    public V onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        V result = null;
        try {
            Constructor<V> constructor = clazz.getConstructor(View.class);
            result = constructor.newInstance(view);
        } catch (Exception e) {
            ALog.getInstance().d(e.toString());
        }
        return result;
    }

    @Override
    public void onBindViewHolder(V holder, final int position) {
        final T t = dataList.get(position);
        holder.setData(t);
        holder.loadItemData(t, position);
        if (this.onItemClickListener != null) {
            holder.setOnClickListener(view -> onItemClickListener.onItemClick(t, view, position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
