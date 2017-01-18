package com.jack.music;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.DrawerLayout;

import com.jack.music.service.utils.PlayUtils;
import com.jack.music.widgets.BottomPlayController;

public class MainActivity extends Activity {
    //负责播放service的绑定
    private PlayUtils.ServiceToken mToken;
    private BottomPlayController mBottomController;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    public void onStart() {
        super.onStart();
        bindService();
    }

    public void onStop() {
        super.onStop();
    }

    private void initViews() {
        mBottomController = (BottomPlayController)findViewById(R.id.layer_bottom_controller);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.main_page_drawer);
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
}
