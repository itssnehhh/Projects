package com.example.platzifakestore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.platzifakestore.Model.CategoryItem
import com.example.platzifakestore.databinding.LayoutCategoryBinding
import com.squareup.picasso.Picasso

class CategoryAdapter(
    var context: Context,
    var categoryList: MutableList<CategoryItem>,
    val onItemClick: (CategoryItem) -> Unit
) :
    Adapter<CategoryAdapter.MyViewHolder>() {

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
        holder.binding.tvCategory.text = category.name

        Picasso.get().load(category.image).into(holder.binding.ivCategory)

        holder.binding.itemCardView.setOnClickListener {
            onItemClick.invoke(category)
        }
    }
}