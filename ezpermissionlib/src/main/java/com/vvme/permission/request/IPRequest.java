package com.vvme.permission.request;


import com.vvme.permission.action.Action;
import com.vvme.permission.action.RationaleAction;

import java.util.List;

/**
 * Project name:EzPermission
 * Author:VV
 * Created on 2018/5/22 16:04.
 * Copyright (c) 2018, huangchunwei715@163.com All Rights Reserved.
 * Description: TODO
 */
public interface IPRequest<P> {

    P permission(String... permission);

    P permission(List<String> permissions);

    P rationale(RationaleAction<List<String>> action);

    P onDenied(Action<List<String>> action);

    P onGranted(Action<List<String>> action);

    void start();

}
