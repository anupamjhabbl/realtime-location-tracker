package com.example.location_tracking_core.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LocationData(
    val id: Long = 0,
    val employeeId: String,
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float,
    val timestamp: Long,
    val speed: Float
)