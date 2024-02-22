package com.example.foodmealapp.Activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodmealapp.Adapter.MealAdapter
import com.example.foodmealapp.Model.Meal
import com.example.foodmealapp.Model.MealIntroduction
import com.example.foodmealapp.Network.ApiCategory
import com.example.foodmealapp.databinding.ActivityMealListBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMealListBinding
    private lateinit var mealAdapter: MealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var mealList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("MEALLIST",Meal::class.java)
        } else {
            intent.getParcelableArrayListExtra("MEALLIST")
        }

        mealAdapter = MealAdapter(this,mealList!!){selectedMeal ->
            getSelectedMealDetail(selectedMeal)
        }
        binding.rvMeal.adapter = mealAdapter
        binding.rvMeal.layoutManager = GridLayoutManager(this,2)

    }

    private fun getSelectedMealDetail(selectedMeal: Meal) {
        ApiCategory.init().getMealDetail(selectedMeal.idMeal).enqueue(object :Callback<MealIntroduction>{
            override fun onResponse(
                call: Call<MealIntroduction>,
                response: Response<MealIntroduction>
            ) {
                if (response.isSuccessful){
                    var mealDetailList = response.body()?.meals
                    mealDetailList?.let {
                        if (mealDetailList!=null){
                            var intent = Intent(applicationContext,MealDetailActivity::class.java)
                            intent.putParcelableArrayListExtra("MEALDETAIL",ArrayList(it))
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MealIntroduction>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }
}