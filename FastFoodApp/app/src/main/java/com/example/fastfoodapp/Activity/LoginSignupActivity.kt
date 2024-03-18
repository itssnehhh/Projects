package com.example.fastfoodapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.fastfoodapp.Adapter.RegisterAdapter
import com.example.fastfoodapp.R
import com.example.fastfoodapp.databinding.ActivityLoginSignupBinding
import com.google.android.material.tabs.TabLayoutMediator

class LoginSignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)



        var adapter = RegisterAdapter(supportFragmentManager,lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tablayout,binding.viewPager){tab,position ->
            when(position){
                0 -> tab.text = "Login"
                1 -> tab.text = "Signup"
            }
        }.attach()
    }
}