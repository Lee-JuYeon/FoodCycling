package com.cavss.foodcycling.ui.custom.googlemap

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun GoogleMapScreen() {
    val context = LocalContext.current
    val latLng = LatLng(37.5830, 126.9770) // 청와대 좌표

    val place1 = LatLng(37.5796, 126.9770) // 경복궁
    val place2 = LatLng(37.5793, 126.9803) // 국립현대미술관 서울관
    val place3 = LatLng(37.5824, 126.9860) // 북촌 한옥마을
    val place4 = LatLng(37.5826, 126.9826) // 삼청동 거리


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