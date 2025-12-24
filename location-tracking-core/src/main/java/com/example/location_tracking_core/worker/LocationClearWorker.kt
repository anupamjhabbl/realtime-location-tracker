package com.example.location_tracking_core.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.location_tracking_core.domain.repository.LocationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

@HiltWorker
class LocationClearWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val locationRepository: LocationRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            locationRepository.deleteOldByStatus()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val TASK_TIMER_MINUTES = 1L
        const val TASK_IDENTIFIER = "location_sync_worker"

        fun initWorker(context: Context) {
            val workManagerBuilder = PeriodicWorkRequestBuilder<LocationClearWorker>(
                repeatInterval = TASK_TIMER_MINUTES,
                repeatIntervalTimeUnit = TimeUnit.HOURS
            ).setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true)
                    .build()
            )

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                uniqueWorkName = TASK_IDENTIFIER,
                existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP,
                request = workManagerBuilder.build()
            )
        }
    }
}