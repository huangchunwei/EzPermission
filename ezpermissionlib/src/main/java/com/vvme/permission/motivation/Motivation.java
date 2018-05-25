package com.vvme.permission.motivation;

import android.content.Context;
import android.content.Intent;

/**
 * Project name:EzPermission
 * Author:VV
 * Created on 2018/5/22 14:39.
 * Copyright (c) 2018, huangchunwei715@163.com All Rights Reserved.
 * Description: TODO
 */
public interface Motivation {

    interface Type {
        int CONTEXT_MOTIVATION = 0x3000;
        int ACTIVITY_MOTIVATION = 0x4000;
        int FRAGMENT_MOTIVATION = 0x5000;
        int SUPPORT_FRAGMENT_MOTIVATION = 0x6000;
    }

    Context getContext();

    int getMotivationType();

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

    boolean isShowRationale(String permission);

}
