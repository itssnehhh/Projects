package com.example.indianpostoffice.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.indianpostoffice.R
import com.example.indianpostoffice.databinding.LayoutAddressBinding
import com.example.indianpostoffice.model.Address
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class DetailAdapter(var context: Context,var addresslist: MutableList<Address>,var databaseReference: DatabaseReference,var displayData:Boolean = false,private val onItemClick:(Int) -> Unit):Adapter<DetailAdapter.MyViewHolder>() {

    class MyViewHolder(var binding : LayoutAddressBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = LayoutAddressBinding.inflate(LayoutInflater.from(context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return if (displayData) addresslist.size else 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var address = addresslist[position]

        holder.binding.tvPinCode.text = address.pincode
        holder.binding.tvDistrictState.text = "${address.district},${address.state}"
        holder.binding.tvPostOfficeName.text = address.postOfficeName
        address.city

        holder.binding.ivLike.setImageResource(
            if (address.isLike) R.drawable.icon_like_fill
            else R.drawable.baseline_favorite_border_24
        )

        holder.binding.ivLike.setOnClickListener {
            address.isLike = !address.isLike
            updateLikeStatusInFirebase(address)
            notifyItemChanged(position)
        }

        holder.binding.ivShare.setOnClickListener {
            shareLocation(address)
        }

        holder.binding.cardView.setOnClickListener {
            onItemClick(address.pincode!!.toInt())
            showFormWithData(holder,address.pincode!!.toInt())
        }
    }

    private fun showFormWithData(holder: DetailAdapter.MyViewHolder, itemId: Int) {
        val selectedLocation = addresslist.find { it.pincode?.toInt() == itemId }
        selectedLocation?.let {
            holder.binding.officeName1.text = ("Office Name :- ${it.postOfficeName}")
            holder.binding.pinCode1.text = ("Pincode :- ${it.pincode}")
            holder.binding.city.text = ("City :- ${it.city}")
            holder.binding.district.text = ("District :- ${it.district}")
            holder.binding.state.text = ("State :- ${it.state}")

            if (holder.binding.addressForm.isVisible){
                holder.binding.addressForm.visibility = View.GONE
            }else{
                holder.binding.addressForm.visibility = View.VISIBLE
            }
        }
    }

    private fun shareLocation(address: Address) {
        var shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"

        // Customize the text you want to share
        val shareText = "Location : ${address.postOfficeName},${address.city},${address.district},${address.state},${address.pincode}"
        shareIntent.putExtra(Intent.EXTRA_TEXT,shareIntent)
        context.startActivity(Intent.createChooser(shareIntent,"Share Location"))
    }

    private fun updateLikeStatusInFirebase(address: Address) {

        var pincodeNodeRef = databaseReference.child("likeStatus").child(address.pincode.toString())

        if (address.isLike){
            //Update Like status for the specific location in firebase
            val likeData = mapOf(
                "isLike" to true,
                "district" to address.district,
                "postOfficeName" to address.postOfficeName,
                "state" to address.state,
                "city" to address.city
            )

            pincodeNodeRef.setValue(likeData)
                .addOnSuccessListener {
                    Log.d("Firebase", "Like status updated successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Error updating Like status", e)
                }
        }else{
            pincodeNodeRef.removeValue()
                .addOnSuccessListener {
                    Log.d("Firebase", "Entry removed Successfully")
                }
                .addOnFailureListener { e->
                    Log.e("Firebase", "Error removing entry", e)
                }
        }

    }

    fun setDetail(mutableList: List<Address>,displayData: Boolean = true){
        this.addresslist.clear()
        this.addresslist.addAll(mutableList)
        this.displayData = displayData
        notifyDataSetChanged()
    }

    fun fetchLikeStatusFromFirebase(address: Address){
        var pincodeNodeRef = databaseReference.child("likeStatus").child(address.pincode.toString())

        pincodeNodeRef.child("isLike").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var isLike = snapshot.getValue(Boolean::class.java) ?: false
                address.isLike = isLike
                notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fetchAllLikeStatusFromFirebase(){
        addresslist.clear()
        //Reference to the "likeStatus" node
        val likeStatusNodeRef = databaseReference.child("likeStatus")

        //Fetch all like status data from Firebase

        likeStatusNodeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val likeStatusList = mutableListOf<Address>()
                for (snapshot in snapshot.children){
                    val pincode = snapshot.key.toString()
                    var isLike = snapshot.child("isLike").getValue(Boolean::class.java)
                    val district = snapshot.child("district").getValue(String::class.java) ?: ""
                    val state = snapshot.child("state").getValue(String::class.java) ?: ""
                    val city = snapshot.child("city").getValue(String::class.java) ?: ""
                    val officename =
                        snapshot.child("postOfficeName").getValue(String::class.java) ?: ""
                    if (isLike == true) {
                        // Create a Location object and add it to the list
                        val location = Address(
                            pincode = pincode,
                            isLike = isLike,
                            city=city,
                            district = district,
                            state = state,
                            postOfficeName = officename
                        )
                        likeStatusList.add(location)
                    } else {
                        // If isLike is false, delete the entry from the "likeStatus" node
                        snapshot.ref.removeValue()
                    }
                }
                setDetail(likeStatusList)
                notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}
