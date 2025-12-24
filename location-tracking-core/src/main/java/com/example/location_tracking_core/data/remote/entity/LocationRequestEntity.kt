package com.example.location_tracking_core.data.remote.entity

import com.example.location_tracking_core.domain.model.LocationData
import kotlinx.serialization.Serializable

@Serializable
data class LocationRequestEntity(
    val locationList: List<LocationData>
)