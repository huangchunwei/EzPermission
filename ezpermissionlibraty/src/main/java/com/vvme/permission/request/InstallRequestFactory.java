package com.vvme.permission.request;

import android.os.Build;

import com.vvme.permission.install.InstallRequest;
import com.vvme.permission.install.NInstallRequest;
import com.vvme.permission.install.OInstallRequest;
import com.vvme.permission.motivation.Motivation;


/**
 * Project name:MyAndroid
 * Author:VV
 * Created on 2018/5/24 19:38.
 * Copyright (c) 2018, vvismile@163.com All Rights Reserved.
 * Description: TODO
 */
public class InstallRequestFactory {

    public static InstallRequest create(Motivation motivation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new OInstallRequest(motivation);
        } else {
            return new NInstallRequest(motivation);
        }
    }
}
