package com.example.location_tracking_core.data.mapper

import com.example.location_tracking_core.data.local.entity.LocationEntity
import com.example.location_tracking_core.data.local.entity.SyncStatus
import com.example.location_tracking_core.domain.model.LocationData

object LocationMapper {
    fun LocationEntity.locationEntityToDto(): LocationData {
        return LocationData(
            id = locationEntryId,
            employeeId = employeeId,
            latitude = latitude,
            longitude = longitude,
            accuracy = accuracy,
            timestamp = timestamp,
            speed = speed
        )
    }

    fun LocationData.locationDaoToEntity(syncStatus: SyncStatus = SyncStatus.PENDING): LocationEntity {
        return LocationEntity(
            employeeId = employeeId,
            latitude = latitude,
            longitude = longitude,
            accuracy = accuracy,
            timestamp = timestamp,
            speed = speed,
            syncStatus = syncStatus,
        )
    }
}