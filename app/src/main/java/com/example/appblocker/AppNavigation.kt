package com.example.appblocker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appblocker.ui.screens.AddAppToBlockListScreenViewModel
import com.example.appblocker.ui.screens.AddAppToBlockListScreenViewModelFactory
import com.example.appblocker.ui.screens.AddAppToBlocklistScreen
import com.example.appblocker.ui.screens.BlockedAppListScreen
import com.example.appblocker.ui.screens.BlockedAppListScreenViewModelFactory
import com.example.appblocker.ui.screens.BlockedAppListScreenViewModel

val APP_PACKAGE_BLOCK_LIST = stringSetPreferencesKey("app_package_block_list")
val Context.dataStore: DataStore<List<AppBlockListPreferences>> by dataStore(fileName = "appBlockList.json", serializer = AppBlockListPreferencesSerializer)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            App(
                onNavigateToAddApp = { navController.navigate("add_to_blocklist") },
                onNavigateToBlockedList = { navController.navigate("blocked_app_list") }
            )
        }
        composable("add_to_blocklist") {
            val viewModel: AddAppToBlockListScreenViewModel = viewModel(factory = AddAppToBlockListScreenViewModelFactory(LocalContext.current.dataStore))
            AddAppToBlocklistScreen(viewModel)
        }
        composable("blocked_app_list") {
            val viewModel: BlockedAppListScreenViewModel = viewModel(factory = BlockedAppListScreenViewModelFactory(LocalContext.current.dataStore))
            BlockedAppListScreen(viewModel)
        }
    }
}
