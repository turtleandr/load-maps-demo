package dev.bokov.maplistdemo.presentation.state

import dev.bokov.maplistdemo.presentation.model.MapItemUi

sealed class MapListUiState {
    data object Loading : MapListUiState()
    data class Success(val items: List<MapItemUi>) : MapListUiState()
    data class Error(val message: String? = null) : MapListUiState()
}