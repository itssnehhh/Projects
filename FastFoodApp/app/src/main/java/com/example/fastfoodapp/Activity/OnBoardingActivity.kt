package com.example.fastfoodapp.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.fastfoodapp.Adapter.OnBoardingAdapter
import com.example.fastfoodapp.Model.OnBoarding
import com.example.fastfoodapp.Preference.PrefManager
import com.example.fastfoodapp.R
import com.example.fastfoodapp.databinding.ActivityOnBoardingBinding

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private var onBoardingList = mutableListOf<OnBoarding>()
    private lateinit var pagerAdapter : OnBoardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            statusBarColor = Color.TRANSPARENT
        }

        prepareData()

        pagerAdapter = OnBoardingAdapter(this, onBoardingList)
        binding.viewPager.adapter = pagerAdapter

        updateIndicator(0)
        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == pagerAdapter.count - 1) {
                    binding.btnStart.visibility = View.VISIBLE
                    binding.layoutDots.visibility = View.GONE
                } else {
                    binding.layoutDots.visibility = View.VISIBLE
                    binding.btnStart.visibility = View.GONE
                    updateIndicator(position)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

    }

    private fun updateIndicator(position: Int) {
        binding.layoutDots.removeAllViews()

        for (i in 0 until pagerAdapter.count) {
            var imageView = ImageView(this)

            if (position == i) {
                imageView.setBackgroundResource(R.drawable.active_indicater)
            } else {
                imageView.setBackgroundResource(R.drawable.inactive_indicater)
            }

            var params = LinearLayout.LayoutParams(
                ViewPager.LayoutParams.WRAP_CONTENT,
                ViewPager.LayoutParams.WRAP_CONTENT
            )
            val margin = params.setMargins(8, 0, 0, 0)
            binding.layoutDots.addView(imageView, params)
        }
    }


    private fun prepareData() {
        onBoardingList.add(
            OnBoarding(
                1,
                "Save Food with \nour new Feature!",
                R.drawable.onboarding_slide_1
            )
        )
        onBoardingList.add(
            OnBoarding(
                2,
                "Set preferences for \nmultiple users from \nvarious restaurants!",
                R.drawable.onboarding_slide_2
            )
        )
        onBoardingList.add(
            OnBoarding(
                3,
                "Fast, rescued food \nat your service.",
                R.drawable.onboarding_slide_3
            )
        )
    }

    fun onButtonClicked(view: View) {
        //PrefManager
        var manager = PrefManager(this)
        manager.updateOnBoardingStatus(true)

        //Navigate to next activity
        startActivity(Intent(this, LoginSignupActivity::class.java))
        finish()
    }
}