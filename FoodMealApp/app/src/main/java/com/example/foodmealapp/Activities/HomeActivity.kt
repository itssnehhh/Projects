package com.example.foodmealapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodmealapp.Adapter.CategoryAdapter
import com.example.foodmealapp.ApiService.ApiService
import com.example.foodmealapp.Model.Category
import com.example.foodmealapp.Model.CategoryData
import com.example.foodmealapp.Model.MealData
import com.example.foodmealapp.Network.ApiCategory
import com.example.foodmealapp.R
import com.example.foodmealapp.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var categoryList = mutableListOf<Category>()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategoryList()

        categoryAdapter = CategoryAdapter(this, categoryList) { selectedCategory ->
            getMealByCategory(selectedCategory)
        }
        binding.rvCategory.adapter = categoryAdapter
        binding.rvCategory.layoutManager = LinearLayoutManager(this)

    }


    private fun getCategoryList() {
        ApiCategory.init().getCategories().enqueue(object : Callback<CategoryData> {
            override fun onResponse(call: Call<CategoryData>, response: Response<CategoryData>) {
                if (response.isSuccessful) {
                    var category = response.body()?.categories
                    category?.let {
                        categoryList.addAll(it)
                        categoryAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<CategoryData>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getMealByCategory(selectedCategory: Category) {
        ApiCategory.init().getMealByCategory(selectedCategory.strCategory)
            .enqueue(object : Callback<MealData> {
                override fun onResponse(call: Call<MealData>, response: Response<MealData>) {
                    if (response.isSuccessful) {
                        var mealList = response.body()?.meals
                        mealList?.let {
                            if (mealList != null) {
                                var intent = Intent(applicationContext,MealListActivity::class.java)
                                intent.putParcelableArrayListExtra("MEALLIST",ArrayList(it))
                                startActivity(intent)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<MealData>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }

}