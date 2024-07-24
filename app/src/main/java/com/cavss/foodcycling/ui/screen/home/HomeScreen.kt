package com.cavss.foodcycling.ui.screen.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.cavss.foodcycling.ui.custom.googlemap.GoogleMapScreen
import com.cavss.foodcycling.ui.custom.permission.PermissionView


@Composable
fun HomeScreen() {

    val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    PermissionView(
        permissions = permissionList,
        grantedView = {
            GoogleMapScreen()
        },
        permissionRequestView = { requestPermissionsLauncher ->
            Column {
                Text("이 앱은 위치 기능을 사용하기 위해 위치 권한이 필요합니다.")
                Button(onClick = requestPermissionsLauncher) {
                    Text("위치 권한 요청")
                }
            }
        },
        rationaleView = { showRationale, onRationaleReply ->
            if (showRationale) {
                AlertDialog(
                    onDismissRequest = { onRationaleReply(false) },
                    title = { Text("위치 권한 필요") },
                    text = { Text("이 앱은 주변 정보를 제공하기 위해 위치 권한이 필요합니다. 권한을 허용하시겠습니까?") },
                    confirmButton = {
                        Button(onClick = { onRationaleReply(true) }) {
                            Text("확인")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { onRationaleReply(false) }) {
                            Text("취소")
                        }
                    }
                )
            }
        }
    )
}
