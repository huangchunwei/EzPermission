package com.vvme.permission.request;

import android.os.Build;

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
 * Project name:EzPermission
 * Author:VV
 * Created on 2018/5/22 18:06.
 * Copyright (c) 2018, huangchunwei715@163.com All Rights Reserved.
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
            HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<String> permissions = generatePermissions(mMotivation, new ArrayList<String>(fInternalPermission));
                    if (permissions.isEmpty()) {
                        sendSuccess(new ArrayList<String>(fInternalPermission));
                    } else {
                        sendFailed(permissions);
                    }
                }
            }, 100);
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
            sendSuccess(new ArrayList<String>(fInternalPermission));
            return;
        }
        List<String> permissions = generatePermissions(mMotivation, new ArrayList<String>(fInternalPermission));
        mRealPermissions = permissions.toArray(new String[permissions.size()]);
        if (!permissions.isEmpty()) {
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
        List<String> permissionList = new ArrayList<>(1);
        for (String permission : permissions) {
            if (!CHECKER.hasPermission(motivation.getContext(), permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList;
    }

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
