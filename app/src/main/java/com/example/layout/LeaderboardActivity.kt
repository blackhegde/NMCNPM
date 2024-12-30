package com.example.layout

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.layout.LeaderboardAdapter
import com.example.layout.User

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
    }
}