package com.vvme.permission.setting;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.vvme.permission.action.SettingAction;
import com.vvme.permission.motivation.Motivation;
import com.vvme.permission.ui.PermissionActivity;


/**
 * Project name:MyAndroid
 * Author:VV
 * Created on 2018/5/23 19:14.
 * Copyright (c) 2018, vvismile@163.com All Rights Reserved.
 * Description: 参考AndPermission   https://github.com/yanzhenjie/AndPermission/blob/master/permission/src/main/java/com/yanzhenjie/permission/runtime/setting/SettingPage.java
 */
public final class PSetting implements PermissionActivity.RequestPermissionListener {

    private static final String MANUFACTURER = Build.MANUFACTURER.toLowerCase();

    private final Motivation fMotivation;
    private SettingAction mSettingAction;

    public PSetting(Motivation motivation) {
        fMotivation = motivation;
    }

    public PSetting action(SettingAction action) {
        mSettingAction = action;
        return this;
    }

    public void execute(int requestCode) {
        Intent intent;
        if (MANUFACTURER.contains("huawei")) {
            intent = huaweiApi(fMotivation.getContext());
        } else if (MANUFACTURER.contains("xiaomi")) {
            intent = xiaomiApi(fMotivation.getContext());
        } else if (MANUFACTURER.contains("oppo")) {
            intent = oppoApi(fMotivation.getContext());
        } else if (MANUFACTURER.contains("vivo")) {
            intent = vivoApi(fMotivation.getContext());
        } else if (MANUFACTURER.contains("samsung")) {
            intent = samsungApi(fMotivation.getContext());
        } else if (MANUFACTURER.contains("meizu")) {
            intent = meizuApi(fMotivation.getContext());
        } else if (MANUFACTURER.contains("smartisan")) {
            intent = smartisanApi(fMotivation.getContext());
        } else {
            intent = defaultApi(fMotivation.getContext());
        }
        try {
            fMotivation.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            intent = defaultApi(fMotivation.getContext());
            fMotivation.startActivityForResult(intent, requestCode);
        }
    }

    public final void start() {
        PermissionActivity.startRequestSetting(fMotivation.getContext(), this);
    }

    /**
     * App details page.
     */
    private static Intent defaultApi(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }

    /**
     * Huawei cell phone Api23 the following method.
     */
    private static Intent huaweiApi(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return defaultApi(context);
        }
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity"));
        return intent;
    }

    /**
     * Xiaomi phone to achieve the method.
     */
    private static Intent xiaomiApi(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.putExtra("extra_pkgname", context.getPackageName());
        return intent;
    }

    /**
     * Vivo phone to achieve the method.
     */
    private static Intent vivoApi(Context context) {
        Intent intent = new Intent();
        intent.putExtra("packagename", context.getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity"));
        } else {
            intent.setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.safeguard.SoftPermissionDetailActivity"));
        }
        return intent;
    }

    /**
     * Oppo phone to achieve the method.
     */
    private static Intent oppoApi(Context context) {
        return defaultApi(context);
    }

    /**
     * Meizu phone to achieve the method.
     */
    private static Intent meizuApi(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return defaultApi(context);
        }
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.putExtra("packageName", context.getPackageName());
        intent.setComponent(new ComponentName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity"));
        return intent;
    }

    /**
     * Smartisan phone to achieve the method.
     */
    private static Intent smartisanApi(Context context) {
        return defaultApi(context);
    }

    /**
     * Samsung phone to achieve the method.
     */
    private static Intent samsungApi(Context context) {
        return defaultApi(context);
    }


    @Override
    public void onRequestCallback() {
        Log.d("hate", "设置页面: onRequestCallback()");
        if (mSettingAction != null) {
            mSettingAction.onAction();
        }
    }
}
