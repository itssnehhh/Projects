package com.example.fastfoodapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.fastfoodapp.Model.Offer
import com.example.fastfoodapp.databinding.LayoutOfferlistBinding
import com.squareup.picasso.Picasso

class OfferListAdapter(var context: Context, private var offerList: MutableList<Offer>):Adapter<OfferListAdapter.MyViewHolder>() {

    var itemCardClickListener: ((position: Int, offer: Offer) -> Unit)? = null
    var itemEditClickListener: ((position: Int, offer: Offer) -> Unit)? = null
    var itemDeleteClickListener: ((position: Int, offer: Offer) -> Unit)? = null


    class MyViewHolder(var binding : LayoutOfferlistBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutOfferlistBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var offer = offerList[position]

        holder.binding.tvName.text = offer.foodName
        holder.binding.tvRestaurantName.text = offer.restaurantName
        holder.binding.tvCategory.text = offer.category
        Picasso.get().load(offer.image).into(holder.binding.ivOffer)

        holder.binding.cardView.setOnClickListener {
            itemCardClickListener?.invoke(position, offer)
        }

        holder.binding.ivDelete.setOnClickListener {
            itemDeleteClickListener?.invoke(position, offer)
        }

        holder.binding.ivEdit.setOnClickListener {
            itemEditClickListener?.invoke(position, offer)
        }

    }

    fun setOffer(mutableList: MutableList<Offer>){
        this.offerList = mutableList
        notifyDataSetChanged()
    }

    fun removeOffer(position: Int){
        offerList.removeAt(position)
        notifyItemChanged(position)
    }
}