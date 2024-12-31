package com.example.layout.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatar_type") val avatarType: Int,
    @SerializedName("activities") val activities: List<Activity>?,
    @SerializedName("streak") val streak: Streak?,
    @SerializedName("achievements") val achievements: List<Achievement>?
)




