package com.jack.music.funcomp.permission;

import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionRequestBean {
    private boolean neverAsked;
    private int requestCode;
    private IPermissionResult result;
    private ArrayList<CharSequence> permissionNames;
    private String[] messages;
    private Map<CharSequence, Boolean> resultMap;
    private int count = 0;

    public PermissionRequestBean(IPermissionResult listener,
                                  int requestCode, ArrayList<CharSequence> permissions/*, String[] tipMessages*//*, boolean handleNeverAsk*/) {
        this.result = listener;
        this.requestCode = requestCode;
        this.permissionNames = permissions;
//        this.messages = tipMessages;
//        this.neverAsked = handleNeverAsk;
        initMap();
    }

    private void initMap() {
        resultMap = new HashMap<>(16);
        if (permissionNames == null || permissionNames.size() == 0) {
            return;
        }
        for (CharSequence permission : permissionNames) {
            resultMap.put(permission, false);
            count++;
        }
    }

    public void updateRequestResult(String permission, int result) {

    }

    public IPermissionResult getResult() {
        return result;
    }
}
