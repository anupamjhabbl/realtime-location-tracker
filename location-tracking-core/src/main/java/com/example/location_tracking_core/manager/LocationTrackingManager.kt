package com.example.location_tracking_core.manager

import android.content.Context
import android.content.Intent
import com.example.location_tracking_core.domain.repository.LocationRepository
import com.example.location_tracking_core.service.LocationCollectorService
import com.example.location_tracking_core.service.LocationSyncService
import com.example.location_tracking_core.worker.LocationClearWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow

class LocationTrackingManager(
    private val locationRepository: LocationRepository,
    @ApplicationContext private val appContext: Context
) {
    fun startTracking() {
        if (!(LocationCollectorService.hasLocationPermission(appContext) && LocationCollectorService.hasNotificationPermission(appContext))) {
            return
        }
        val intent = Intent(appContext, LocationCollectorService::class.java)
            .apply {
                putExtra( LocationCollectorService.LOCATION_TRACKING_ACTION, LocationCollectorService.START_LOCATION_TRACKING )
            }
        appContext.startForegroundService(intent)
    }

    fun stopTracking() {
        val intent = Intent(appContext, LocationCollectorService::class.java)
            .apply {
                putExtra( LocationCollectorService.LOCATION_TRACKING_ACTION, LocationCollectorService.STOP_LOCATION_TRACKING )
            }
        appContext.startService(intent)
    }

    fun getPendingOfflineLogs(): Flow<Long> {
        return locationRepository.getPendingOfflineLogs()
    }

    fun startSync() {
        LocationClearWorker.initWorker(appContext)
    }

    fun startCleanUpWorker() {
        if (LocationCollectorService.hasNotificationPermission(appContext)) {
            val intent = Intent(appContext, LocationSyncService::class.java)
            appContext.startService(intent)
        }
    }
}