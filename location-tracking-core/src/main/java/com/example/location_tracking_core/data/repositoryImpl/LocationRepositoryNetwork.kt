package com.example.location_tracking_core.data.repositoryImpl

import com.example.location_tracking_core.data.local.LocationDao
import com.example.location_tracking_core.data.mapper.LocationMapper.locationDaoToEntity
import com.example.location_tracking_core.data.mapper.LocationMapper.locationEntityToDto
import com.example.location_tracking_core.data.remote.LocationClient
import com.example.location_tracking_core.data.remote.entity.LocationRequestEntity
import com.example.location_tracking_core.domain.model.LocationData
import com.example.location_tracking_core.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationRepositoryNetwork(
    val locationDao: LocationDao,
    val locationClient: LocationClient
): LocationRepository {
    companion object {
        const val PENDING_LOCATION_LIMIT = 50
    }

    override suspend fun saveLocation(location: LocationData) {
        locationDao.saveNewLocationEntry(location.locationDaoToEntity())
    }

    override suspend fun getPendingLocations(): List<LocationData> {
        return locationDao.getPendingLocations(
            PENDING_LOCATION_LIMIT
        ).map { it.locationEntityToDto() }
    }

    override suspend fun markAsSynced(ids: List<Long>) {
       locationDao.updateSyncStatus(ids)
    }

    override suspend fun deleteOldByStatus() {
        locationDao.deleteOldByStatus()
    }

    override suspend fun syncDataToRemote(locationList: List<LocationData>) {
        locationClient.syncDataToRemote(
            LocationRequestEntity(locationList)
        )
    }

    override fun getPendingOfflineLogs(): Flow<Long> {
        return locationDao.getPendingOfflineLogs()
    }
}