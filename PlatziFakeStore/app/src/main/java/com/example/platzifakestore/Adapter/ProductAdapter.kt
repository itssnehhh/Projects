package com.example.platzifakestore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.platzifakestore.Model.ProductItem
import com.example.platzifakestore.R
import com.example.platzifakestore.databinding.LayoutProductBinding
import com.squareup.picasso.Picasso

class ProductAdapter(var context: Context,var productList: MutableList<ProductItem>):Adapter<ProductAdapter.MyViewHolder>() {

    class MyViewHolder(var binding: LayoutProductBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutProductBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var product = productList[position]

        holder.binding.tvCategory.text = product.category.name
        holder.binding.tvTitle.text = product.title
        holder.binding.tvDescription.text = product.description
        holder.binding.tvPrice.text = "₹ ${product.price}.00"
        if (product.images.isNotEmpty()) {
            Picasso.get().load(product.images[0]).into(holder.binding.ivProduct)
        } else {
            // If there are no images, you may want to set a placeholder or handle it accordingly
             Picasso.get().load(R.drawable.ic_launcher_foreground).into(holder.binding.ivProduct)
        }
    }
}