package model

data class GpsPoint (
    val latitude : Double,
    val longtitude : Double,
    val timestamp : Long
)

data class GpsData(
    val points : List<GpsPoint>
)