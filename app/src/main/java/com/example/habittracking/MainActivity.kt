package com.example.habittracking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.keyframesWithSpline
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.habittracking.ui.theme.HabitTrackingTheme
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTrackingTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(
                        Modifier
                            .width(267.dp)
                            .height(267.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.demo_avatar)
    Box(modifier) {
        Column {
            Image(
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Text(
                text = "Dang nhap"

            )
            Column {
                CustomTextField(
                    label = "Username",
                    modifier = Modifier
                        .width(210.dp)
                        .height(40.dp)
                )
                CustomTextField(
                    label = "Password",
                    modifier = Modifier
                        .width(210.dp)
                        .height(40.dp)
                )
            }
        }
    }
}

@Composable
fun CustomTextField(
    label: String,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.TextField(
        value = "",
        onValueChange = { /* Handle input */ },
        label = { Text(text = label) },
        modifier = modifier,
        shape = RoundedCornerShape(15.dp), // Border bo góc 15
//        colors = androidx.compose.material3.TextFieldDefaults.textFieldColors(
//            containerColor = Color.White, // Màu nền
//            focusedIndicatorColor = Color.Transparent, // Không hiển thị đường viền khi focus
//            unfocusedIndicatorColor = Color.Transparent
//        )
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HabitTrackingTheme {
        Greeting(
            Modifier
                .width(267.dp)
                .height(267.dp)
        )
    }
}