package com.vvme.permission.install;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.vvme.permission.EzPermission;
import com.vvme.permission.action.Action;
import com.vvme.permission.action.RationaleAction;
import com.vvme.permission.motivation.Motivation;
import com.vvme.permission.request.PermissionExecutor;
import com.vvme.permission.ui.PermissionActivity;
import com.vvme.permission.ui.PermissionHandler;

import java.io.File;

/**
 * Project name:MyAndroid
 * Author:VV
 * Created on 2018/5/24 18:24.
 * Copyright (c) 2018, vvismile@163.com All Rights Reserved.
 * Description: TODO
 */
public class OInstallRequest implements InstallRequest, PermissionExecutor, PermissionActivity.RequestPermissionListener {

    private static final PermissionHandler HANDLER = new PermissionHandler();

    private final Motivation mMotivation;
    private File mFile;
    private Action mGrantedAction;
    private Action mDeniedAction;

    private RationaleAction<File> mRationaleAction = new RationaleAction<File>() {
        @Override
        public void onAction(Context context, File data, PermissionExecutor executor) {
            executor.execute();
        }
    };

    public OInstallRequest(Motivation motivation) {
        mMotivation = motivation;
    }

    @Override
    public InstallRequest file(File result) {
        mFile = result;
        return this;
    }

    @Override
    public InstallRequest onGranted(Action<File> action) {
        mGrantedAction = action;
        return this;
    }

    @Override
    public InstallRequest onDenied(Action<File> action) {
        mDeniedAction = action;
        return this;
    }

    @Override
    public InstallRequest rationale(RationaleAction<File> action) {
        mRationaleAction = action;
        return this;
    }

    @Override
    public void start() {
        if (canRequestPackageInstalls(mMotivation.getContext())) {
            sendSuccess();
            doInstall();
        } else {
            mRationaleAction.onAction(mMotivation.getContext(), mFile, this);
        }
    }

    @Override
    public void execute() {
        PermissionActivity.startRequestInstall(mMotivation.getContext(), this);
    }

    @Override
    public void cancel() {
        sendFailed();
    }

    private boolean canRequestPackageInstalls(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    private void sendSuccess() {
        if (mGrantedAction != null) {
            mGrantedAction.onAction(mFile);
        }
    }

    private void sendFailed() {
        if (mDeniedAction != null) {
            mDeniedAction.onAction(mFile);
        }
    }

    private void doInstall() {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = EzPermission.getFileUri(mMotivation.getContext(), mFile);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mMotivation.startActivity(intent);
    }

    @Override
    public void onRequestCallback() {
        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (canRequestPackageInstalls(mMotivation.getContext())) {
                    sendSuccess();
                    doInstall();
                } else {
                    sendFailed();
                }
            }
        }, 200);
    }
}
