package com.sajjad.base.dsl

import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

inline fun ComponentActivity.requestPermission(
    permission: String,
    crossinline onGranted: () -> Unit = {},
    crossinline showRational: () -> Unit = {},
    crossinline onDenied: () -> Unit = {}
) {
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        when {
            granted -> onGranted()
            shouldShowRequestPermissionRationale(permission) -> showRational()
            else -> onDenied()
        }
    }
        .launch(permission)
}

inline fun Fragment.requestPermission(
    permission: String,
    crossinline onGranted: () -> Unit = {},
    crossinline showRational: () -> Unit = {},
    crossinline onDenied: () -> Unit = {}
) {
    registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        when {
            granted -> onGranted()
            shouldShowRequestPermissionRationale(permission) -> showRational()
            else -> onDenied()
        }
    }
        .launch(permission)
}