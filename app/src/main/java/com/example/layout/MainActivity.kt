package com.example.layout

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
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
