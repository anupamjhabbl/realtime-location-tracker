package com.example.location_tracking_core.domain.repository

import com.example.location_tracking_core.domain.model.LocationData
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun saveLocation(location: LocationData)

    suspend fun getPendingLocations(): List<LocationData>

    suspend fun markAsSynced(ids: List<Long>)

    suspend fun deleteOldByStatus()

    suspend fun syncDataToRemote(locationList: List<LocationData>)

    fun getPendingOfflineLogs(): Flow<Long>
}