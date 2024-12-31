package com.example.layout

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.layout.model.User

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var leaderboardAdapter: LeaderboardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        // Dữ liệu mẫu
        val userList = listOf(
            User(1, R.drawable.ic_launcher_background, "Alice", "1500 pts"),
            User(2, R.drawable.ic_launcher_background, "Bob", "1400 pts"),
            User(3, R.drawable.ic_launcher_background, "Phanh", "1300 pts"),
            User(4, R.drawable.ic_launcher_background, "Son", "1300 pts"),
            User(5, R.drawable.ic_launcher_background, "KamTu", "1300 pts"),
            User(6, R.drawable.ic_launcher_background, "KyAnh", "1300 pts"),
            User(7, R.drawable.ic_launcher_background, "NMCNPM", "1300 pts"),
            User(8, R.drawable.ic_launcher_background, "Hoa", "1300 pts"),
            User(9, R.drawable.ic_launcher_background, "Hong", "1300 pts"),
            User(10, R.drawable.ic_launcher_background, "Charlie", "1300 pts"),
            User(11, R.drawable.ic_launcher_background, "Charlie", "1300 pts"),
            User(12, R.drawable.ic_launcher_background, "Charlie", "1300 pts"),
            User(13, R.drawable.ic_launcher_background, "Charlie", "1300 pts"),
            User(14, R.drawable.ic_launcher_background, "Charlie", "1300 pts"),
            User(15, R.drawable.ic_launcher_background, "Charlie", "1300 pts"),
            User(16, R.drawable.ic_launcher_background, "Charlie", "1300 pts")
        )

        // Kết nối RecyclerView
        recyclerView = findViewById(R.id.recyclerView_runners)
        leaderboardAdapter = LeaderboardAdapter(userList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = leaderboardAdapter

        val btnhome : ImageButton = findViewById(R.id.btnhome)

        btnhome.setOnClickListener{
            finish()
        }

        // Xử lý sự kiện nhấn nút Start
        val btnStart = findViewById<ImageButton>(R.id.btnStart)
        btnStart.setOnClickListener {
            showModeSelectionDialog()
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