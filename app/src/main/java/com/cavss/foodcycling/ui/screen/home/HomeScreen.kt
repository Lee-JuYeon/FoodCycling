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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.cavss.foodcycling.ui.custom.googlemap.GoogleMapScreen


@Composable
fun HomeScreen() {


    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(false) }
    var showRationale by remember { mutableStateOf(false) }

    val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val areGranted = permissionsMap.values.reduce { acc, next -> acc && next } // granted되면 리스트에서 제거
        hasLocationPermission = areGranted // granted되면 리스트에서 제거하는 작업 실행
        if (!areGranted) {
            showRationale = true // 만약 하나라도 granted 안되어있다면, showRationale변수에 의해 다시 실행됨.
        }
    }

    LaunchedEffect(key1 = Unit) {
        hasLocationPermission = permissionList.all { // 권한 리스트 하나하나 권한이 granted인지 체크하는 반복문
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        if (!hasLocationPermission) { // 권한이 없다면,  권한요청 실행
            requestPermissionLauncher.launch(permissionList)
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(text = "Home Screen", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(16.dp))

        if (hasLocationPermission) {
            GoogleMapScreen()
        } else {
            Text(
                text = "위치 권한이 필요합니다.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    requestPermissionLauncher.launch(permissionList)
                }
            ) {
                Text("권한 요청")
            }
        }

        if (showRationale) {
            AlertDialog(
                onDismissRequest = { showRationale = false },
                title = { Text("위치 권한 필요") },
                text = { Text("이 앱은 지도 기능을 위해 위치 권한이 필요합니다. 설정에서 권한을 허용해주세요.") },
                confirmButton = {
                    Button(onClick = {
                        showRationale = false

                    }) {
                        Text("설정으로 이동")
                    }
                },
                dismissButton = {
                    Button(onClick = { showRationale = false }) {
                        Text("취소")
                    }
                }
            )
        }
    }
}
