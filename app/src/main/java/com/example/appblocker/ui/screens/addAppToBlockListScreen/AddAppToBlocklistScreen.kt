package com.example.appblocker.ui.screens.addAppToBlockListScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appblocker.ui.theme.Surface
import kotlinx.coroutines.launch

@Composable
fun AddAppToBlocklistScreen(viewModel: AddAppToBlockListScreenViewModel, blockSetId: Int) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(state.apps) {
        focusRequester.requestFocus()
    }

    Scaffold() { padding ->
        Box(Modifier.zIndex(1f)) {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
            ) { data ->
                Snackbar(
                    containerColor = Color(0xFF2E7D32),
                    contentColor = Color.White,
                    actionColor = Color.Yellow,
                    snackbarData = data
                )
            }
        }

        val blockSet = state.blockSet
        if (blockSet == null) {
            Text("Error, unable to load block set for id $blockSetId")
        } else {
            if (state.apps.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            } else {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    Text(
                        "Add to block set ${blockSet.name}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    OutlinedTextField(
                        state.searchTerm, { searchTerm ->
                            viewModel.updateSearchTerm(searchTerm)
                        },
                        placeholder = { Text("Search") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp, end = 4.dp)
                            .focusRequester(focusRequester)
                    )

                    Spacer(
                        Modifier
                            .height(12.dp)
                            .border(2.dp, Color.Green)
                    )
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),

                        ) {
                        items(state.filteredApps.size) { index ->
                            val appName = state.filteredApps[index].appName
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                                    .clickable(onClick = {
                                        viewModel.addAppPackageToBlockList(
                                            appName,
                                            state.filteredApps[index].appPackageName
                                        )
                                        scope.launch {
                                            snackbarHostState.currentSnackbarData?.dismiss()
                                            snackbarHostState.showSnackbar(message = "$appName added to block list!")
                                        }
                                    }),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                )

                                Spacer(Modifier.width(12.dp))

                                Text(
                                    text = appName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            /*Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable(onClick = {
                                        viewModel.addAppPackageToBlockList(
                                            appName,
                                            state.filteredApps[index].appPackageName
                                        )
                                        scope.launch {
                                            snackbarHostState.currentSnackbarData?.dismiss()
                                            snackbarHostState.showSnackbar(message = "$appName added to block list!")
                                        }
                                    })
                            ) {
                                Text(
                                    appName,
                                    fontSize = 32.sp,
                                    lineHeight = 32.sp,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                            Spacer(Modifier.height(4.dp))
                            HorizontalDivider(color = Color.Gray, thickness = 1.dp)*/
                        }
                    }
                }
            }
        }

    }

    Spacer(Modifier.height(10.dp))
}


