package com.example.platzifakestore.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.platzifakestore.Model.UserItem
import com.example.platzifakestore.R
import com.example.platzifakestore.databinding.LayoutUserBinding
import com.squareup.picasso.Picasso

class UserListAdapter(var context: Context, var userList: MutableList<UserItem>) :
    Adapter<UserListAdapter.MyViewHolder>() {

    class MyViewHolder(var binding: LayoutUserBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var user = userList[position]

        holder.binding.tvName.text = user.name
        holder.binding.tvRole.text = ".${user.role}"
        holder.binding.tvEmail.text = user.email
        holder.binding.tvPassword.text = user.password
        if (user.avatar.isNotEmpty()) {
            Picasso.get().load(user.avatar).into(holder.binding.ivUser)
        } else {
            // If there are no images, you may want to set a placeholder or handle it accordingly
            Picasso.get().load(R.drawable.ic_launcher_foreground).into(holder.binding.ivUser)
        }
    }

}