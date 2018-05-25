package com.vvme.permission.action;

/**
 * Project name:EzPermission
 * Author:VV
 * Created on 2018/5/22 14:36.
 * Copyright (c) 2018, huangchunwei715@163.com All Rights Reserved.
 * Description: TODO
 */
public interface Action<T> {
    void onAction(T data);
}
