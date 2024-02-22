package com.example.indianpostoffice.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.indianpostoffice.R
import com.example.indianpostoffice.adapter.DetailAdapter
import com.example.indianpostoffice.databinding.FragmentAreaSearchBinding
import com.example.indianpostoffice.databinding.FragmentSavedRecordBinding
import com.example.indianpostoffice.model.Address
import com.example.indianpostoffice.model.State
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class SavedRecordFragment : Fragment() {

    private lateinit var binding: FragmentSavedRecordBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var detailAdapter: DetailAdapter

    private var addressList = mutableListOf<Address>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSavedRecordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = Firebase.database.reference

        detailAdapter = DetailAdapter(requireContext(),addressList,databaseReference,true){

        }
        binding.recycleview.layoutManager = LinearLayoutManager(requireContext())
        binding.recycleview.adapter = detailAdapter

        detailAdapter.fetchAllLikeStatusFromFirebase()
        for (address in addressList){
            detailAdapter.fetchLikeStatusFromFirebase(address)
        }
        detailAdapter.notifyDataSetChanged()

    }
}