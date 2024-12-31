package com.example.layout.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Streak(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("start_date") val startDate: Date,
    @SerializedName("end_date") val endDate: Date?,
    @SerializedName("is_active") val isActive: Boolean,
    @SerializedName("streak_days") val streakDays: Int
)
