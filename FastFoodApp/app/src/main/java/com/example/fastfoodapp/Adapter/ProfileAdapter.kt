package com.example.fastfoodapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.fastfoodapp.Model.User
import com.example.fastfoodapp.databinding.LayoutCardProfileBinding

class ProfileAdapter(var context: Context,var userList: MutableList<User>):Adapter<ProfileAdapter.MyViewHolder>() {

    class MyViewHolder(var binding: LayoutCardProfileBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutCardProfileBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var user = userList[position]

        holder.binding.tvUserName.text = user.name
        holder.binding.tvUserEmail.text = user.email
        holder.binding.tvUserContact.text = user.contact
        holder.binding.tvUserAddress.text = user.address

    }

    fun setItem(mutableList: MutableList<User>){
        this.userList = mutableList
        notifyDataSetChanged()
    }
}