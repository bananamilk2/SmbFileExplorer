package com.asjm.fileexplorer.base;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    private int position;

    public BaseViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    public abstract void initView(View view);

    public T data;

    public abstract void loadItemData(T data, int position);

    public void setData(T data) {
        this.data = data;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }
}
