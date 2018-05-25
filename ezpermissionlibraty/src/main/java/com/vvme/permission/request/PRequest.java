package com.vvme.permission.request;


import com.vvme.permission.action.Action;
import com.vvme.permission.action.RationaleAction;
import com.vvme.permission.install.IInstall;
import com.vvme.permission.install.InstallRequest;
import com.vvme.permission.motivation.Motivation;
import com.vvme.permission.setting.ISetting;
import com.vvme.permission.setting.PSetting;

import java.util.List;

/**
 * Project name:MyAndroid
 * Author:VV
 * Created on 2018/5/22 14:36.
 * Copyright (c) 2018, vvismile@163.com All Rights Reserved.
 * Description: TODO
 */
public final class PRequest implements IPRequest<PRequest>, ISetting<PSetting>, IInstall<InstallRequest> {

    private RealPRequest mRealPRequest;

    public PRequest(Motivation motivation) {
        this.mRealPRequest = new RealPRequest(motivation);
    }

    @Override
    public PRequest permission(String... permission) {
        mRealPRequest.permission(permission);
        return this;
    }

    @Override
    public PRequest permission(List<String> permissions) {
        mRealPRequest.permission(permissions);
        return this;
    }

    @Override
    public PRequest rationale(RationaleAction<List<String>> action) {
        mRealPRequest.rationale(action);
        return this;
    }

    @Override
    public PRequest onDenied(Action<List<String>> action) {
        mRealPRequest.onDenied(action);
        return this;
    }

    @Override
    public PRequest onGranted(Action<List<String>> action) {
        mRealPRequest.onGranted(action);
        return this;
    }

    @Override
    public void start() {
        mRealPRequest.start();
    }

    @Override
    public PSetting setting() {
        return new PSetting(mRealPRequest.getMotivation());
    }

    @Override
    public InstallRequest install() {
        return InstallRequestFactory.create(mRealPRequest.getMotivation());
    }
}
