package com.example.location_tracking_core.data.remote

import com.example.location_tracking_core.data.remote.entity.LocationRequestEntity
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationClient {
    @POST("post")
    suspend fun syncDataToRemote(@Body locationRequest: LocationRequestEntity)
}