package com.example.platzifakestore.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.platzifakestore.Adapter.CategoryAdapter
import com.example.platzifakestore.Model.Category
import com.example.platzifakestore.Model.CategoryItem
import com.example.platzifakestore.Model.ProductItem
import com.example.platzifakestore.Network.ApiProduct
import com.example.platzifakestore.R
import com.example.platzifakestore.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var categoryList = mutableListOf<CategoryItem>()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        getCategoryList()

        categoryAdapter = CategoryAdapter(this, categoryList) { selectedCategory ->
            getProductByCategory(selectedCategory)
        }
        binding.rvCategory.adapter = categoryAdapter
        binding.rvCategory.layoutManager = GridLayoutManager(this, 2)

        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.iv_user -> {
                    startActivity(Intent(this,UserActivity::class.java))
                    true
                }
                R.id.iv_add -> {
                    startActivity(Intent(this,CreateProductActivity::class.java))
                    true
                }
                else -> false
            }
        }

    }



    private fun getCategoryList() {
        ApiProduct.init().getCategories().enqueue(object : Callback<Category> {
            override fun onResponse(call: Call<Category>, response: Response<Category>) {
                if (response.isSuccessful) {
                    var category = response.body()
                    category?.let {
                        if (category != null) {
                            categoryList.addAll(it)
                            categoryAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Category>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getProductByCategory(category: CategoryItem) {
        ApiProduct.init().getProductsByCategory(category.id)
            .enqueue(object : Callback<List<ProductItem>> {
                override fun onResponse(
                    call: Call<List<ProductItem>>,
                    response: Response<List<ProductItem>>
                ) {
                    if (response.isSuccessful) {
                        var productList = response.body()
                        productList?.let {
                            if (productList != null) {
                                var intent = Intent(applicationContext, ProductActivity::class.java)
                                intent.putParcelableArrayListExtra("PRODUCTLIST", ArrayList(it))
                                startActivity(intent)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<List<ProductItem>>, t: Throwable) {
                    Log.e("TAG", "Failed to get product list", t)

                    // Log details about the response
                    if (t is HttpException) {
                        val response = t.response()
                        Log.d("TAG", "Response Code: ${response?.code()}")
                        Log.d("TAG", "Response Message: ${response?.message()}")
                        Log.d("TAG", "Response Body: ${response?.errorBody()?.string()}")
                    }
                }

            })
    }
}