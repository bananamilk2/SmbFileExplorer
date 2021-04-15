package com.asjm.fileexplorer.hook;

import android.view.View;

import com.asjm.lib.util.ALog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HookUtil {
    public static void hook(View view) {
        try {
            Method method = View.class.getDeclaredMethod("getListenerInfo");
            method.setAccessible(true);
            Object invoke = method.invoke(view);
            Class<?> aClass = Class.forName("android.view.View$ListenerInfo");
            Field mOnClickListener = aClass.getDeclaredField("mOnClickListener");
            mOnClickListener.setAccessible(true);
            View.OnClickListener instance = (View.OnClickListener) mOnClickListener.get(invoke);


            ProxyOnClickListener proxyOnClickListener = new ProxyOnClickListener(instance);
            mOnClickListener.set(invoke, proxyOnClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class A {

}
