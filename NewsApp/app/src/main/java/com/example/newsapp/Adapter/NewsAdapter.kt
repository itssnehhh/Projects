package com.example.newsapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.newsapp.Model.Article
import com.example.newsapp.R
import com.example.newsapp.databinding.LayoutNewsBinding
import com.squareup.picasso.Picasso

class NewsAdapter(private var context: Context, var articleList: MutableList<Article>, private var onItemClickListener: OnItemClickListener):Adapter<NewsAdapter.MyViewHolder>() {

    interface OnItemClickListener{
        fun onItemClick(url:String)
    }

    inner class MyViewHolder(var binding: LayoutNewsBinding):ViewHolder(binding.root){
        init {
            binding.cardNews.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    val article = articleList[position]
                    onItemClickListener.onItemClick(article.url)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutNewsBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var article = articleList[position]

        holder.binding.tvHeadline.text = article.title
        holder.binding.tvContent.text = article.content
        holder.binding.tvTime.text = article.publishedAt
        Picasso.get().load(article.image).into(holder.binding.ivNews)
    }


}