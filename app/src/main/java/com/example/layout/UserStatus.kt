package com.example.layout

import org.osmdroid.util.Distance

data class UserStatus (
    val avatarResId : Int,
    val name : String,
    val address: String,
    val distance: String,
    val pace: String,
    val time: String,
    val mapResId: Int
)