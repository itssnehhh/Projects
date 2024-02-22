package com.example.indianpostoffice.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.indianpostoffice.adapter.DetailAdapter
import com.example.indianpostoffice.databinding.FragmentAreaSearchBinding
import com.example.indianpostoffice.model.Address
import com.example.indianpostoffice.model.State
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AreaSearchFragment : Fragment() {

    private lateinit var binding: FragmentAreaSearchBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var detailAdapter: DetailAdapter

    private var stateList = mutableListOf<State>()
    private var addressList = mutableListOf<Address>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAreaSearchBinding.inflate(inflater, container, false)
        return binding.root

        databaseReference = Firebase.database.reference

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseReference = Firebase.database.reference

        detailAdapter = DetailAdapter(requireContext(),addressList,databaseReference,false){itemId ->

        }
        binding.recyclerview.adapter = detailAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        databaseReference.child("state").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                stateList.clear()

                for (snap in snapshot.children){
                    var state = snap.getValue(State::class.java)
                    state?.let {
                        stateList.add(it)
                    }
                }

                val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,stateList)
                binding.spState.adapter = adapter

                binding.spState.onItemSelectedListener = object : OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        var state = stateList[position]
                        loadDistricts(state)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.search.setOnClickListener {
            val selectedDistrict = binding.spDistrict.selectedItem.toString()
            val selectedCity = binding.spCity.selectedItem.toString()
            val filteredCity = addressList.filter { it.district == selectedDistrict || it.city == selectedCity}

            detailAdapter.setDetail(filteredCity,true)
        }

        binding.clear.setOnClickListener {
            binding.spState.setSelection(0)
            binding.spDistrict.setSelection(0)
            binding.spCity.setSelection(0)

            detailAdapter.setDetail(emptyList(),false)
        }

    }

    private fun loadDistricts(state: State) {
        var districts = state.districts as List<String>

        var districtAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,districts)
        binding.spDistrict.adapter = districtAdapter
        districtAdapter.notifyDataSetChanged()

        binding.spDistrict.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var district = districts[position]
                loadCity(district)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun loadCity(district: String) {
        addressList.clear()

        databaseReference.child("address").orderByChild("district").equalTo(district).addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val uniqueCities  = mutableSetOf<String>()

                    for (snap in snapshot.children){
                        var address = snap.getValue(Address::class.java)
                        address?.let {
                            if (uniqueCities.add(it.city!!))
                                addressList.add(it)
                        }
                    }
                    val filteredCities = addressList.filter { it.district == district }

                    var cityAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,filteredCities.map { it.city })
                    binding.spCity.adapter = cityAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }
        )
    }

    private fun DetailAdapter.UpdateList(mutableList: List<Address>,displayData: Boolean = true) {
        this.addresslist = mutableList.toMutableList()
        this.displayData=displayData

        notifyDataSetChanged()
    }
}