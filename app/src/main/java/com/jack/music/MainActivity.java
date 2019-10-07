package com.jack.music;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jack.music.funcomp.permission.IPermissionResult;
import com.jack.music.funcomp.permission.PermissionConstants;
import com.jack.music.funcomp.permission.PermissionManager;
import com.jack.music.funcomp.utils.CollectionUtils;
import com.jack.music.service.utils.PlayUtils;
import com.jack.music.widgets.BottomPlayController;

import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, IPermissionResult {
    private static final String TAG = "Debug_MainActvity";
    //负责播放service的绑定
    private PlayUtils.ServiceToken mToken;
    private BottomPlayController mBottomController;
    private DrawerLayout mDrawerLayout;
    TextView tvBindService;
    TextView tvRequestPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        requestPermissions();
    }

    private void requestPermissions() {
        PermissionManager.getInstance().requestDefaultPermissions(this, this);
    }

    public void onStart() {
        super.onStart();
//        bindService();
    }

    public void onStop() {
        super.onStop();
    }

    private void initViews() {
        mBottomController = (BottomPlayController)findViewById(R.id.layer_bottom_controller);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.main_page_drawer);
        tvBindService = (TextView) findViewById(R.id.tv_btn_bindService);
        tvBindService.setOnClickListener(this);
        tvRequestPermission = (TextView) findViewById(R.id.tv_btn_request_permission);
        tvRequestPermission.setOnClickListener(this);
    }

    private void bindService() {
        if (mToken == null) {
            mToken = PlayUtils.bindToService(this, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mBottomController.restoreStatus();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                }
            });
        }
    }

    private void unbindService() {
        PlayUtils.unBindFromService(mToken);
    }

    public void onDestroy() {
        super.onDestroy();
        unbindService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_btn_bindService:
                bindService();
                break;
            case R.id.tv_btn_request_permission:
                requestPermissions();
                break;
        }
    }

    @Override
    public void onPermissionResult(int requestCode, List<String> allowedPermissions, List<String> deniedPermissions) {
        Log.d(TAG, "onPermissionResult() ");
        if (CollectionUtils.isEmpty(allowedPermissions)) {
            Toast.makeText(this, "存储空间权限申请失败",Toast.LENGTH_SHORT).show();
            return;
        }
        if (allowedPermissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE) || allowedPermissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "存储空间权限申请成功", Toast.LENGTH_SHORT).show();
            bindService();
        }
    }

    @Override
    public void onPremissionAlreadyGranted(int requestCode) {
        if (requestCode == PermissionConstants.EXTRA_PERMISSION_DEFAULT_CODE) {
            bindService();
        }
    }
}
