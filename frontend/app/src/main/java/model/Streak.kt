package model

import java.util.Date

data class Streak (
    val id : Int,
    val user_id : Int,
    val start_date : Date,
    val end_date : Date?,
    val is_active : Boolean,
    val count : Int
) {
}