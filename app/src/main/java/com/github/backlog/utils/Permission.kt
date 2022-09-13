package com.github.backlog.utils

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
fun launchWithPermission(permission: PermissionState, launcherRunnable: () -> Unit) {
    if (permission.status.isGranted) {
        launcherRunnable()
    } else {
        permission.launchPermissionRequest()
    }
}
