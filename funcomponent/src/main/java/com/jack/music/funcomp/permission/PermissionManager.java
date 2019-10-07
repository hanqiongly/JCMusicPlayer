package com.jack.music.funcomp.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.jack.base.BaseApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 增加客户端权限申请机制，动态申请app的权限调用
 * */
public class PermissionManager {
    private static final String TAG = "Debug_PermissionManager";
    private static PermissionManager mInstance;
//    private Context mAppContext;//当前app中最不容易被回收的context，通常就是MainActivity对应的context；
    private static ConcurrentHashMap<Integer,PermissionRequestBean> requestedPermission = null;

    public static PermissionManager getInstance() {
        return PermissionManagerInstanceHolder.instance;
    }

    private PermissionManager(Context context){
        requestedPermission = new ConcurrentHashMap<>();
    }

    public boolean checkHasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestDefaultPermissions(Context context, IPermissionResult permissionResult) {
        if (context == null || permissionResult == null) {
            return;
        }
        ArrayList<CharSequence> dftPermissions = new ArrayList<>();

        dftPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        dftPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (checkHasPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE) ||
                checkHasPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissionResult.onPremissionAlreadyGranted(PermissionConstants.EXTRA_PERMISSION_DEFAULT_CODE);
            return;
        }

        requestPermissionsInner(context, dftPermissions, PermissionConstants.EXTRA_PERMISSION_DEFAULT_CODE, permissionResult);

    }

    private void requestPermissionsInner(Context context, ArrayList<CharSequence> permissions, int requestCode, IPermissionResult permissionResult) {

        if (requestedPermission.contains(requestCode)) {
            return;
        }

        PermissionRequestBean requestBean = new PermissionRequestBean(permissionResult, requestCode, permissions);
        requestedPermission.put(requestCode, requestBean);

        Intent intent = new Intent();
        intent.setClass(context, PermissionRequestActivity.class);
        intent.putCharSequenceArrayListExtra(PermissionConstants.EXTRA_PERMISSION_NAMES, permissions);
        intent.putExtra(PermissionConstants.EXTRA_PERMISSION_ID, requestCode);
        context.startActivity(intent);
    }

    public IPermissionResult getPermissionCallback(Integer key) {
        PermissionRequestBean resultBean = requestedPermission.get(key);
        if (resultBean == null) {
            return null;
        }
        requestedPermission.remove(key);
        return resultBean.getResult();
    }

    void onPermissionResultCallback(int requestCode, String[] permissions, int[] granted) {
        Log.d(TAG, "onPermissionResultCallback()");
        int permissionSize = permissions == null ? 0 : permissions.length;
        int grantedSize = granted == null ? 0 : granted.length;
        if (permissionSize <= 0 || grantedSize <= 0) {
            return;
        }
        IPermissionResult permissionResult = getPermissionCallback(requestCode);
        if (permissionResult == null) {
            return;
        }

        List<String> allowed = new ArrayList<>();
        List<String> denied = new ArrayList<>();

        for (int index = 0; index < permissionSize; index ++) {
            String permissionName = permissions[index];
            if (TextUtils.isEmpty(permissionName)) {
                continue;
            }
            if (granted[index] == 0) {
                allowed.add(permissionName);
            } else {
                denied.add(permissionName);
            }
        }
        permissionResult.onPermissionResult(requestCode, allowed, denied);

    }


//    public boolean checkHasPermission()

    private static class PermissionManagerInstanceHolder {
        private static PermissionManager instance = new PermissionManager(BaseApplication.getAppContext());
    }



}
