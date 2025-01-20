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
import model.Streak
import model.User
import network.ApiClient.apiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                        //GET các thứ các thứ
                        getUserInfo(email)
                        getStreak()
                        //Thông báo đăng nhập thành công và chuyển sang màn hình main
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

    private fun getUserInfo(email: String) {
        // Lấy ID từ SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Gửi yêu cầu đến server để lấy thông tin người dùng
        apiService.getUser(email).enqueue(object : Callback<User> {
            override fun onResponse(call: retrofit2.Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        val username = user.username
                        val avatarType = user.avatar_type
                        val userId = user.id

                        // Lưu thông tin vào SharedPreferences
                        sharedPreferences.edit().apply {
                            putString("USERNAME", username)
                            putInt("AVATAR_TYPE", avatarType)
                            putInt("USER_ID", userId)
                            apply()
                        }

                        // Hiển thị thông báo chào mừng
                        Toast.makeText(
                            this@LoginActivity,
                            "Xin chào, $username! Avatar Type: $avatarType",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Không thể lấy thông tin người dùng.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Lỗi khi lấy thông tin người dùng: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<User>, t: Throwable) {
                Toast.makeText(
                    this@LoginActivity,
                    "Lỗi kết nối đến server: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    //cập nhật streak
    private fun getStreak() {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("USER_ID", -1)

        // Kiểm tra nếu userId không hợp lệ
        if (userId == -1) {
            return // Dừng thực hiện nếu không tìm thấy userId
        }

        // Gửi yêu cầu lấy streak từ server
        apiService.getStreak(userId).enqueue(object : Callback<Streak> {
            override fun onResponse(call: Call<Streak>, response: Response<Streak>) {
                if (response.isSuccessful) {
                    val streak = response.body()
                    val count = streak?.count ?: 0 // Sử dụng giá trị mặc định nếu count là null

                    // Lưu thông tin streak vào SharedPreferences
                    sharedPreferences.edit().apply {
                        putInt("STREAK_COUNT", count)
                        apply()
                    }
                }
            }

            override fun onFailure(call: Call<Streak>, t: Throwable) {
                // Không cần xử lý thêm logic, có thể ghi log nếu cần
            }
        })
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
