package com.example.layout

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
        }
    }
    private fun showModeSelectionDialog() {
        // Hiển thị Popup để chọn chế độ
        val modes = arrayOf("Chạy Bộ", "Đạp Xe", "Đi Bộ")
        AlertDialog.Builder(this)
            .setTitle("Chọn chế độ")
            .setItems(modes) { _, which ->
                val selectedMode = modes[which]
                startDashboard(selectedMode)
            }
            .setNegativeButton("Hủy") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun startDashboard(selectedMode: String) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("mode", selectedMode) // Gửi chế độ đã chọn sang DashboardActivity
        startActivity(intent)
    }
}
