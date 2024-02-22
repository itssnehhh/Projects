package com.example.foodmealapp.Activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodmealapp.Adapter.DetailAdapter
import com.example.foodmealapp.Model.MealDetail
import com.example.foodmealapp.R
import com.example.foodmealapp.databinding.ActivityMealDetailBinding
import com.squareup.picasso.Picasso

class MealDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealDetailBinding
    private lateinit var detailAdapter: DetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var mealDetailList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("MEALDETAIL",MealDetail::class.java)
        } else {
            intent.getParcelableArrayListExtra("MEALDETAIL")
        }


        detailAdapter = DetailAdapter(this,mealDetailList!!)
        binding.rvDetail.adapter = detailAdapter
        binding.rvDetail.layoutManager = LinearLayoutManager(this)
    }
}