package com.vvme.permission.checker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Project name:MyAndroid
 * Author:VV
 * Created on 2018/5/22 15:54.
 * Copyright (c) 2018, vvismile@163.com All Rights Reserved.
 * Description: TODO
 */
public class AndPermissionStandardChecker implements PermissionChecker {

    @Override
    public boolean hasPermission(Context context, String... permission) {
        return hasPermission(context, Arrays.asList(permission));
    }

    /**
     * 如果是多个权限,有一个没授权,则返回false,所有的权限都授权才会返回true
     *
     * @param context
     * @param permissions
     * @return
     */
    @Override
    public boolean hasPermission(Context context, List<String> permissions) {

        /**
         * https://developer.android.com/training/permissions/requesting?hl=zh-cn
         * 检查权限
         * 如果您的应用需要危险权限，则每次执行需要这一权限的操作时您都必须检查自己是否具有该权限。
         * 用户始终可以自由调用此权限，因此，即使应用昨天使用了相机，它不能假设自己今天仍具有该权限。
         * 要检查您是否具有某项权限，请调用 ContextCompat.checkSelfPermission() 方法。
         * 例如，以下代码段显示了如何检查 Activity 是否具有在日历中进行写入的权限：
         *      Assume thisActivity is the current activity
         *      int permissionCheck = ContextCompat.checkSelfPermission(thisActivity,Manifest.permission.WRITE_CALENDAR);
         * 如果应用具有此权限，方法将返回 PackageManager.PERMISSION_GRANTED，并且应用可以继续操作。
         * 如果应用不具有此权限，方法将返回 PERMISSION_DENIED，且应用必须明确向用户要求权限
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result == PackageManager.PERMISSION_DENIED) {
                Log.d("hate", "权限检查:未授权");
                return false;
            }
            String op = AppOpsManagerCompat.permissionToOp(permission);
            if (TextUtils.isEmpty(op)) {
                continue;
            }
            result = AppOpsManagerCompat.noteProxyOp(context, op, context.getPackageName());
            if (result != AppOpsManagerCompat.MODE_ALLOWED) {
                return false;
            }
        }
        return true;
    }
}
