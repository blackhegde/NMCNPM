package com.example.layout

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Khởi tạo FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Kết nối View từ giao diện
        val edtUser = findViewById<EditText>(R.id.edtUser)
        val edtPassWord = findViewById<EditText>(R.id.edtPassWord)
        val btSignin = findViewById<Button>(R.id.btSignin)
        val tvSwitchToRegister = findViewById<TextView>(R.id.tvSwitchToRegister)
        val ResetPassWord = findViewById<TextView>(R.id.ResetPassWord)

        // Xử lý nút Đăng nhập
        btSignin.setOnClickListener {
            val email = edtUser.text.toString().trim()
            val password = edtPassWord.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        // Chuyển sang màn hình Đăng ký
        tvSwitchToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện "Quên mật khẩu?"
        ResetPassWord.setOnClickListener {
            showResetPasswordDialog()
        }
    }

    private fun loginUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if (user != null && user.isEmailVerified) {
                        // Đăng nhập thành công
                        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Email chưa được xác thực
                        Toast.makeText(
                            this,
                            "Vui lòng xác thực email trước khi đăng nhập!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    // Đăng nhập thất bại
                    val errorMessage = task.exception?.message ?: "Đăng nhập thất bại!"
                    Toast.makeText(this, "Lỗi: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun showResetPasswordDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Quên mật khẩu")
        val input = EditText(this)
        input.hint = "Nhập email của bạn"
        builder.setView(input)

        builder.setPositiveButton("Gửi") { _, _ ->
            val email = input.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show()
            } else {
                sendResetPasswordEmail(email)
            }
        }

        builder.setNegativeButton("Hủy") { dialog, _ ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun sendResetPasswordEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Đã gửi email đặt lại mật khẩu. Vui lòng kiểm tra email của bạn.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val errorMessage = task.exception?.message ?: "Lỗi không xác định."
                    Toast.makeText(this, "Lỗi: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
