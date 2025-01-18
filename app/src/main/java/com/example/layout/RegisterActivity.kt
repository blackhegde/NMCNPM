package com.example.layout

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import model.NewUser // Đổi thành NewUser
import model.User
import network.ApiClient.apiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Khởi tạo FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Tạo SharedPreferences để lưu thông tin người dùng
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        // Kết nối View từ giao diện
        val edtUser = findViewById<EditText>(R.id.edtUser)
        val edtPassWord = findViewById<EditText>(R.id.edtPassWord)
        val btSignin = findViewById<Button>(R.id.btSignin)

        // Xử lý nút Đăng ký
        btSignin.setOnClickListener {
            val email = edtUser.text.toString().trim()
            val password = edtPassWord.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            } else {
                // Kiểm tra xem email có tồn tại hay không
                checkIfEmailExists(email, password, sharedPreferences)
            }
        }
    }

    private fun checkIfEmailExists(email: String, password: String, sharedPreferences: SharedPreferences) {
        firebaseAuth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods.isNullOrEmpty()) {
                        // Email không tồn tại, tiến hành đăng ký
                        registerUser(email, password, sharedPreferences)
                    } else {
                        // Email đã tồn tại
                        Toast.makeText(this, "Email đã được sử dụng!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Lỗi khi kiểm tra email
                    val errorMessage = task.exception?.message ?: "Lỗi không xác định."
                    Toast.makeText(this, "Lỗi: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun registerUser(email: String, password: String, sharedPreferences: SharedPreferences) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Gửi email xác thực
                    val user = firebaseAuth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { emailTask ->
                            if (emailTask.isSuccessful) {
                                // Tạo đối tượng NewUser và gửi lên server
                                val newUser = NewUser(username = email, email = email, avatar_type = 1)
                                apiService.postUser(newUser).enqueue(object : Callback<User> {
                                    override fun onResponse(call: Call<User>, response: Response<User>) {
                                        if (response.isSuccessful) {
                                            val user = response.body()
                                            if (user != null) {
                                                // Lưu ID vào SharedPreferences
                                                sharedPreferences.edit().putInt("USER_ID", user.id).apply()
                                                Toast.makeText(
                                                    this@RegisterActivity,
                                                    "Đăng ký thành công! Kiểm tra email để xác thực tài khoản.",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                // Chuyển về LoginActivity
                                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            }
                                        } else {
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                "Đăng ký thất bại trên server!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<User>, t: Throwable) {
                                        Toast.makeText(
                                            this@RegisterActivity,
                                            "Lỗi kết nối đến server: ${t.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                            } else {
                                Toast.makeText(
                                    this,
                                    "Gửi email xác thực thất bại: ${emailTask.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this,
                        "Đăng ký Firebase thất bại: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

}
