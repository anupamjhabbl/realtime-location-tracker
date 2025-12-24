package com.example.location_tracking_core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.location_tracking_core.data.local.entity.LocationEntity

@Database(
    entities = [LocationEntity::class],
    version = 1
)
abstract class LocationDatabase: RoomDatabase() {
    abstract fun getLocationDao(): LocationDao
}