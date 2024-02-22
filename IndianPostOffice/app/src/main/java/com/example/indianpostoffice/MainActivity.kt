package com.example.indianpostoffice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.indianpostoffice.adapter.FragmentAdapter
import com.example.indianpostoffice.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var adapter = FragmentAdapter(supportFragmentManager, lifecycle)
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "QUICK SEARCH"
                1 -> tab.text = "SEARCH BY AREA"
                2 -> tab.text = "SAVED RECORDS"
            }
        }.attach()
    }
}