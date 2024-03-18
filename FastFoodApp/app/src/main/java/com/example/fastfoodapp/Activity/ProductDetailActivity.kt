package com.example.fastfoodapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fastfoodapp.Model.Offer
import com.example.fastfoodapp.Model.Order
import com.example.fastfoodapp.databinding.ActivityProductDetailBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.squareup.picasso.Picasso

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProductDetailBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseAuth : FirebaseAuth

    // Define variables to hold quantity and initial price
    private var quantity: Int = 1
    private var totalPrice: Int = 0
    private lateinit var product : Offer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseAuth = Firebase.auth
        databaseReference = Firebase.database.reference

        product = intent.getParcelableExtra("PRODUCT")!!

        product?.let {
            binding.tvFoodName.text = it.foodName
            binding.tvRestaurantName.text = it.restaurantName
            binding.tvFoodPrice.text = "$ ${it.disPrice}"
            binding.tvFoodType.text = it.type
            binding.tvFoodCategory.text = it.category
            Picasso.get().load(it.image).into(binding.ivProduct)
        }

        totalPrice = product.disPrice

        // Set initial quantity and price
        updateQuantityAndPrice()

        // Plus button click listener
        binding.btnPlus.setOnClickListener {
            quantity++
            updateQuantityAndPrice()
        }

        // Minus button click listener
        binding.btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                updateQuantityAndPrice()
            }
        }

        //Add to cart
        binding.btnAddToCart.setOnClickListener {
            addToCart()
        }
    }

    private fun addToCart() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser!=null){
            val userId = currentUser.uid
            val priceString = binding.tvFoodPrice.text.toString()
            val price = priceString.replace("$ ", "").toInt()
            val order = Order( null,userId,
                product.image!!,product.foodName!!,binding.tvFoodQty.text.toString().toInt(),price)
            saveOrder(order)
            Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveOrder(order: Order) {

        val orderRef =databaseReference.child("orders").child(databaseAuth.currentUser!!.uid).child("orders")

        val orderId = orderRef.push().key

        orderId?.let {
            orderRef.child(it).setValue(order.copy(orderId = it))
        }
    }

    private fun updateQuantityAndPrice() {
        // Update UI to display updated quantity
        binding.tvFoodQty.text = quantity.toString()

        // Calculate total price based on quantity
        totalPrice = quantity * product.disPrice.toInt()
        binding.tvFoodPrice.text = "$ $totalPrice"
    }
}