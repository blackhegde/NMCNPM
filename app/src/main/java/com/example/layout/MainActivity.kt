package com.example.layout

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Kiểm tra trạng thái đăng nhập
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            // Nếu chưa đăng nhập, chuyển đến LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Đóng MainActivity
        } else {
            // Nếu đã đăng nhập, hiển thị giao diện chính
            setContentView(R.layout.activity_main)

            // Xử lý nút Đăng xuất
            val btnLogout = findViewById<Button>(R.id.btnLogout)
            btnLogout.setOnClickListener {
                firebaseAuth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Đóng MainActivity
            }

            // Xử lý sự kiện nhấn nút Start
            val btnStart = findViewById<ImageButton>(R.id.btnStart)
            btnStart.setOnClickListener {
                showModeSelectionDialog()
            }

            val btnRank: ImageButton = findViewById(R.id.btnRank)

            btnRank.setOnClickListener {
                val intent = Intent(this, LeaderboardActivity::class.java)
                startActivity(intent)
            }

        }
        val userList = listOf(
            UserStatus(R.drawable.ic_launcher_foreground, "John Doe", "123 Main Street", "5 km", "6'00\"", "30 mins", R.drawable.ic_launcher_background),
            UserStatus(R.drawable.ic_launcher_foreground, "Jane Smith", "456 Elm Street", "8 km", "5'45\"", "45 mins", R.drawable.ic_launcher_background),
            UserStatus(R.drawable.ic_launcher_foreground, "Jane Phanh", "456 Elm Street", "8 km", "5'45\"", "45 mins", R.drawable.ic_launcher_background),
            UserStatus(R.drawable.ic_launcher_foreground, "Sơn Smith", "456 Elm Street", "8 km", "5'45\"", "45 mins", R.drawable.ic_launcher_background),
            UserStatus(R.drawable.ic_launcher_foreground, "Jane Hoa", "456 Elm Street", "8 km", "5'45\"", "45 mins", R.drawable.ic_launcher_background)
        )

        // Thiết lập RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView_runnerstatus)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MainAdapter(userList)

        val btnStatistics : ImageButton = findViewById(R.id.btnStatistic)

        btnStatistics.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showModeSelectionDialog() {
        // Inflate layout tùy chỉnh
        val dialogView = layoutInflater.inflate(R.layout.dialog_mode_selection, null)

        // Tạo AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

        // Kết nối các tùy chọn với sự kiện nhấn
        val optionRun = dialogView.findViewById<LinearLayout>(R.id.option_run)
        val optionBicycle = dialogView.findViewById<LinearLayout>(R.id.option_bicycle)
        val optionWalk = dialogView.findViewById<LinearLayout>(R.id.option_walk)

        optionRun.setOnClickListener {
            dialog.dismiss()
            startDashboard("Chạy Bộ")
        }

        optionBicycle.setOnClickListener {
            dialog.dismiss()
            startDashboard("Đạp Xe")
        }

        optionWalk.setOnClickListener {
            dialog.dismiss()
            startDashboard("Đi Bộ")
        }

        // Hiển thị Dialog
        dialog.show()
    }

    private fun startDashboard(mode: String) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("mode", mode) // Gửi chế độ đã chọn sang DashboardActivity
        startActivity(intent)
    }
}
