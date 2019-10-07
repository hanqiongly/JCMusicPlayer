package com.jack.music.funcomp.permission;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Process;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.jack.base.log.DebugLog;

class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();

    /* 从一个权限表列中筛选出当前应用没有权限的权限项，防止重复申请
    *  @params rawPermissions 原始的需要申请的权限表列
    *  @return String[] 当前应用中还未获取到的权限列表，需要进行申请的列表
    * */
    static String[] filterNoPermissionNames(Context context, String[] rawPermissions) {
        int rawPermissionsLength = rawPermissions.length;
       String[] result = new String[rawPermissionsLength];
       int index = 0;
       for (String permission : rawPermissions) {
           if (TextUtils.isEmpty(permission)) {
               continue;
           }
           if (!hasPermissionGranted(context, permission)) {
               result[index] = permission;
               index++;
           }
       }
       return result;
    }

    static boolean hasPermissionGranted(Context context, String permission) {
        if (context == null || TextUtils.isEmpty(permission)) {
            return false;
        }

        int grantCode = ContextCompat.checkSelfPermission(context, permission);
        boolean hasGranted = grantCode == PackageManager.PERMISSION_GRANTED;
        if (hasGranted) {
            hasGranted = checkOpsPermission(context, permission);
        }
        DebugLog.d(TAG, "hasPermissionGranted() : permissionName :" + permission + " hasPermission :" + hasGranted);
        return hasGranted;
    }


    private static boolean checkOpsPermission(Context context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                String opsName = AppOpsManager.permissionToOp(permission);
                if (TextUtils.isEmpty(opsName)) {
                    return true;
                }
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                int opsMode = appOpsManager.checkOpNoThrow(opsName, Process.myUid(), context.getPackageName());
                return opsMode == AppOpsManager.MODE_ALLOWED;
            } catch (Exception e) {
                DebugLog.d(TAG, e.getMessage());
                return false;
            }
        }
        return true;
    }
}
