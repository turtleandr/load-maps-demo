package dev.bokov.maplistdemo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.bokov.maplistdemo.domain.interactor.MapListInteractor
import dev.bokov.maplistdemo.presentation.model.MapItemUi
import dev.bokov.maplistdemo.presentation.state.MapListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapListViewModel @Inject constructor(
    private val interactor: MapListInteractor
) : ViewModel() {

    private val _uiState = MutableStateFlow<MapListUiState>(MapListUiState.Loading)
    val uiState: StateFlow<MapListUiState> = _uiState

    init {
        loadMapItems()
    }

    fun loadMapItems() {
        viewModelScope.launch {
            _uiState.value = MapListUiState.Loading
            val items = interactor.getMapItemList()
            _uiState.value = if (items.isEmpty()) {
                MapListUiState.Error("Не удалось загрузить карты")
            } else {
                MapListUiState.Success(items)
            }
        }
    }
}