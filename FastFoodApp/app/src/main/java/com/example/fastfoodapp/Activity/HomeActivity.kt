package com.example.fastfoodapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.fastfoodapp.Fragment.Home.HomeFragment
import com.example.fastfoodapp.Fragment.Offer.OfferFragment
import com.example.fastfoodapp.Fragment.Order.OrderFragment
import com.example.fastfoodapp.Fragment.Profile.ProfileFragment
import com.example.fastfoodapp.R
import com.example.fastfoodapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addFragment(HomeFragment())
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_home_menu -> {
                    addFragment(HomeFragment())
                    true
                }

                R.id.bottom_offer_menu -> {
                    addFragment(OfferFragment())
                    true
                }

                R.id.bottom_order_menu -> {
                    addFragment(OrderFragment())
                    true
                }

                R.id.bottom_profile_menu -> {
                    addFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun addFragment(fragment: Fragment) {
        var manager = supportFragmentManager
        var transaction = manager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}