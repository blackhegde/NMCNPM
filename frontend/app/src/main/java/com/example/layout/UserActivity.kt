package com.example.layout

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class UserActivity : AppCompatActivity() {
    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        setContentView(R.layout.activity_user)

        val btnHome : ImageButton = findViewById(R.id.btnHome)

        btnHome.setOnClickListener(){
            finish()
        }
    }
}