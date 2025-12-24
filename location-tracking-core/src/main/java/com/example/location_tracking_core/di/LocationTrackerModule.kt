package com.example.location_tracking_core.di

import android.content.Context
import androidx.room.Room
import com.example.location_tracking_core.data.local.LocationDao
import com.example.location_tracking_core.data.local.LocationDatabase
import com.example.location_tracking_core.data.remote.LocationClient
import com.example.location_tracking_core.data.repositoryImpl.LocationRepositoryNetwork
import com.example.location_tracking_core.domain.repository.LocationRepository
import com.example.location_tracking_core.manager.LocationTrackingManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocationTrackerModule {
    @Provides
    @Singleton
    fun provideLocationTrackingManager(
        repository: LocationRepository,
        @ApplicationContext context: Context
    ): LocationTrackingManager {
        return LocationTrackingManager(repository, context)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        dao: LocationDao,
        client: LocationClient
    ): LocationRepository {
        return LocationRepositoryNetwork(dao, client)
    }

    @Provides
    @Singleton
    fun provideLocationClient(retrofit: Retrofit): LocationClient {
        return retrofit.create(LocationClient::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationDao(db: LocationDatabase): LocationDao {
        return db.getLocationDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LocationDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = LocationDatabase::class.java,
            name = "location_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext appContext: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(appContext)
    }
}