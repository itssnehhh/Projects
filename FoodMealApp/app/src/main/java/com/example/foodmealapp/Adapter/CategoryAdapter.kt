package com.example.foodmealapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.foodmealapp.Model.Category
import com.example.foodmealapp.databinding.LayoutCategoryBinding
import com.squareup.picasso.Picasso

class CategoryAdapter(
    var context: Context,
    var categoryList: MutableList<Category>,
    val onItemClick: (Category) -> Unit
) : Adapter<CategoryAdapter.MyViewHolder>() {

    class MyViewHolder(var binding: LayoutCategoryBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var category = categoryList[position]

        holder.binding.tvTitle.text = category.strCategory
        holder.binding.tvDescription.text = category.strCategoryDescription
        Picasso.get().load(category.strCategoryThumb).into(holder.binding.ivCategory)

        holder.binding.cardView.setOnClickListener {
            onItemClick.invoke(category)
        }
    }

}