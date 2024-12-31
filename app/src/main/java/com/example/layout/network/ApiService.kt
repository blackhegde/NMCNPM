package com.example.layout.network

import com.example.layout.model.Achievement
import com.example.layout.model.Activity
import com.example.layout.model.Streak
import com.example.layout.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Lấy danh sách người dùng
    @GET("api/users")
    fun getUsers(): Call<List<User>>

    // Lấy danh sách hoạt động của người dùng
    @GET("api/activities/{userId}")
    fun getActivities(@Path("userId") userId: Int): Call<List<Activity>>

    // Lấy streak của người dùng
    @GET("api/streak/{userId}")
    fun getStreak(@Path("userId") userId: Int): Call<Streak>

    // Lấy danh sách thành tựu của người dùng
    @GET("api/achievements/{userId}")
    fun getAchievements(@Path("userId") userId: Int): Call<List<Achievement>>

    // Lấy danh sách rank theo loại

    // Tạo người dùng mới
    @POST("api/user")
    fun postUser(@Body user: User): Call<User>

    // Tạo activity mới
    @POST("api/activities/{userId}")
    fun postActivities(
        @Path("userId") userId: Int,
        @Body activity: Activity
    ): Call<Activity>

    // Update streak
    @POST("api/streak")
    fun updateStreak(@Body streak: Streak): Call<Streak>

    // Kiểm tra achivement
}