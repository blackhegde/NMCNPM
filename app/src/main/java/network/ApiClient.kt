package network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://nmcnpm-habit-track.onrender.com"  // Địa chỉ API backend

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())  // Chuyển đổi JSON thành đối tượng Kotlin
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}