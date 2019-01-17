package com.jack.music.funcomp.permission;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

/**执行权限申请的主要activity，所有的权限申请都通过这个Activity执行，以及回调接口的返回*/
public class PermissionRequestActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceStatus) {
        super.onCreate(savedInstanceStatus);
    }

    public void onStart() {
        super.onStart();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
