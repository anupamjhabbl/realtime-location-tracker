package com.example.location_tracking_core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.location_tracking_core.domain.repository.LocationRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationSyncService : Service() {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var networkMonitor: NetworkMonitor
    @Inject
    lateinit var locationRepository: LocationRepository
    private var syncJob: Job? = null

    override fun onCreate() {
        super.onCreate()
        networkMonitor = NetworkMonitor(this)
        networkMonitor.start()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createNotification())

        scope.launch {
            networkMonitor.isOnline.collect { isOnline ->
                if (isOnline) {
                    startSyncLoop()
                } else {
                    stopSyncLoop()
                }
            }
        }
        return START_STICKY
    }

    private fun startSyncLoop() {
        if (syncJob != null) return

        syncJob = scope.launch {
            while (isActive) {
                try {
                    doWork()
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {}
                delay(1000 * 60 * 2)
            }
        }
    }

    private fun stopSyncLoop() {
        syncJob?.cancel()
        syncJob = null
    }

    private suspend fun doWork() {
        var pendingLocations = locationRepository.getPendingLocations()
        while (pendingLocations.isNotEmpty()) {
            locationRepository.syncDataToRemote(pendingLocations)
            val ids = pendingLocations.map { it.id }
            locationRepository.markAsSynced(ids)
            pendingLocations = locationRepository.getPendingLocations()
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Location syncing active")
            .setContentText("Your location is being synced")
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Shows notification while location is synced"
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        networkMonitor.stop()
        scope.cancel()
        super.onDestroy()
    }

    companion object {
        const val CHANNEL_ID = "location_syncing_channel"
        const val CHANNEL_NAME = "Location Syncer"
        const val NOTIFICATION_ID = 1002
    }
}
