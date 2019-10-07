package com.jack.music.funcomp.permission;

import java.util.List;

public interface IPermissionResult {
    /**
     * 请求结果回调接口
     * @param requestCode 这一次权限请求的请求码
     * @param allowedPermissions 这一次请求接口中允许的权限名称
     * @param deniedPermissions 这一次请求接口中不允许的权限名称
     * */
    void onPermissionResult(int requestCode, List<String> allowedPermissions, List<String> deniedPermissions);

    /**
     * 当前requestCode所请求的权限之前已经申请好了
     *
     * @param requestCode 请求的权限对应的请求码
     * */
    void onPremissionAlreadyGranted(int requestCode);
}
