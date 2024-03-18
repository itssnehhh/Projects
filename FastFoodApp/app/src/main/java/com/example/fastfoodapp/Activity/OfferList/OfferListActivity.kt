package com.example.fastfoodapp.Activity.OfferList

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastfoodapp.Adapter.OfferListAdapter
import com.example.fastfoodapp.Model.Offer
import com.example.fastfoodapp.databinding.ActivityCuisineListBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage

class OfferListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCuisineListBinding

    private lateinit var storageReference: StorageReference
    private lateinit var databaseReference: DatabaseReference

    private var offerList = mutableListOf<Offer>()
    private lateinit var offerListAdapter: OfferListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuisineListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = Firebase.database.reference
        storageReference = Firebase.storage.reference

        offerListAdapter = OfferListAdapter(this, offerList)
        binding.rvOfferList.adapter = offerListAdapter
        binding.rvOfferList.layoutManager = LinearLayoutManager(this)

        databaseReference.child("offer-node").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                offerList.clear()

                for (snap in snapshot.children) {
                    var offer = snap.getValue(Offer::class.java)
                    offerList.add(offer!!)
                }

                offerListAdapter.setOffer(offerList)

                offerListAdapter.itemDeleteClickListener = { position, offer ->
                    try {
                        showCustomDialog(position,offer)
                        offerListAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }


                offerListAdapter.itemEditClickListener = {position, offer ->
                    var intent = Intent(applicationContext,OfferAddActivity::class.java)
                    intent.putExtra("OFFER",offer)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this, OfferAddActivity::class.java))
        }
    }

    private fun showCustomDialog(position: Int, offer: Offer) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
            .setMessage("Are you sure you want delete this offer")
            .setPositiveButton("YES",DialogInterface.OnClickListener { dialog, which ->
                deleteOffer(position,offer)
            })
            .setNegativeButton("CANCEL",DialogInterface.OnClickListener { dialog, which ->

            })
        var dialog =builder.create()
        dialog.show()
    }

    private fun deleteOffer(position: Int, offer: Offer) {
        var offerId = offer.id
        var imageUrl = offer.image

        if (offerId!=null && imageUrl !=null){
            storageReference.child("Offer Images").child("${System.currentTimeMillis()}.jpg").delete()
                .addOnSuccessListener {
                    databaseReference.child("offer-node").child(offerId).removeValue()
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                offerListAdapter.removeOffer(position)
                                Toast.makeText(this, "Offer Deleted successfully", Toast.LENGTH_SHORT).show()
                            }
                        }.addOnFailureListener {
                            Log.e("Firebase", "Failed to delete image from storage", it)
                            Toast.makeText(applicationContext, "${it.message}", Toast.LENGTH_LONG).show()
                        }
                }
        }
    }
}