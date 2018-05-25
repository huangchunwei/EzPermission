package com.vvme.permission.motivation;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import java.lang.reflect.Method;

/**
 * Project name:MyAndroid
 * Author:VV
 * Created on 2018/5/22 14:59.
 * Copyright (c) 2018, vvismile@163.com All Rights Reserved.
 * Description: TODO
 */
public class PermissionMotivation implements Motivation {

    private int mMotivationType = Type.CONTEXT_MOTIVATION;

    private Context mContext;
    private Fragment mFragment;
    private android.support.v4.app.Fragment mSupportFragment;

    public PermissionMotivation(Context context) {
        this.mContext = context;
    }

    public PermissionMotivation(Activity context) {
        this.mContext = context;
        this.mMotivationType = Type.ACTIVITY_MOTIVATION;
    }

    public PermissionMotivation(Fragment fragment) {
        this.mFragment = fragment;
        this.mMotivationType = Type.FRAGMENT_MOTIVATION;
    }

    public PermissionMotivation(android.support.v4.app.Fragment supportFragment) {
        this.mSupportFragment = supportFragment;
        this.mMotivationType = Type.SUPPORT_FRAGMENT_MOTIVATION;
    }

    @Override
    public Context getContext() {
        switch (mMotivationType) {
            case Type.FRAGMENT_MOTIVATION:
                return mFragment.getActivity();
            case Type.SUPPORT_FRAGMENT_MOTIVATION:
                return mSupportFragment.getContext();
        }
        return mContext;
    }

    @Override
    public int getMotivationType() {
        return mMotivationType;
    }

    @Override
    public void startActivity(Intent intent) {
        switch (mMotivationType) {
            case Type.CONTEXT_MOTIVATION:
                mContext.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case Type.ACTIVITY_MOTIVATION:
                mContext.startActivity(intent);
                break;
            case Type.FRAGMENT_MOTIVATION:
                mFragment.startActivity(intent);
                break;
            case Type.SUPPORT_FRAGMENT_MOTIVATION:
                mSupportFragment.startActivity(intent);
                break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        switch (mMotivationType) {
            case Type.CONTEXT_MOTIVATION:
                mContext.startActivity(intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            case Type.ACTIVITY_MOTIVATION:
                ((Activity) mContext).startActivityForResult(intent, requestCode);
                break;
            case Type.FRAGMENT_MOTIVATION:
                mFragment.startActivityForResult(intent, requestCode);
                break;
            case Type.SUPPORT_FRAGMENT_MOTIVATION:
                mSupportFragment.startActivityForResult(intent, requestCode);
                break;
        }
    }

    @Override
    public boolean isShowRationale(String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false;
        }
        switch (mMotivationType) {
            case Type.CONTEXT_MOTIVATION:
                PackageManager packageManager = mContext.getPackageManager();
                Class<?> pkManagerClass = packageManager.getClass();
                try {
                    Method method = pkManagerClass.getMethod("shouldShowRequestPermissionRationale", String.class);
                    if (!method.isAccessible()) method.setAccessible(true);
                    return (boolean) method.invoke(packageManager, permission);
                } catch (Exception ignored) {
                    return false;
                }
            case Type.ACTIVITY_MOTIVATION:
                return ((Activity) mContext).shouldShowRequestPermissionRationale(permission);
            case Type.FRAGMENT_MOTIVATION:
                return mFragment.shouldShowRequestPermissionRationale(permission);
            case Type.SUPPORT_FRAGMENT_MOTIVATION:
                return mSupportFragment.shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }
}
