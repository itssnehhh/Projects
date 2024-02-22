package com.example.newsapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newsapp.Model.Category
import com.example.newsapp.databinding.LayoutCategoryBinding

class CategoryAdapter(var context: Context,var categoryList: MutableList<Category>) :Adapter<CategoryAdapter.MyViewHolder>(){

    var categoryClickListener :((position: Int,category:Category) -> Unit)? = null


    class MyViewHolder(var binding: LayoutCategoryBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutCategoryBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var category = categoryList[position]

        holder.binding.tvCategory.text = category.category

        holder.binding.tvCategory.setOnClickListener{
            categoryClickListener?.invoke(position,category)
        }
    }
}