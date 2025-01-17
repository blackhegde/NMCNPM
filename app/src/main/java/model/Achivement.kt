package model

import java.util.Date

data class Achievement (
    val id : Int,
    val user_id : Int,
    val name : String,
    val achieved_on: Date,
    val description: String
) {
}