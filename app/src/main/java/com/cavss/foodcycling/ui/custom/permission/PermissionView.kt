package com.cavss.foodcycling.ui.custom.permission

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun PermissionView(
    permissions : Array<String>,
    grantedView : @Composable () -> Unit,
    permissionRequestView: @Composable (() -> Unit) -> Unit,
    rationaleView: @Composable (
        showRationale: Boolean,
        onRationaleReply: (Boolean) -> Unit
    ) -> Unit
) {

    val context = LocalContext.current
    var hasPermissions by remember { mutableStateOf(false) }
    var showRationale by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        hasPermissions = permissionsMap.values.all { it }
        if (!hasPermissions) {
            showRationale = true
        }
    }

    val checkAndRequestPermissions = {
        hasPermissions = permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        if (!hasPermissions) {
            requestPermissionLauncher.launch(permissions)
        }
    }

    LaunchedEffect(Unit) {
        checkAndRequestPermissions()
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (hasPermissions) {
            grantedView()
        } else {
            permissionRequestView { checkAndRequestPermissions() }
        }

        if (showRationale) {
            rationaleView(
                showRationale = showRationale,
                onRationaleReply = { understood ->
                    showRationale = false
                    if (understood) {
                        checkAndRequestPermissions()
                    }
                }
            )
        }
    }
}
