package com.busanit501.myapplication6

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.busanit501.myapplication6.Adapter.UserAdapter
import com.busanit501.myapplication6.models.User
import com.busanit501.myapplication6.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val button: Button = findViewById(R.id.loadDataButton) // 버튼의 ID를 실제로 맞게 설정하세요
        button.setOnClickListener {
            fetchUsers()
        }
    }


    private fun fetchUsers() {
        val apiService = RetrofitClient.apiService
        apiService.getAllUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val call = RetrofitClient.apiService.getAllUsers()
                call.enqueue(object : Callback<List<User>> {
                    override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                        if (response.isSuccessful) {
                            val users = response.body()
                            // 성공적으로 데이터를 받았을 때 처리
                            // 예: usersRecyclerView.adapter = UserAdapter(users)
                        } else {
                            // 서버 응답은 성공했지만 HTTP 상태 코드가 오류인 경우
                            Log.e("API_ERROR", "Error code: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<List<User>>, t: Throwable) {
                        // Retrofit 요청이 실패한 경우 처리
                        Log.e("API_ERROR", "Request failed", t)
                    }
                })
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    userAdapter = UserAdapter(users)
                    recyclerView.adapter = userAdapter
                    println("작동하고 있어요1")


                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                println("작동안하고 있어요2")
                // Handle the error
            }
        })
    }
}