package com.example.fastfoodapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.fastfoodapp.Model.OnBoarding
import com.example.fastfoodapp.databinding.LayoutOnboardingBinding

class OnBoardingAdapter(var context: Context, private var onBoardingList: MutableList<OnBoarding>) :
    PagerAdapter() {

    private lateinit var binding: LayoutOnboardingBinding

    override fun getCount(): Int {
        return onBoardingList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var item = onBoardingList[position]
        binding = LayoutOnboardingBinding.inflate(LayoutInflater.from(context), container, false)
        binding.tvTitle.text = item.title
        binding.ivThumbnail.setImageResource(item.image)
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }
}