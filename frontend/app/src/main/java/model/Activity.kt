package model

data class Activity(
    val id: Int,
    val user_id: Int,
    val type: Int,
    val distance: Float,
    val duration: Int, //in second
    val average_speed: Float,
    val gps_data: String,
    val start_time: String,
    val end_time: String
    )