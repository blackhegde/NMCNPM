package model

import com.google.android.gms.common.util.Strings

data class User (
    val id : Int,
    val username : String,
    val email : Strings,
    val avartar_type : Int
)