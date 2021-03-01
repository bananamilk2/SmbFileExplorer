package com.asjm.fileexplorer.listener;

import android.view.View;

public interface OnItemClickListener<T> {

    void onItemClick(T data, View view, int position);

}
