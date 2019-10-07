package com.jack.music;

import android.app.Application;

import com.jack.base.BaseApplication;
import com.jack.music.funcomp.permission.PermissionManager;
import com.jack.music.service.database.ConstructDBTask;

/**
 * Created by liuyang on 16/10/22.
 */

public class MusicApplication extends BaseApplication {

    public void onCreate() {
        super.onCreate();
//        requestPermissions();
    }

//    private void requestPermissions() {
//        PermissionManager.getInstance().requestDefaultPermissions();
//    }

}
