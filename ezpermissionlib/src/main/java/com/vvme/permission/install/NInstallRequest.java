package com.vvme.permission.install;

import android.content.Intent;
import android.net.Uri;

import com.vvme.permission.EzPermission;
import com.vvme.permission.action.Action;
import com.vvme.permission.action.RationaleAction;
import com.vvme.permission.motivation.Motivation;

import java.io.File;

/**
 * Project name:EzPermission
 * Author:VV
 * Created on 2018/5/24 19:35.
 * Copyright (c) 2018, huangchunwei715@163.com All Rights Reserved.
 * Description: TODO
 */
public class NInstallRequest implements InstallRequest {

    private final Motivation mMotivation;
    private File mFile;
    private Action<File> mGrantedAction;

    public NInstallRequest(Motivation motivation) {
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
        return this;
    }

    @Override
    public InstallRequest rationale(RationaleAction<File> action) {
        return null;
    }

    @Override
    public void start() {
        sendSuccess();
        doInstall();
    }

    private void doInstall() {
        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = EzPermission.getFileUri(mMotivation.getContext(), mFile);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mMotivation.startActivity(intent);
    }

    private void sendSuccess() {
        if (mGrantedAction != null) {
            mGrantedAction.onAction(mFile);
        }
    }
}
