package com.example.platzifakestore.Activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.platzifakestore.Adapter.ProductAdapter
import com.example.platzifakestore.ApiService.ApiService
import com.example.platzifakestore.Model.Product
import com.example.platzifakestore.Model.ProductItem
import com.example.platzifakestore.Network.ApiProduct
import com.example.platzifakestore.R
import com.example.platzifakestore.databinding.ActivityProductBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var productList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra("PRODUCTLIST",ProductItem::class.java)
        } else {
            intent.getParcelableArrayListExtra("PRODUCTLIST")
        }

        productAdapter = ProductAdapter(this, productList!!)
        binding.rvProduct.adapter = productAdapter
        binding.rvProduct.layoutManager = LinearLayoutManager(this)
    }

}



