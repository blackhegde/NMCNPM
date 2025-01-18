package model

import com.google.gson.Gson
import org.osmdroid.util.GeoPoint

//data class GpsPoint (
//    val latitude : Double,
//    val longitude : Double
//)

data class GpsData(
    val points: MutableList<GeoPoint>
) {
    // Chuyển đổi GpsData thành JSON
    fun toJson(): String {
        return Gson().toJson(this)
    }

    // Chuyển đổi JSON thành GpsData
    companion object {
        fun fromJson(json: String): GpsData {
            return Gson().fromJson(json, GpsData::class.java)
        }
    }
}