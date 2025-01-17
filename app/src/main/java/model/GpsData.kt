package model

data class GpsPoint (
    val latitude : Double,
    val longtitude : Double
)

data class GpsData(
    val points : List<GpsPoint>
)