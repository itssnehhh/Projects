package com.example.fastfoodapp.Activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import com.example.fastfoodapp.Preference.PrefManager
import com.example.fastfoodapp.R
import com.example.fastfoodapp.databinding.ActivitySplashBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var manager: PrefManager
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            statusBarColor = Color.TRANSPARENT
        }

        firebaseAuth = Firebase.auth
        manager = PrefManager(this)

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (firebaseAuth.currentUser != null) {
                //true -> already login
                startActivity(Intent(this, HomeActivity::class.java))
                finishAffinity()
            } else {
                // false -> not login and on_boarding
                if (manager.getOnBoardingStatus()) {
                    //True
                    startActivity(Intent(this, LoginSignupActivity::class.java))
                    finish()
                } else {
                    //false
                    startActivity(Intent(this, OnBoardingActivity::class.java))
                    finish()
                }
            }
        }, 3000)
    }
}