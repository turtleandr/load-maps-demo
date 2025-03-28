package dev.bokov.maplistdemo.domain.repository

import dev.bokov.maplistdemo.domain.model.DeviceMap
import dev.bokov.maplistdemo.domain.model.Device

internal interface DeviceRepository {
    suspend fun getDevices(): List<Device>
    suspend fun getMaps(deviceId: String): List<DeviceMap>
    suspend fun getMapImage(mapId: String): String

}
