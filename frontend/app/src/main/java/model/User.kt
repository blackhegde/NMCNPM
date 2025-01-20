package model

data class User(
    val id: Int,             // ID tự động tạo từ server
    val username: String,    // Tên người dùng
    val email: String,       // Email của người dùng
    val avatar_type: Int     // Loại avatar
)
