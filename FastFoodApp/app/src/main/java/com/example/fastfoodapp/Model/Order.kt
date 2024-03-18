package com.example.fastfoodapp.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Order(
    var orderId: String? = null,
    var userId: String = "",
    val image: String = "",
    val foodName: String = "",
    val qty: Int = 0,
    val foodPrice: Int = 0,
) : Parcelable
