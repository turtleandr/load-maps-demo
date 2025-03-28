package dev.bokov.maplistdemo.domain.interactor

import dev.bokov.maplistdemo.presentation.model.MapItemUi


interface MapListInteractor {
    suspend fun getMapItemList(): List<MapItemUi>
}