package com.vvme.permission;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import com.vvme.permission.checker.AndPermissionStandardChecker;
import com.vvme.permission.motivation.Motivation;
import com.vvme.permission.motivation.PermissionMotivation;
import com.vvme.permission.request.PRequest;
import com.vvme.permission.setting.PSetting;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Project name:MyAndroid
 * Author:VV
 * Created on 2018/5/22 15:27.
 * Copyright (c) 2018, vvismile@163.com All Rights Reserved.
 * Description: TODO
 */
public final class EzPermission {

    private static final AndPermissionStandardChecker STANDARD_CHECKER = new AndPermissionStandardChecker();

    private EzPermission() {

    }

    public static PRequest with(Context context) {
        return new PRequest(new PermissionMotivation(context));
    }

    public static PRequest with(Activity activity) {
        return new PRequest(new PermissionMotivation(activity));
    }

    public static PRequest with(Fragment fragment) {
        return new PRequest(new PermissionMotivation(fragment));
    }

    public static PRequest with(android.support.v4.app.Fragment supportFragment) {
        return new PRequest(new PermissionMotivation(supportFragment));
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull String... permissions) {
        return hasPermission(context, Arrays.asList(permissions));
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull List<String> permissions) {
        return STANDARD_CHECKER.hasPermission(context, permissions);
    }

    public static boolean hasPermission(@NonNull Context context, @NonNull String[]... permissions) {
        for (String[] permission : permissions) {
            boolean hasPermission = STANDARD_CHECKER.hasPermission(context, permission);
            if (!hasPermission) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermission(@NonNull Fragment fragment, @NonNull String... permissions) {
        return hasPermission(fragment.getActivity(), Arrays.asList(permissions));
    }

    public static boolean hasPermission(@NonNull Fragment fragment, @NonNull List<String> permissions) {
        return STANDARD_CHECKER.hasPermission(fragment.getActivity(), permissions);
    }

    public static boolean hasPermission(@NonNull Fragment fragment, @NonNull String[]... permissions) {
        for (String[] permission : permissions) {
            boolean hasPermission = STANDARD_CHECKER.hasPermission(fragment.getActivity(), permission);
            if (!hasPermission) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermission(@NonNull android.support.v4.app.Fragment fragment, @NonNull String... permissions) {
        return hasPermission(fragment.getContext(), Arrays.asList(permissions));
    }

    public static boolean hasPermission(@NonNull android.support.v4.app.Fragment fragment, @NonNull List<String> permissions) {
        return STANDARD_CHECKER.hasPermission(fragment.getContext(), permissions);
    }

    public static boolean hasPermission(@NonNull android.support.v4.app.Fragment fragment, @NonNull String[]... permissions) {
        for (String[] permission : permissions) {
            boolean hasPermission = STANDARD_CHECKER.hasPermission(fragment.getContext(), permission);
            if (!hasPermission) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermissionAlwaysDenied(@NonNull Context context, List<String> permissions) {
        return hasPermissionAlwaysDenied(new PermissionMotivation(context), permissions);
    }
    public static boolean hasPermissionAlwaysDenied(@NonNull Fragment fragment, List<String> permissions) {
        return hasPermissionAlwaysDenied(new PermissionMotivation(fragment), permissions);
    }
    public static boolean hasPermissionAlwaysDenied(@NonNull android.support.v4.app.Fragment fragment, List<String> permissions) {
        return hasPermissionAlwaysDenied(new PermissionMotivation(fragment), permissions);
    }

    private static boolean hasPermissionAlwaysDenied(Motivation motivation, List<String> permissions) {
        if (motivation != null) {
            for (String permission : permissions) {
                if (!motivation.isShowRationale(permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean hasPermissionAlwaysDenied(@NonNull Context context, String... permissions) {
        return hasPermissionAlwaysDenied(new PermissionMotivation(context), permissions);
    }
    public static boolean hasPermissionAlwaysDenied(@NonNull Fragment fragment, String... permissions) {
        return hasPermissionAlwaysDenied(new PermissionMotivation(fragment), permissions);
    }
    public static boolean hasPermissionAlwaysDenied(@NonNull android.support.v4.app.Fragment fragment, String... permissions) {
        return hasPermissionAlwaysDenied(new PermissionMotivation(fragment), permissions);
    }

    private static boolean hasPermissionAlwaysDenied(Motivation motivation, String... permissions) {
        if (motivation != null) {
            for (String permission : permissions) {
                if (!motivation.isShowRationale(permission)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static PSetting goPermissionSetting(Context context) {
        return EzPermission.with(context).setting();
    }

    public static PSetting goPermissionSetting(Fragment fragment) {
        return EzPermission.with(fragment).setting();
    }

    public static PSetting goPermissionSetting(android.support.v4.app.Fragment fragment) {
        return EzPermission.with(fragment).setting();
    }

    public static Uri getFileUri(Fragment fragment, File file) {
        return getFileUri(fragment.getActivity(), file);
    }

    public static Uri getFileUri(android.support.v4.app.Fragment fragment, File file) {
        return getFileUri(fragment.getContext(), file);
    }

    public static Uri getFileUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
        }
        return Uri.fromFile(file);
    }
}
