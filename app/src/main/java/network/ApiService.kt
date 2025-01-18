package network

import com.example.layout.Uuser
import model.Achievement
import model.Activity
import model.Streak
import model.NewUser
import model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // Lấy danh sách người dùng
//    @GET("api/users")
//    fun getUsers(): Call<List<NewUser>>

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
    fun postUser(@Body newUser: NewUser): Call<User>

    // Lấy thông tin người dùng
    @GET("api/users/{username}")
    fun getUser(@Path("username") username: String): Call<User>

    // Tạo activity mới
    @POST("api/activities")
    fun postActivities(
        @Body activity: Activity
    ): Call<Activity>

    // Update streak
    @POST("api/streak")
    fun updateStreak(@Body streak: Streak): Call<Streak>

    // Kiểm tra achivement
}