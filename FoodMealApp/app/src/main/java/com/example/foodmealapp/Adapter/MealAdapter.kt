package com.example.foodmealapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.foodmealapp.Model.Meal
import com.example.foodmealapp.databinding.LayoutMealBinding
import com.squareup.picasso.Picasso

class MealAdapter(var context: Context,var mealList : MutableList<Meal>,var onItemClick : (Meal) -> Unit):Adapter<MealAdapter.MyViewHolder>() {

    class MyViewHolder(var binding : LayoutMealBinding ):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutMealBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var meal = mealList[position]

        holder.binding.tvMeal.text = meal.strMeal
        Picasso.get().load(meal.strMealThumb).into(holder.binding.ivMeal)

        holder.binding.mealCard.setOnClickListener {
            onItemClick.invoke(meal)
        }
    }
}