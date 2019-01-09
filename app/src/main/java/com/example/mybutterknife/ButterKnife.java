package com.example.mybutterknife;

import android.app.Activity;

import java.lang.reflect.Method;

public class ButterKnife {
    public static void bind(Activity activity) {
        Class clazz = activity.getClass();
        try {
            Class bindViewClass = Class.forName(clazz.getName() + "ViewBinding");
            Method method = bindViewClass.getMethod("bind", activity.getClass());
            method.invoke(bindViewClass.newInstance(), activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
