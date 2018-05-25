package com.vvme.permission.checker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Project name:EzPermission
 * Author:VV
 * Created on 2018/5/22 15:54.
 * Copyright (c) 2018, huangchunwei715@163.com All Rights Reserved.
 * Description: TODO
 */
public class AndPermissionStandardChecker implements PermissionChecker {

    @Override
    public boolean hasPermission(Context context, String... permission) {
        return hasPermission(context, Arrays.asList(permission));
    }

    @Override
    public boolean hasPermission(Context context, List<String> permissions) {

        /**
         * https://developer.android.com/training/permissions/requesting?hl=zh-cn
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            if (result == PackageManager.PERMISSION_DENIED) {
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
