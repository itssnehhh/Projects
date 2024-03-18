package com.example.platzifakestore.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.platzifakestore.Adapter.UserListAdapter
import com.example.platzifakestore.Model.User
import com.example.platzifakestore.Model.UserItem
import com.example.platzifakestore.Network.ApiProduct
import com.example.platzifakestore.R
import com.example.platzifakestore.databinding.ActivityUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private var userList = mutableListOf<UserItem>()
    private lateinit var userListAdapter: UserListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getUserList()

        userListAdapter = UserListAdapter(this,userList)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = userListAdapter
    }

    private fun getUserList() {
        ApiProduct.init().getUsers().enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    var userList = response.body()
                    userList?.let {
                        if (userList != null) {
                            userList.addAll(it)
                            userListAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}