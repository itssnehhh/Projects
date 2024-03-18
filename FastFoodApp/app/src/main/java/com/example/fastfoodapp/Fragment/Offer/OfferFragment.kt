package com.example.fastfoodapp.Fragment.Offer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastfoodapp.Adapter.BannerAdapter
import com.example.fastfoodapp.Adapter.OfferAdapter
import com.example.fastfoodapp.Model.Banner
import com.example.fastfoodapp.Model.Offer
import com.example.fastfoodapp.R
import com.example.fastfoodapp.databinding.FragmentOfferBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class OfferFragment : Fragment() {

    private lateinit var binding: FragmentOfferBinding

    private lateinit var databaseRef: DatabaseReference

    private var bannerList = mutableListOf<Banner>()
    private lateinit var bannerAdapter: BannerAdapter

    private var offerList = mutableListOf<Offer>()
    private lateinit var offerAdapter: OfferAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOfferBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bannerList.add(Banner(1,R.drawable.offer_banner_1))
        bannerList.add(Banner(2,R.drawable.offer_banner_2))

        bannerAdapter = BannerAdapter(requireContext(), bannerList, binding.viewPager)
        binding.viewPager.adapter = bannerAdapter


        databaseRef = Firebase.database.reference

        offerAdapter = OfferAdapter(requireContext(), offerList)
        binding.rvOffer.adapter = offerAdapter
        binding.rvOffer.layoutManager = LinearLayoutManager(requireContext())


        databaseRef.child("offer-node").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                offerList.clear()

                for (snap in snapshot.children) {
                    var offer = snap.getValue(Offer::class.java)
                    offerList.add(offer!!)
                }

                offerAdapter.setOffer(offerList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}