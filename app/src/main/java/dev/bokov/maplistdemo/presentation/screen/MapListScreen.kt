package dev.bokov.maplistdemo.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import dev.bokov.maplistdemo.presentation.MapCard
import dev.bokov.maplistdemo.presentation.state.MapListUiState
import dev.bokov.maplistdemo.presentation.viewmodel.MapListViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MapListScreen(
    modifier: Modifier = Modifier,
    viewModel: MapListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    // Флаг для отслеживания первой загрузки
    var isInitialLoad by remember { mutableStateOf(true) }

    // Обновляем флаг после первой загрузки
    when (uiState) {
        is MapListUiState.Success -> isInitialLoad = false
        is MapListUiState.Error -> isInitialLoad = false
        else -> {} // Loading оставляет флаг без изменений
    }

    val isRefreshing = uiState is MapListUiState.Loading && !isInitialLoad
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.loadMapItems() }
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        when (uiState) {
            is MapListUiState.Loading -> {
                if (isInitialLoad) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is MapListUiState.Error -> {
                val message = (uiState as MapListUiState.Error).message ?: "Ошибка загрузки данных"
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadMapItems() }) {
                            Text("Попробовать снова")
                        }
                    }
                }
            }

            is MapListUiState.Success -> {
                val maps = (uiState as MapListUiState.Success).items

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(maps, key = { it.mapId }) { map ->
                        MapCard(
                            title = map.mapName,
                            deviceName = map.deviceName,
                            iconUrl = map.imageUrl
                        )
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
