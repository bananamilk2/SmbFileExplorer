package com.asjm.fileexplorer.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.asjm.lib.util.ALog;

public class ListScrollView extends ScrollView {
    public ListScrollView(Context context) {
        super(context);
    }

    public ListScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ALog.getInstance().d("dispatchTouchEvent: " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ALog.getInstance().d("onInterceptTouchEvent: " + ev.getAction());
//        if (listView != null && checkArea(listView, ev)) {
//            return false;
//        }
        return super.onInterceptTouchEvent(ev);
//        return false;
    }

    private ListView listView;

    private boolean checkArea(View v, MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        int[] locate = new int[2];
        v.getLocationOnScreen(locate);
        int l = locate[0];
        int r = l + v.getWidth();
        int t = locate[1];
        int b = t + v.getHeight();
        if (l < x && x < r && t < y && y < b) {
            return true;
        }
        return false;
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        ALog.getInstance().d("onTouchEvent: " + ev.getAction());
        return super.onTouchEvent(ev);
    }
}
