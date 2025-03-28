package dev.bokov.maplistdemo.data

import dev.bokov.maplistdemo.domain.repository.DeviceRepository
import dev.bokov.maplistdemo.domain.model.DeviceMap
import dev.bokov.maplistdemo.domain.model.Device
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.random.Random

internal class MockDeviceRepositoryImpl @Inject constructor() : DeviceRepository {

    // private val mockDevices = emptyList<Device>()
    private val mockDevices = listOf(
        Device(id = "device_1", name = "Device1"),
        Device(id = "device_2", name = "Device2")
    )

    private val mockMaps = mapOf(
        "device_1" to listOf(
            DeviceMap(id = "map_1_dev1", name = "Kitchen Map"),
            DeviceMap(id = "map_2_dev1", name = "Bedroom Map")
        ),
        "device_2" to listOf(
            DeviceMap(id = "map_1_dev2", name = "Office Map"),
            DeviceMap(id = "map_2_dev2", name = "Lobby Map"),
            DeviceMap(id = "map_3_dev2", name = "Basement Map")
        )
    )

    private val mockMapImages = mapOf(
        "map_1_dev1" to "https://picsum.photos/200/300",
        "map_2_dev1" to "https://picsum.photos/200/300",
        "map_1_dev2" to "https://picsum.photos/200/300",
        "map_2_dev2" to "https://picsum.photos/200/300",
        "map_3_dev2" to "https://picsum.photos/200/300"
    )

    override suspend fun getDevices(): List<Device> {
        simulateRandomFailure("getDevices")
        delay(300)
        return mockDevices
    }

    override suspend fun getMaps(deviceId: String): List<DeviceMap> {
        simulateRandomFailure("getMaps")
        delay(300)
        return mockMaps[deviceId].orEmpty()
    }

    override suspend fun getMapImage(mapId: String): String {
        simulateRandomFailure("getMapImage")
        delay(300)
        return mockMapImages[mapId] ?: "https://example.com/images/default.png" // FIXME:
    }

    private fun simulateRandomFailure(methodName: String) {
        val shouldFail = Random.nextFloat() < 0.25f // 25% шанс ошибки
        if (shouldFail) {
            throw RuntimeException("Simulated failure in $methodName")
        }
    }
}