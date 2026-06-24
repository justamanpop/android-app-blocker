package com.example.appblocker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appblocker.ui.screens.AddAppToBlocklistScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            App(
                onNavigateToAddApp = { navController.navigate("add_to_blocklist") }
            )
        }
        composable("add_to_blocklist") {
            AddAppToBlocklistScreen()
        }
    }
}
