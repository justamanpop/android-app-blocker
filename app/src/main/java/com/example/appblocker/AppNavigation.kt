package com.example.appblocker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appblocker.ui.screens.addAppToBlockListScreen.AddAppToBlockListScreenViewModel
import com.example.appblocker.ui.screens.addAppToBlockListScreen.AddAppToBlockListScreenViewModelFactory
import com.example.appblocker.ui.screens.addAppToBlockListScreen.AddAppToBlocklistScreen
import com.example.appblocker.ui.screens.addBlockSetScreen.AddBlockSetScreen
import com.example.appblocker.ui.screens.addBlockSetScreen.AddBlockSetScreenViewModel
import com.example.appblocker.ui.screens.addBlockSetScreen.AddBlockSetScreenViewModelFactory
import com.example.appblocker.ui.screens.blockSetListScreen.BlockSetListScreen
import com.example.appblocker.ui.screens.blockSetListScreen.BlockSetListScreenViewModel
import com.example.appblocker.ui.screens.blockSetListScreen.BlockSetListScreenViewModelFactory
import com.example.appblocker.ui.screens.blockedAppListScreen.BlockedAppListScreen
import com.example.appblocker.ui.screens.blockedAppListScreen.BlockedAppListScreenViewModelFactory
import com.example.appblocker.ui.screens.blockedAppListScreen.BlockedAppListScreenViewModel

val Context.dataStore: DataStore<List<AppBlockListPreferences>> by dataStore(
    fileName = "appBlockList.json",
    serializer = AppBlockListPreferencesSerializer
)

val Context.dataStore2: DataStore<List<AppBlockSetPreferences>> by dataStore(
    fileName = "appBlockSet.json",
    serializer = AppBlockSetPreferencesSerializer
)

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home", modifier = modifier) {
        composable("home") {
            App(onNavigateToManageBlockSets = { navController.navigate("block_set_list") })
        }
        composable("block_set_list") {
            val viewModel: BlockSetListScreenViewModel =
                viewModel(factory = BlockSetListScreenViewModelFactory(LocalContext.current.dataStore2))
            BlockSetListScreen(viewModel, {navController.navigate("add_block_set")})
        }
        composable("add_block_set") {
            val viewModel: AddBlockSetScreenViewModel =
                viewModel(factory = AddBlockSetScreenViewModelFactory(LocalContext.current.dataStore2))
            AddBlockSetScreen(viewModel)
        }
        composable("blocked_app_list") {
            val viewModel: BlockedAppListScreenViewModel =
                viewModel(factory = BlockedAppListScreenViewModelFactory(LocalContext.current.dataStore))
            BlockedAppListScreen(
                viewModel,
                onNavigateToAddApp = { navController.navigate("add_to_blocklist") })
        }
        composable("add_to_blocklist") {
            val viewModel: AddAppToBlockListScreenViewModel =
                viewModel(
                    factory = AddAppToBlockListScreenViewModelFactory(
                        LocalContext.current.dataStore
                    )
                )
            AddAppToBlocklistScreen(viewModel)
        }
    }
}
