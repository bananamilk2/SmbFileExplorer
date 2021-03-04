package com.asjm.fileexplorer.listener;

import android.view.View;

public interface OnItemLongClickListener<T> {

    boolean onItemLongClick(T data, View view, int position);

}
