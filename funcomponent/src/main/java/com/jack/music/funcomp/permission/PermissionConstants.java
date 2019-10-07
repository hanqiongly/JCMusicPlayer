package com.jack.music.funcomp.permission;

import android.Manifest;

public class PermissionConstants {
    /**
     * 请求权限的权限名，由系统定义的权限名
     * */
   public static final String EXTRA_PERMISSION_NAMES = "extra_bundle_permission_name_list";

   /**
    * 在PermissionManager中进行权限请求的过程中，执行请求的唯一编号，以备回调使用
    * */
   public static final String EXTRA_PERMISSION_ID = "extra_bundle_permission_id";

   /**请求系统权限的请求code，需要在onActivityResult回调中使用*/
   public static final int EXTRA_PERMISSION_CODE = 1080;
   public static final int EXTRA_PERMISSION_DEFAULT_CODE = 1081;

   /**
    * 请求某一项系统权限的identity code(某一项权限的识别码，一项权限对应一个识别码)，不同权限对应的code不一样
    * */
   public interface DynamicPermissionReqCodes {
      /**文件读权限 id*/
      int REQ_FILE_READ_ID = 0;
      /**文件写权限 id*/
      int REQ_FILE_WRITE_ID = 1;
      /**申请照相机权限 id*/
      int REQ_CAMERA_ID = 2;
      /**获取联系人的权限id*/
      int REQ_CONTACTS = 3;
   }

   /**
    * 收集一些动态权限的权限名称
    * */
   public interface DynamicPermissionNames {
      /*联系人的权限名称*/
       String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS,
               Manifest.permission.WRITE_CONTACTS};
       String PERMISSION_CAMERA = Manifest.permission.CAMERA;
   }
}
