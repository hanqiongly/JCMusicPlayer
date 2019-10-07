package com.jack.base;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

    private static Context context;
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Context getAppContext() {
        return context.getApplicationContext();
    }
}
