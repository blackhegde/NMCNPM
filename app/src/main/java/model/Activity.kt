package model

import java.util.Date

data class Activity(
    val id : Int,
    val user_id : Int,
    val type : Int,
    val distance : Float,
    val duration : Int, //in second
    val average_speed : Float,
    val gps_data : GpsData,
    val start_time : Date,
    val end_time : Date
    )