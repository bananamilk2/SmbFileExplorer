package com.asjm.fileexplorer.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

import com.asjm.lib.util.ALog;

public class ScrollListView extends ListView {
    public ScrollListView(Context context) {
        super(context);
    }

    public ScrollListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,//右移运算符，相当于除于4
                MeasureSpec.AT_MOST);//测量模式取最大值
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);//重新测量高度
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        ALog.getInstance().d("dispatchTouchEvent: " + ev.getAction());
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        ALog.getInstance().d("onInterceptTouchEvent: " + ev.getAction());
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        ALog.getInstance().d("onTouchEvent: " + ev.getAction());
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ALog.getInstance().d("dispatchTouchEvent: " + ev.getAction());
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    //为listview/Y，设置初始值,默认为0.0(ListView条目一位置)
    private float mLastY;

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        ALog.getInstance().d("dispatchTouchEvent: " + ev.getAction());
//        //重点在这里
//        int action = ev.getAction();
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                super.onInterceptTouchEvent(ev);
//                //不允许上层viewGroup拦截事件.
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                //满足listView滑动到顶部，如果继续下滑，那就让scrollView拦截事件
//                if (getFirstVisiblePosition() == 0 && (ev.getY() - mLastY) > 0) {
//                    //允许上层viewGroup拦截事件
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                //满足listView滑动到底部，如果继续上滑，那就让scrollView拦截事件
//                else if (getLastVisiblePosition() == getCount() - 1 && (ev.getY() - mLastY) < 0) {
//                    //允许上层viewGroup拦截事件
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                } else {
//                    //不允许上层viewGroup拦截事件
//                    getParent().requestDisallowInterceptTouchEvent(true);
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                //不允许上层viewGroup拦截事件
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//        }
//
//        mLastY = ev.getY();
//        return super.dispatchTouchEvent(ev);
//
//    }
}
