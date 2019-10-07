package com.jack.music.funcomp.permission;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 执行权限申请的主要activity，所有的权限申请都通过这个Activity执行，以及回调接口的返回
 * */
public class PermissionRequestActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private static final String TAG = "Debug_PermissionActivit";
    /**
     * 当前请求权限的列表
     * */
    private String[] mPermissionList;
    /**
     * 当前请求权限的id，在PermissionManager中对每一个申请权限的项进行编号，
     * 结果由这个编号进行回调返回*/
    private String permissionRequestID;

    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
        initWidget();
        parseData();
    }

    private void initWidget() {

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (permissions == null || permissions.length <= 0) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < permissions.length; i++) {
            stringBuilder.append(permissions[i]);
            stringBuilder.append("<>");
        }
        Log.d(TAG, "onRequestPermissionsResult() " + stringBuilder.toString() + " requestCode :" + requestCode);

        switch (requestCode) {
            case PermissionConstants.EXTRA_PERMISSION_DEFAULT_CODE:
                PermissionManager.getInstance().onPermissionResultCallback(requestCode, permissions, grantResults);
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        finish();

    }

    private void parseData() {
        Intent intent = getIntent();
        ArrayList<String> permissionList = intent.getStringArrayListExtra(PermissionConstants.EXTRA_PERMISSION_NAMES);
        int permissionListSize = permissionList == null ? 0 : permissionList.size();
        if (permissionListSize <= 0) {
            finish();
            return;
        }
        permissionRequestID = intent.getStringExtra(PermissionConstants.EXTRA_PERMISSION_ID);
        mPermissionList = new String[permissionListSize];
        int index = 0;
        for (String permission : permissionList ) {
            if (TextUtils.isEmpty(permission)) {
                continue;
            }
            mPermissionList[index] = permission;
            index++;
        }
        Log.d(TAG, "parseData() permission not null . ");
        requestPermissions();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    private void requestPermissions() {
        String[] needRequestPermission = PermissionUtils.filterNoPermissionNames(this, mPermissionList);
        if (needRequestPermission == null || needRequestPermission.length <= 0) {
            return;
        }
        ActivityCompat.requestPermissions(this, mPermissionList, PermissionConstants.EXTRA_PERMISSION_DEFAULT_CODE);
    }
}
