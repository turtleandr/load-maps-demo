package dev.bokov.maplistdemo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.bokov.maplistdemo.data.MockDeviceRepositoryImpl
import dev.bokov.maplistdemo.domain.interactor.MapListInteractor
import dev.bokov.maplistdemo.domain.interactor.MapListInteractorImpl
import dev.bokov.maplistdemo.domain.repository.DeviceRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindDevicesRepository(
        impl: MockDeviceRepositoryImpl
    ): DeviceRepository

    @Binds
    @Singleton
    abstract fun bindDevicesInteractor(
        impl: MapListInteractorImpl
    ): MapListInteractor
}
