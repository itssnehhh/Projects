package com.example.foodmealapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.foodmealapp.Model.MealDetail
import com.example.foodmealapp.databinding.LayoutDetailBinding
import com.squareup.picasso.Picasso

class DetailAdapter(var context: Context, var mealDetailList: MutableList<MealDetail>) :
    Adapter<DetailAdapter.MyViewHolder>() {

    class MyViewHolder(var binding: LayoutDetailBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutDetailBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mealDetailList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var mealDetail = mealDetailList[position]

        holder.binding.tvTitle.text = mealDetail.strMeal
        holder.binding.tvDescription.text = "How to make ?\n\n${mealDetail.strInstructions}"
        holder.binding.tvArea.text = "Area :- ${mealDetail.strArea}"
        holder.binding.tvCategory.text = "Category :- ${mealDetail.strCategory}"
        holder.binding.tvIngredient.text =
            "Ingredients :- ${mealDetail.strIngredient1}, ${mealDetail.strIngredient2}, ${mealDetail.strIngredient3}, ${mealDetail.strIngredient4}, ${mealDetail.strIngredient5}, ${mealDetail.strIngredient6}, ${mealDetail.strIngredient7}, ${mealDetail.strIngredient8}, ${mealDetail.strIngredient9}, ${mealDetail.strIngredient10}, ${mealDetail.strIngredient11}, ${mealDetail.strIngredient12}, ${mealDetail.strIngredient13},${mealDetail.strIngredient14},${mealDetail.strIngredient15},${mealDetail.strIngredient16},${mealDetail.strIngredient17},${mealDetail.strIngredient18},${mealDetail.strIngredient19},${mealDetail.strIngredient20}"
        Picasso.get().load(mealDetail.strMealThumb).into(holder.binding.ivMeal)
    }
}