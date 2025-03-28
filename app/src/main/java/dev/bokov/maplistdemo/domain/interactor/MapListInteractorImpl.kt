package dev.bokov.maplistdemo.domain.interactor

import dev.bokov.maplistdemo.di.IoDispatcher
import dev.bokov.maplistdemo.domain.repository.DeviceRepository
import dev.bokov.maplistdemo.presentation.model.MapItemUi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MapListInteractorImpl @Inject constructor(
    private val repository: DeviceRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : MapListInteractor {

    override suspend fun getMapItemList(): List<MapItemUi> = withContext(ioDispatcher) {
        supervisorScope {
            val result = mutableListOf<MapItemUi>()

            try {
                val devices = repository.getDevices()

                val deferredLists = devices.map { device ->
                    async {
                        try {
                            val maps = repository.getMaps(device.id)

                            maps.map { map ->
                                val imageUrl = try {
                                    repository.getMapImage(map.id)
                                } catch (e: Exception) {
                                    null // если картинка не загрузилась — отобразим без неё
                                }

                                MapItemUi(
                                    mapId = map.id,
                                    mapName = map.name,
                                    deviceName = device.name,
                                    imageUrl = imageUrl
                                )
                            }

                        } catch (e: Exception) {
                            // если не удалось загрузить карты — ничего не возвращаем от этого устройства
                            emptyList()
                        }
                    }
                }
                result.addAll(deferredLists.flatMap { it.await() })
            } catch (e: Exception) {
                // ошибка загрузки девайсов — критическая, вернём пустой список
                return@supervisorScope emptyList()
            }
            return@supervisorScope result
        }
    }
}