package com.vvme.permission.ui;

import android.os.Handler;
import android.os.Looper;

/**
 * Project name:EzPermission
 * Author:VV
 * Created on 2018/5/22 17:20.
 * Copyright (c) 2018, huangchunwei715@163.com All Rights Reserved.
 * Description: TODO
 */
public class PermissionHandler {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());


    public void post(Runnable runnable) {
        HANDLER.post(runnable);
    }

    public void postDelayed(Runnable runnable, long delayedMillis) {
        HANDLER.postDelayed(runnable, delayedMillis);
    }

}
