package com.cavss.foodcycling

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cavss.foodcycling.ui.theme.FoodcyclingTheme
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.cavss.foodcycling.ui.custom.bottomnavi.BottomNavigationBarView
import com.cavss.foodcycling.ui.custom.bottomnavi.BottomNavigationScreenView

class MainActivity : ComponentActivity() {
    init {
        val googleMapApiKey = BuildConfig.googleMapApiKey
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodcyclingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Greeting("Android")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Profile,
        BottomNavItem.Settings
    )

    val screens = linkedMapOf<BottomNavItem, @Composable () -> Unit>(
        BottomNavItem.Home to { HomeScreen() },
        BottomNavItem.Profile to { ProfileScreen() },
        BottomNavItem.Settings to { SettingsScreen() },
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBarView(
                navController = navController,
                bottomNaviItemList = items
            )
        }
    ) { innerPadding ->
        BottomNavigationScreenView(
            navController = navController,
            screens = screens,
            modifier = Modifier
                .padding(innerPadding)
        )
    }
}



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

@Composable
fun GoogleMapScreen() {
    val context = LocalContext.current
//    val latLng = getMyLocation(context) // LatLng(40.9971, 29.1007)
    val latLng = LatLng(37.5830, 126.9770) // 청와대 좌표

    val place1 = LatLng(37.5796, 126.9770) // 경복궁
    val place2 = LatLng(37.5793, 126.9803) // 국립현대미술관 서울관
    val place3 = LatLng(37.5824, 126.9860) // 북촌 한옥마을
    val place4 = LatLng(37.5826, 126.9826) // 삼청동 거리

//    val dataStore = DataStoreModule(context)
//    var savedLatitude by remember {
//        mutableDoubleStateOf(0.0)
//    }
//    var savedLongitude by remember {
//        mutableDoubleStateOf(0.0)
//    }
//    LaunchedEffect(Unit) {
//        dataStore.getLatitude.collect {
//            savedLatitude = it
//        }
//    }
//
//    LaunchedEffect(Unit) {
//        dataStore.getLongitude.collect {
//            savedLongitude = it
//        }
//    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 17f)
    }
    val uiSettings = remember {
        mutableStateOf(
            MapUiSettings(
                myLocationButtonEnabled = true,
                zoomControlsEnabled = true
            )
        )
    }
    val properties by remember {
        mutableStateOf(
            MapProperties(
                mapType = MapType.SATELLITE,
                isMyLocationEnabled = true
            )
        )
    }

    val routeCoordinates = listOf(
        LatLng(37.5830, 126.9770), // 시작점: 청와대
        LatLng(37.5796, 126.9770), // 경유지 1: 경복궁
        LatLng(37.5793, 126.9803), // 경유지 2: 국립현대미술관 서울관
        LatLng(37.5824, 126.9860)  // 종점: 북촌 한옥마을
    )


    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings.value
    ) {
//        // makercomposalbe 대신  ver.2를 사용할 수 있다.
//        MarkerComposable(
//            state = MarkerState(position = place1),
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher_background),
//                contentDescription = "",
//            )
//        }
//
//        Polyline(
//            points = routeCoordinates,
//            clickable = true,
//            color = Color.Blue,
//            width = 5f
//        )
//
//        // ver2
//        Marker(
//            icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background),
//            state = MarkerState(position = place2)
//        )

        Marker(
            state = MarkerState(position = place3),
            title = "Marker 1"
        )

//        MarkerInfoWindow(
//            state = MarkerState(position = place4),
//            icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_background)
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
//                modifier = Modifier
//                    .border(
//                        BorderStroke(1.dp, Color.Black),
//                        RoundedCornerShape(10)
//                    )
//                    .clip(RoundedCornerShape(10))
//                    .background(Color.Blue)
//                    .padding(20.dp)
//            ) {
//                Text("Title", fontWeight = FontWeight.Bold, color = Color.White)
//                Text("Description", fontWeight = FontWeight.Medium, color = Color.White)
//            }
//        }
    }
}

@Composable
fun ProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Profile Screen")
    }
}

@Composable
fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Settings Screen")
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Home : BottomNavItem("home", Icons.Default.LocationOn, "Home")
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
    object Settings : BottomNavItem("settings", Icons.Default.Settings, "Settings")

}

data class ShareModel (
    var shareUID : String,
    var sharePlaceName : String,
    var shareItems : List<ShareItem>,

)

enum class ItemType(val rawValue : String) {
    ExpriedFood("ExpriedFood"),
    DonatedFood("DonatedFood")
}

data class ShareItem(
    var itemUID : String,
    var itemName : String,
    var itemImage : String,
    var itemGram : Int,
    var itemType : ItemType,
    var shareUID : String
){

}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FoodcyclingTheme {
        Greeting("Android")
    }
}
