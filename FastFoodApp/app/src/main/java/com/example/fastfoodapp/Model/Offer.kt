package com.example.fastfoodapp.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Offer(
    var id: String? = null,
    var foodName : String ?=null,
    var image: String? = null,
    var category: String? = null,
    var type: String? = null,
    var discount: Int = 0,
    var price: Int? = 0,
    var disPrice: Int = price!!*discount/100,
    var restaurantName: String? = null,
    var rating: Float? = 0.0f,
    var calaries: String? = null,
    var description: String? = null,
):Parcelable
