package com.vvme.permission.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.vvme.permission.motivation.PermissionMotivation;
import com.vvme.permission.setting.PSetting;


/**
 * Project name:MyAndroid
 * Author:VV
 * Created on 2018/5/22 14:34.
 * Copyright (c) 2018, vvismile@163.com All Rights Reserved.
 * Description: TODO
 */
public class PermissionActivity extends Activity {

    private static RequestPermissionListener sRequestListener;

    private static final String KEY_REQUEST_PERMISSIONS = "KEY_REQUEST_PERMISSIONS";
    private static final String KEY_REQUEST_OPERATION = "KEY_REQUEST_OPERATION";

    private static final int VALUE_PERMISSION_OPERATION = 1;
    private static final int VALUE_PERMISSION_SETTING_OPERATION = 2;
    private static final int VALUE_PERMISSION_INSTALL_OPERATION = 3;


    public static void startRequestPermission(Context context, String[] permissions, RequestPermissionListener listener) {
        PermissionActivity.sRequestListener = listener;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_REQUEST_OPERATION, VALUE_PERMISSION_OPERATION);
        intent.putExtra(KEY_REQUEST_PERMISSIONS, permissions);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startRequestSetting(Context context, RequestPermissionListener listener) {
        PermissionActivity.sRequestListener = listener;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_REQUEST_OPERATION, VALUE_PERMISSION_SETTING_OPERATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startRequestInstall(Context context, RequestPermissionListener listener) {
        PermissionActivity.sRequestListener = listener;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.putExtra(KEY_REQUEST_OPERATION, VALUE_PERMISSION_INSTALL_OPERATION);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(this);
        getAndRequestPermissions(getIntent());
    }

    private void init(PermissionActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void getAndRequestPermissions(Intent intent) {
        if (intent != null) {
            switch (intent.getIntExtra(KEY_REQUEST_OPERATION, 0)) {
                case VALUE_PERMISSION_OPERATION:
                    String[] permissions = intent.getStringArrayExtra(KEY_REQUEST_PERMISSIONS);
                    if (permissions == null || permissions.length == 0) {
                        finish();
                        return;
                    }
                    if (sRequestListener == null) {
                        finish();
                        return;
                    }
                    Log.d("hate", "PermissionActivity进行权限申请");
                    requestPermissions(permissions, VALUE_PERMISSION_OPERATION);
                    break;
                case VALUE_PERMISSION_SETTING_OPERATION:
                    //跳转到权限设置页面
                    PSetting pSetting = new PSetting(new PermissionMotivation(PermissionActivity.this));
                    pSetting.execute(VALUE_PERMISSION_SETTING_OPERATION);
                    break;
                case VALUE_PERMISSION_INSTALL_OPERATION: {
                    if (sRequestListener == null) {
                        finish();
                        return;
                    }
                    Intent installIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    installIntent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivityForResult(installIntent, VALUE_PERMISSION_INSTALL_OPERATION);
                    break;
                }
                default:
                    throw new UnsupportedOperationException("The operation is so bad that it's not supported at the moment");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (sRequestListener != null) {
            sRequestListener.onRequestCallback();
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (sRequestListener != null) {
            sRequestListener.onRequestCallback();
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sRequestListener = null;
    }

    public interface RequestPermissionListener {
        void onRequestCallback();
    }
}
