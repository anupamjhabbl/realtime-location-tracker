package com.example.android_template_project.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.location_tracking_core.manager.LocationTrackingManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationTrackingViewModel @Inject constructor(
    val locationTrackingManager: LocationTrackingManager
) : ViewModel() {
    private val _pendingLogs = MutableStateFlow(0L)
    val pendingLogs: StateFlow<Long> = _pendingLogs.asStateFlow()

    init {
        viewModelScope.launch {
            locationTrackingManager.getPendingOfflineLogs().collect {
                _pendingLogs.value = it
            }
        }
    }

    fun startTracking() {
        locationTrackingManager.startTracking()
    }

    fun stopTracking() {
        locationTrackingManager.stopTracking()
    }
}
