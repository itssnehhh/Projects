package com.example.fastfoodapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.fastfoodapp.Model.Order
import com.example.fastfoodapp.R
import com.example.fastfoodapp.databinding.LayoutOrderlistBinding
import com.squareup.picasso.Picasso

class OrderListAdapter(var context: Context, private var orderList: MutableList<Order>) : Adapter<OrderListAdapter.MyViewHolder>(){

    var onItemDeleteClickListener : ((position:Int,order:Order) -> Unit)?=null

    class MyViewHolder(var binding: LayoutOrderlistBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutOrderlistBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var order = orderList[position]

        if (order.image.isNotEmpty()) {
            Picasso.get().load(order.image).into(holder.binding.ivProduct)
        } else {
            Picasso.get().load(R.drawable.app_logo_circular).into(holder.binding.ivProduct)
        }
        holder.binding.tvFoodName.text = order.foodName
        holder.binding.tvFoodQty.text = "Qty :- ${order.qty}"
        holder.binding.tvFoodPrice.text = "Price :- $ ${order.foodPrice}"

        holder.binding.ivDelete.setOnClickListener {
            onItemDeleteClickListener?.invoke(position,order)
        }
    }

    fun setOrderList(mutableList: MutableList<Order>){
        this.orderList = mutableList
        notifyDataSetChanged()
    }

    fun getTotalPrice(): Double {
        var totalPrice = 0.0
        for (order in orderList) {
            totalPrice += order.foodPrice
        }
        return totalPrice
    }
}