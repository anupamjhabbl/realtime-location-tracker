package com.example.location_tracking_core.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.location_tracking_core.data.local.entity.LocationEntity
import com.example.location_tracking_core.data.local.entity.SyncStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveNewLocationEntry(location: LocationEntity)

    @Query("""
        SELECT * FROM locationEntity
        WHERE syncStatus = :status
        ORDER BY timestamp ASC
        LIMIT :limit
    """)
    suspend fun getPendingLocations(
        limit: Int,
        status: SyncStatus = SyncStatus.PENDING
    ): List<LocationEntity>

    @Query("""
        UPDATE locationEntity
        SET syncStatus = :newStatus
        WHERE locationEntryId IN (:ids)
    """)
    suspend fun updateSyncStatus(
        ids: List<Long>,
        newStatus: SyncStatus = SyncStatus.SYNCED
    )

    @Query("""
        DELETE FROM locationEntity
        WHERE syncStatus = :status
    """)
    suspend fun deleteOldByStatus(
        status: SyncStatus = SyncStatus.SYNCED
    )

    @Query(
        """
        SELECT COUNT(*) FROM locationEntity
        WHERE syncStatus = :syncStatus
    """)
    fun getPendingOfflineLogs(
        syncStatus: SyncStatus = SyncStatus.PENDING
    ): Flow<Long>
}