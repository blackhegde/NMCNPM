package com.example.habittracking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.habittracking.ui.theme.HabitTrackingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitTrackingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // Avatar Image
            Image(
                painter = painterResource(R.drawable.demo_avatar),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
            )

            // Title Text
            Text(
                text = "Đăng nhập",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Input Fields
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomTextField(label = "Username")
                CustomTextField(label = "Password")
            }


            //Login Button
//            Button(onClick = () ) {
//                Text("Xác nhận")
//            }
        }
    }
}


@Composable
fun CustomTextField(
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        label = { Text(text = label) },
        modifier = modifier
            .width(210.dp)
            .height(56.dp),
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun HomepageScreen() {
    Scaffold (
        topBar = { HeaderBar() },
        bottomBar = { FooterBar() }
    ) {
        paddingValues ->
        MainContent(Modifier.padding(paddingValues))
    }
}

@Composable
fun HeaderBar() {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 5.dp,
                        spotColor = Color(0x26000000),
                        ambientColor = Color(0x26000000)
                    )
                    .height(60.dp)
                    .background(color = Color(0xFFFFFFFF))
                    .padding(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Image(
                        painter = painterResource(R.drawable.streak),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text("217")
                }
                Image(
                    painter = painterResource(R.drawable.notification),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }
    )
}

@Composable
fun FooterBar() {
    val navController = rememberNavController()
    BottomAppBar {
        NavHost(navController = navController,
            startDestination = "home"
        ) {
            composable("ranking") {}
            composable("start") {}
            composable("statistic") {}
        }
        BottomNavigationItem(

        )
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(10) {
            index ->
            PostCard()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomepagePreview() {
    HabitTrackingTheme {
        HomepageScreen()
    }
}
