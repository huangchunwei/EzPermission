package com.vvme.permission.install;


import com.vvme.permission.action.Action;
import com.vvme.permission.action.RationaleAction;

import java.io.File;

/**
 * Project name:MyAndroid
 * Author:VV
 * Created on 2018/5/24 19:29.
 * Copyright (c) 2018, vvismile@163.com All Rights Reserved.
 * Description: TODO
 */
public interface InstallRequest {

    InstallRequest file(File result);

    InstallRequest onGranted(Action<File> action);

    InstallRequest onDenied(Action<File> action);

    InstallRequest rationale(RationaleAction<File> action);

    void start();


}
