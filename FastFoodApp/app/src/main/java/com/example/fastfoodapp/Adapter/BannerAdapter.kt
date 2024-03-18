package com.example.fastfoodapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.ViewPager2
import com.example.fastfoodapp.Model.Banner
import com.example.fastfoodapp.databinding.LayoutBannerBinding

class BannerAdapter(
    var context: Context,
    private var imageList: MutableList<Banner>,
    private var viewPager: ViewPager2
) : Adapter<BannerAdapter.MyViewHolder>() {

    class MyViewHolder(var binding: LayoutBannerBinding) : ViewHolder(binding.root)

    private val run = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutBannerBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var image = imageList[position]

        holder.binding.imageView.setImageResource(image.image)

        if (position == imageList.size - 1) {
            viewPager.post(run)
        }
    }
}