package com.example.indianpostoffice.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.indianpostoffice.adapter.DetailAdapter
import com.example.indianpostoffice.databinding.FragmentSearchBinding
import com.example.indianpostoffice.model.Address
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var detailAdapter: DetailAdapter
    private lateinit var databaseReference: DatabaseReference

    private var addressList = mutableListOf<Address>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        databaseReference = Firebase.database.reference

        detailAdapter = DetailAdapter(requireContext(), addressList, databaseReference, false) {

        }
        binding.recycleview.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleview.adapter = detailAdapter

        binding.recycleview1.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleview1.adapter = detailAdapter

        binding.recycleview2.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleview2.adapter = detailAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = Firebase.database.reference

        binding.radioButtonOption1.setOnClickListener { onRadioButtonClicked(binding.radioButtonOption1) }
        binding.radioButtonOption2.setOnClickListener { onRadioButtonClicked(binding.radioButtonOption2) }
        binding.radioButtonOption3.setOnClickListener { onRadioButtonClicked(binding.radioButtonOption3) }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterDataByPin(it) }
                return true
            }

        })

        binding.searchView1.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterDataByDistrict(it) }
                return true
            }
        })

        binding.searchView2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterDataByOfficeName(it)
                }
                return true
            }
        })
    }

    private fun filterDataByOfficeName(officeName: String) {
        addressList.clear()
        databaseReference.child("address").orderByChild("postOfficeName").equalTo(officeName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot in snapshot.children) {
                        val location = snapshot.getValue(Address::class.java)
                        location?.let {
                            addressList.add(it)
                        }
                    }

                    detailAdapter.setDetail(addressList, true)
                    detailAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun filterDataByPin(pin: String) {
        addressList.clear()
        pin.length >= 2
        databaseReference.child("address").orderByChild("pincode").equalTo(pin)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot in snapshot.children) {
                        val address = snapshot.getValue(Address::class.java)
                        address?.let {

                            binding.progress.visibility = View.GONE
                            addressList.add(it)
                        }
                    }
                    detailAdapter.setDetail(addressList, true)
                    detailAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Data", "onCancelled: Data is Not coming")
                }

            })
    }

    private fun filterDataByDistrict(officeName: String) {
        addressList.clear()
        databaseReference.child("address").orderByChild("postOfficeName").equalTo(officeName)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot in snapshot.children) {
                        val address = snapshot.getValue(Address::class.java)
                        address?.let {
                            addressList.add(it)
                        }
                    }
                    detailAdapter.setDetail(addressList, true)
                    detailAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun onRadioButtonClicked(radioButton: View) {
        when (radioButton.id) {
            binding.radioButtonOption1.id -> {
                binding.searchView.visibility = View.VISIBLE
                binding.searchView1.visibility = View.GONE
                binding.searchView2.visibility = View.GONE
                binding.recycleview.visibility = View.VISIBLE
                binding.recycleview1.visibility = View.GONE
                binding.recycleview2.visibility = View.GONE
            }

            binding.radioButtonOption2.id -> {
                binding.searchView.visibility = View.GONE
                binding.searchView1.visibility = View.VISIBLE
                binding.searchView2.visibility = View.GONE
                binding.recycleview.visibility = View.GONE
                binding.recycleview1.visibility = View.VISIBLE
                binding.recycleview2.visibility = View.GONE
            }

            binding.radioButtonOption3.id -> {
                binding.searchView.visibility = View.GONE
                binding.searchView1.visibility = View.GONE
                binding.searchView2.visibility = View.VISIBLE
                binding.recycleview.visibility = View.GONE
                binding.recycleview1.visibility = View.GONE
                binding.recycleview2.visibility = View.VISIBLE
            }
        }
    }

    private fun DetailAdapter.setDetail(mutableList: List<Address>, displayData: Boolean = true) {
        this.addresslist = mutableList.toMutableList()
        this.displayData = displayData
        for (address in mutableList) {
            fetchLikeStatusFromFirebase(address)
        }
        notifyDataSetChanged()
    }
}