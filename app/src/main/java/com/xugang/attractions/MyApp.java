package com.xugang.attractions;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ASUS on 2016-10-18.
 */
public class MyApp extends Application {
    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        Fresco.initialize(this);
        context = this;
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }

    public static Context getApplication() {
        return context;
    }
}
