package com.vvme.permission.request;

import android.os.Build;
import android.util.Log;

import com.vvme.permission.action.Action;
import com.vvme.permission.action.RationaleAction;
import com.vvme.permission.action.SettingAction;
import com.vvme.permission.checker.AndPermissionStandardChecker;
import com.vvme.permission.motivation.Motivation;
import com.vvme.permission.ui.PermissionActivity;
import com.vvme.permission.ui.PermissionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Project name:MyAndroid
 * Author:VV
 * Created on 2018/5/22 18:06.
 * Copyright (c) 2018, vvismile@163.com All Rights Reserved.
 * Description: TODO
 */
public class RealPRequest implements IPRequest<RealPRequest>, PermissionExecutor {

    private static final PermissionHandler HANDLER = new PermissionHandler();
    private static final AndPermissionStandardChecker CHECKER = new AndPermissionStandardChecker();

    private final Motivation mMotivation;
    private final Set<String> fInternalPermission;
    private String[] mRealPermissions;
    private Action mGrantedAction;
    private Action mDeniedAction;
    private RationaleAction mRationaleAction;
    private SettingAction mSettingAction;

    private PermissionActivity.RequestPermissionListener mPermissionListener = new PermissionActivity.RequestPermissionListener() {
        @Override
        public void onRequestCallback() {
            Log.d("hate", "申请权限的回调");
            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<String> permissions = generatePermissions(mMotivation, new ArrayList<String>(fInternalPermission));
                    if (permissions.isEmpty()) {
                        //授权成功
                        sendSuccess(new ArrayList<String>(fInternalPermission));
                    } else {
                        //授权失败
                        sendFailed(permissions);
                    }
                }
            }, 100);
        }
    };

    private PermissionActivity.RequestPermissionListener mSettingCallback = new PermissionActivity.RequestPermissionListener() {
        @Override
        public void onRequestCallback() {
            Log.d("hate", "跳转设置页面的回调");
        }
    };

    RealPRequest(Motivation motivation) {
        mMotivation = motivation;
        fInternalPermission = new LinkedHashSet<>();
    }

    public Motivation getMotivation() {
        return mMotivation;
    }

    @Override
    public RealPRequest permission(String... permission) {
        fInternalPermission.addAll(Arrays.asList(permission));
        return this;
    }

    @Override
    public RealPRequest permission(List<String> permissions) {
        fInternalPermission.addAll(permissions);
        return this;
    }

    @Override
    public RealPRequest rationale(RationaleAction<List<String>> action) {
        mRationaleAction = action;
        return this;
    }

    @Override
    public RealPRequest onDenied(Action<List<String>> action) {
        mDeniedAction = action;
        return this;
    }

    @Override
    public RealPRequest onGranted(Action<List<String>> action) {
        mGrantedAction = action;
        return this;
    }

    @Override
    public void start() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // TODO: 2018/5/22 不需要动态申请权限
            sendSuccess(new ArrayList<String>(fInternalPermission));
            return;
        }
        List<String> permissions = generatePermissions(mMotivation, new ArrayList<String>(fInternalPermission));
        mRealPermissions = permissions.toArray(new String[permissions.size()]);
        if (!permissions.isEmpty()) {
            //需要申请权限
            List<String> rationalePermissions = generateRationalePermission(mMotivation, permissions);
            if (!rationalePermissions.isEmpty()) {
                if (mRationaleAction != null) {
                    mRationaleAction.onAction(mMotivation.getContext(), rationalePermissions, this);
                }
            } else {
                execute();
            }
        } else {
            sendSuccess(new ArrayList<String>(fInternalPermission));
        }
    }

    private List<String> generatePermissions(Motivation motivation, List<String> permissions) {
        //初始化长度1
        List<String> permissionList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (!CHECKER.hasPermission(motivation.getContext(), permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList;
    }

    /**
     * 是否需要对权限进行解释
     *
     * @param motivation
     * @param permissions
     * @return
     */
    private List<String> generateRationalePermission(Motivation motivation, List<String> permissions) {
        List<String> rationaleList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (motivation.isShowRationale(permission)) {
                rationaleList.add(permission);
            }
        }
        return rationaleList;
    }

    private void sendSuccess(List<String> permissions) {
        if (mGrantedAction != null) {
            try {
                mGrantedAction.onAction(permissions);
            } catch (Exception e) {
                e.printStackTrace();
                if (mDeniedAction != null) {
                    mDeniedAction.onAction(permissions);
                }
            }
        }
    }

    private void sendFailed(List<String> permissions) {
        if (mDeniedAction != null) {
            mDeniedAction.onAction(permissions);
        }
    }

    @Override
    public void execute() {
        PermissionActivity.startRequestPermission(mMotivation.getContext(), mRealPermissions, mPermissionListener);
    }

    @Override
    public void cancel() {
        mPermissionListener.onRequestCallback();
    }

}
