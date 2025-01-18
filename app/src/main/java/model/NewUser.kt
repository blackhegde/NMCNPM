package model

import com.google.android.gms.common.util.Strings

data class NewUser (
    val username : String,
    val email : Strings,
    val avartar_type : Int
)