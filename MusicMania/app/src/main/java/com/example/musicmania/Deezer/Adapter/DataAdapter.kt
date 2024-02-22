package com.example.musicmania.Deezer.Adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.musicmania.Deezer.Model.Data
import com.example.musicmania.R
import com.example.musicmania.databinding.LayoutDataBinding
import com.squareup.picasso.Picasso

class DataAdapter(var context:Context,var dataList: List<Data>):Adapter<DataAdapter.MyViewHolder>() {

    class MyViewHolder(var binding: LayoutDataBinding):ViewHolder(binding.root)
    private var lastSelection:MediaPlayer?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutDataBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var data = dataList[position]

        var mediaPlayer = MediaPlayer.create(context,data.preview.toUri())

        holder.binding.musicTitle.text = data.title
        Picasso.get().load(data.album.cover).into(holder.binding.imageView)

        holder.binding.btnPlay.setOnClickListener {
            if(lastSelection!=null && lastSelection!!.isPlaying){
                lastSelection!!.stop()
            }
            lastSelection = mediaPlayer
            mediaPlayer.start()
        }

        holder.binding.btnPause.setOnClickListener {
            mediaPlayer.pause()
        }
    }
}

