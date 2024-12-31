package com.example.layout.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Activity(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("type") val type: String,
    @SerializedName("distance") val distance: Float,
    @SerializedName("duration") val duration: Int, // in seconds
    @SerializedName("average_speed") val averageSpeed: Float?,
    @SerializedName("gps_data") val gpsData: Map<String, Any>?,
    @SerializedName("start_time") val startTime: Date,
    @SerializedName("end_time") val endTime: Date
)