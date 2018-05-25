package com.vvme.permission.checker;

import android.content.Context;

import java.util.List;

/**
 * Project name:MyAndroid
 * Author:VV
 * Created on 2018/5/22 15:49.
 * Copyright (c) 2018, vvismile@163.com All Rights Reserved.
 * Description: TODO
 */
public interface PermissionChecker {
    boolean hasPermission(Context context, String... permission);

    boolean hasPermission(Context context, List<String> permission);
}
