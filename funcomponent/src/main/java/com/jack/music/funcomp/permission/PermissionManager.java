package com.jack.music.funcomp.permission;

import android.content.Context;

/**
 * 增加客户端权限申请机制，动态申请app的权限调用
 * */
public class PermissionManager {
    private static PermissionManager mInstance;
    private Context mAppContext;//当前app中最不容易被回收的context，通常就是MainActivity对应的context；
    public synchronized static PermissionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PermissionManager(context);
        }
        return mInstance;
    }

    private PermissionManager(Context context){

    }

//    public boolean checkHasPermission()

}
