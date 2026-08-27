package com.example.appblocker

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appblocker.ui.screens.addAppToBlockListScreen.AddAppToBlockListScreenViewModel
import com.example.appblocker.ui.screens.addAppToBlockListScreen.AddAppToBlockListScreenViewModelFactory
import com.example.appblocker.ui.screens.addAppToBlockListScreen.AddAppToBlocklistScreen
import com.example.appblocker.ui.screens.addBlockSetScreen.AddBlockSetScreen
import com.example.appblocker.ui.screens.addBlockSetScreen.AddBlockSetScreenViewModel
import com.example.appblocker.ui.screens.addBlockSetScreen.AddBlockSetScreenViewModelFactory
import com.example.appblocker.ui.screens.blockSetDetails.BlockSetDetailsScreen
import com.example.appblocker.ui.screens.blockSetDetails.BlockSetDetailsScreenViewModel
import com.example.appblocker.ui.screens.blockSetDetails.BlockSetDetailsScreenViewModelFactory
import com.example.appblocker.ui.screens.blockSetListScreen.BlockSetListScreen
import com.example.appblocker.ui.screens.blockSetListScreen.BlockSetListScreenViewModel
import com.example.appblocker.ui.screens.blockSetListScreen.BlockSetListScreenViewModelFactory
import com.example.appblocker.ui.screens.settingsScreen.SettingsScreen
import com.example.appblocker.ui.screens.settingsScreen.SettingsScreenViewModel
import com.example.appblocker.ui.screens.settingsScreen.SettingsScreenViewModelFactory
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<List<AppBlockSetPreferences>> by dataStore(
    fileName = "appBlockSet.json",
    serializer = AppBlockSetPreferencesSerializer
)
val Context.settingsDataStore: DataStore<AppSettingsPreferences> by dataStore(
    fileName = "settings.json",
    serializer = AppSettingsPreferencesSerializer
)

@Composable
fun AppNavigation(modifier: Modifier = Modifier, appRepository: AppRepository) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            App(
                onNavigateToManageBlockSets = { navController.navigate("block_set_list") },
                onNavigateToSettings = { navController.navigate("settings") })
        }

        composable("settings") {
            val viewModel: SettingsScreenViewModel =
                viewModel(factory = SettingsScreenViewModelFactory(LocalContext.current.settingsDataStore))
            SettingsScreen(viewModel, { navController.navigate("home") })
        }

        composable("block_set_list") {
            val viewModel: BlockSetListScreenViewModel =
                viewModel(
                    factory = BlockSetListScreenViewModelFactory(
                        LocalContext.current.dataStore,
                        LocalContext.current.settingsDataStore
                    )
                )
            BlockSetListScreen(
                viewModel,
                { navController.navigate("add_block_set") },
                { id -> navController.navigate("block_set_details/$id") },
                { navController.navigate("home") }
            )
        }

        composable("add_block_set") {
            val viewModel: AddBlockSetScreenViewModel =
                viewModel(factory = AddBlockSetScreenViewModelFactory(LocalContext.current.dataStore))
            AddBlockSetScreen(
                viewModel,
                { navController.navigate("block_set_list") },
                { navController.navigate("block_set_list") }
            )
        }

        composable(
            "block_set_details/{id}",
            listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val viewModel: BlockSetDetailsScreenViewModel = viewModel(
                factory = BlockSetDetailsScreenViewModelFactory(
                    LocalContext.current.dataStore,
                    LocalContext.current.settingsDataStore,
                    id
                )
            )
            BlockSetDetailsScreen(
                viewModel,
                id,
                { id -> navController.navigate("add_to_blocklist/$id") },
                {navController.navigate("block_set_list")}
            )
        }


        composable(
            "add_to_blocklist/{id}",
            listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val viewModel: AddAppToBlockListScreenViewModel =
                viewModel(
                    factory = AddAppToBlockListScreenViewModelFactory(
                        LocalContext.current.dataStore, id, appRepository
                    )
                )
            AddAppToBlocklistScreen(viewModel, id)
        }
    }
}
