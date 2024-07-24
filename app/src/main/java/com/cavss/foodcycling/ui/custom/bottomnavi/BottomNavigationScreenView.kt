package com.cavss.foodcycling.ui.custom.bottomnavi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun BottomNavigationScreenView(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    screens: Map<BottomNavItem, @Composable () -> Unit>,
) {
    NavHost(
        navController = navController,
        startDestination = screens.keys.first().route,
        modifier = modifier
    ) {
        screens.forEach { (screen, view) ->
            composable(screen.route) { view() }
        }
    }
}
