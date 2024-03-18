package com.example.fastfoodapp.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.fastfoodapp.Activity.ProductDetailActivity
import com.example.fastfoodapp.Model.Offer
import com.example.fastfoodapp.R
import com.example.fastfoodapp.databinding.LayoutOfferBinding
import com.google.firebase.database.core.view.View
import com.squareup.picasso.Picasso

class OfferAdapter(var context: Context, var offerList: MutableList<Offer>) :
    Adapter<OfferAdapter.MyViewHolder>() {


    inner class MyViewHolder(var binding: LayoutOfferBinding) : ViewHolder(binding.root),
        android.view.View.OnClickListener {
        init {
            binding.cardView.setOnClickListener(this)
        }

        override fun onClick(view: android.view.View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val offer = offerList[position]
                val intent = Intent(context, ProductDetailActivity::class.java)
                intent.putExtra("PRODUCT",offer) // Pass necessary data
                context.startActivity(intent)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutOfferBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var offer = offerList[position]

        val imageUrl = offer.image

        holder.binding.tvResName.text = offer.restaurantName
        holder.binding.tvFoodCategory.text = offer.category
        holder.binding.tvFoodType.text = offer.type
        holder.binding.tvFoodPrice.text = "$${offer.price.toString()}"

        holder.binding.tvFoodDisPrice.text = offer.disPrice.toString()

        holder.binding.tvFoodPrice.paintFlags =
            holder.binding.tvFoodPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        holder.binding.tvFoodDisPrice.text = "$${offer.disPrice.toString()}"
        holder.binding.tvCaleries.text = offer.calaries
        holder.binding.tvDescription.text = offer.description

        if (!imageUrl.isNullOrEmpty()) {
            // Use Picasso to load the image into the ImageView
            Picasso.get().load(imageUrl).into(holder.binding.ivOfferFood)

        } else {
            holder.binding.ivOfferFood.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    fun setOffer(mutableList: MutableList<Offer>) {
        this.offerList = mutableList
        notifyDataSetChanged()
    }

}