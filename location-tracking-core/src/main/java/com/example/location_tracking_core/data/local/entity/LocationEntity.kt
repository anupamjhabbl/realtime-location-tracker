package com.example.location_tracking_core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "locationEntity"
)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val locationEntryId: Long = 0,
    val employeeId: String,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long,
    val speed: Float,
    val syncStatus: SyncStatus
)

enum class SyncStatus {
    PENDING, SYNCED
}
