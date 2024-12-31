package com.example.layout.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class Achievement(
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("achieved_on") val achievedOn: Date,
    @SerializedName("description") val description: String?
)