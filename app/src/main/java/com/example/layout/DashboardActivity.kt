package com.example.layout

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import model.Activity
import model.GpsData
import network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import java.util.Date

@Suppress("DEPRECATION")
class DashboardActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
    private lateinit var tvTimer: TextView
    private lateinit var tvMode: TextView
    private lateinit var btnStop: Button

    private var seconds = 0
    private var running = true
    private var totalDistance = 0.0 // Tổng quãng đường đã chạy (km)
    private var lastLocation: GeoPoint? = null // Lưu vị trí cuối cùng
    private val points: MutableList<GeoPoint> = mutableListOf() // Lưu danh sách các điểm

    private var currentMarker: Marker? = null // Quản lý Marker hiện tại

    // FusedLocationProviderClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var polyline: Polyline

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cấu hình osmdroid
        Configuration.getInstance().load(applicationContext, applicationContext.getSharedPreferences("osmdroid", MODE_PRIVATE))
        setContentView(R.layout.dashboard_activity)

        // Kết nối các View
        mapView = findViewById(R.id.mapView)
        tvTimer = findViewById(R.id.tvTimer)
        tvMode = findViewById(R.id.tvMode)
        btnStop = findViewById(R.id.btnStop)

        // Hiển thị chế độ đã chọn
        val mode = intent.getStringExtra("mode") ?: "Không rõ"
        tvMode.text = "Chế độ: $mode"

        // Cấu hình MapView
        mapView.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Tạo Polyline cho đường đi
        polyline = Polyline()
        polyline.color = android.graphics.Color.BLUE
        polyline.width = 5.0f
        mapView.overlays.add(polyline)

        // Bắt đầu đếm thời gian
        runTimer()
        // Khởi tạo thêm hàm postActivityToServer
        fun postActivityToServer(activity: Activity) {
            val apiService = ApiClient.apiService // Khởi tạo ApiService từ ApiClient

            apiService.postActivities(activity).enqueue(object : Callback<Activity> {
                override fun onResponse(call: Call<Activity>, response: Response<Activity>) {
                    if (response.isSuccessful) {
                        val syncedActivity = response.body()
                        Toast.makeText(
                            this@DashboardActivity,
                            "Hoạt động đã được đồng bộ: ID = ${syncedActivity?.id}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.e("Sync", "Lỗi đồng bộ Activity: ${response.code()}")
                        Toast.makeText(
                            this@DashboardActivity,
                            "Lỗi đồng bộ hoạt động: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Activity>, t: Throwable) {
                    Log.e("Sync", "Lỗi kết nối khi đồng bộ Activity: ${t.message}")
                    Toast.makeText(
                        this@DashboardActivity,
                        "Lỗi kết nối đến server.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        // Dừng chạy khi nhấn nút Stop
        btnStop.setOnClickListener {
            running = false
            // Tạo biến Activity từ dữ liệu hiện tại
            val gpsData = GpsData(points)
            val activity = Activity(
                id = 0, // Server sẽ tự tạo ID
                user_id = 1, // ID người dùng hiện tại, bạn cần lấy từ phiên đăng nhập hoặc một nguồn khác
                type = 1, // Loại hoạt động, giả sử 1 là chạy bộ
                distance = totalDistance.toFloat(),
                duration = seconds,
                average_speed = (totalDistance / (seconds / 3600.0)).toFloat(),
                gps_data = gpsData.toJson(),
                start_time = Date(), // startTime cần được lưu từ khi bắt đầu hoạt động
                end_time = Date() // Thời gian hiện tại
            )

            // Gọi hàm lưu hoạt động lên server
            postActivityToServer(activity)
            showSummaryPopup()

        }

        // Khởi tạo FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Cấu hình yêu cầu vị trí
        locationRequest = LocationRequest.create().apply {
            interval = 3000 // 5 giây
            fastestInterval = 1000 // 2 giây
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // Kiểm tra quyền và bắt đầu theo dõi vị trí
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            startTrackingLocation()
        }
    }

    private fun startTrackingLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val newLocation = locationResult.lastLocation
            if (newLocation != null) {
                val geoPoint = GeoPoint(newLocation.latitude, newLocation.longitude)

                // Cập nhật Marker và đường đi
                updateMarker(geoPoint)
                if (lastLocation == null) {
                    lastLocation = geoPoint
                    points.add(geoPoint)
                } else {
                    updatePath(geoPoint)
                }
            } else {
                Toast.makeText(this@DashboardActivity, "Không thể lấy vị trí GPS", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun runTimer() {
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                val secs = seconds % 60

                tvTimer.text = String.format("Thời gian: %02d:%02d:%02d", hours, minutes, secs)

                if (running) {
                    seconds++
                }
                handler.postDelayed(this, 1000)
            }
        })
    }

    private fun updatePath(newPoint: GeoPoint) {
        points.add(newPoint)
        lastLocation?.let { last ->
            val distance = haversine(last.latitude, last.longitude, newPoint.latitude, newPoint.longitude)
            totalDistance += distance
        }
        polyline.setPoints(points)
        mapView.invalidate()
        lastLocation = newPoint
    }

    private fun updateMarker(location: GeoPoint) {
        if (currentMarker == null) {
            currentMarker = Marker(mapView).apply {
                position = location
                title = "Vị trí hiện tại"
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                mapView.overlays.add(this)
            }
        } else {
            currentMarker?.position = location
        }
        mapView.controller.setCenter(location)
        mapView.invalidate()
    }

    private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(kotlin.math.sqrt(a), kotlin.math.sqrt(1 - a))
        return R * c
    }

    private fun showSummaryPopup() {
        val avgSpeed = if (seconds > 0) totalDistance / (seconds / 3600.0) else 0.0
        AlertDialog.Builder(this)
            .setTitle("Kết quả")
            .setMessage(
                String.format(
                    "Thời gian chạy: %02d:%02d:%02d\nQuãng đường: %.2f km\nTốc độ trung bình: %.2f km/h",
                    seconds / 3600, (seconds % 3600) / 60, seconds % 60,
                    totalDistance, avgSpeed
                )
            )
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .create()
            .show()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}
