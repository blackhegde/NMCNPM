package com.example.layout

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
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

            // Hiển thị email của người dùng (nếu có)

            // Xử lý nút Đăng xuất
            val btnLogout = findViewById<Button>(R.id.btnLogout)
            btnLogout.setOnClickListener {
                firebaseAuth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Đóng MainActivity
            }
        }
    }
}
