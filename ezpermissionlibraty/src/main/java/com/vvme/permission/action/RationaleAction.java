package com.vvme.permission.action;

import android.content.Context;

import com.vvme.permission.request.PermissionExecutor;

/**
 * Project name:MyAndroid
 * Author:VV
 * Created on 2018/5/22 16:14.
 * Copyright (c) 2018, vvismile@163.com All Rights Reserved.
 * Description: TODO
 */
public interface RationaleAction<T> {

    void onAction(final Context context, T data, final PermissionExecutor executor);
}
