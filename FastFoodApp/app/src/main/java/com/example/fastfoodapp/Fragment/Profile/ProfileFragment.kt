package com.example.fastfoodapp.Fragment.Profile

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fastfoodapp.Activity.LoginSignupActivity
import com.example.fastfoodapp.Adapter.ProfileAdapter
import com.example.fastfoodapp.Model.User
import com.example.fastfoodapp.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var profileAdapter: ProfileAdapter

    private var userList = mutableListOf<User>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = Firebase.auth
        databaseReference = Firebase.database.reference

        profileAdapter = ProfileAdapter(requireContext(), userList)
        binding.rvProfile.adapter = profileAdapter
        binding.rvProfile.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProfile.setHasFixedSize(true)


        var currentUserUid = firebaseAuth.currentUser?.uid

        if (currentUserUid != null) {
            databaseReference.child("user-node").orderByChild("id").equalTo(currentUserUid)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snap in snapshot.children) {
                            var user = snap.getValue(User::class.java)
                            user?.let {
                                userList.add(it)
                            }
                        }
                        profileAdapter.setItem(userList)

                        Log.d("ProfileFragment", "UserList size: ${userList.size}")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }

        binding.tvSendFeedback.setOnClickListener {
            Toast.makeText(requireContext(), "Feedback Text Click", Toast.LENGTH_SHORT).show()
        }
        binding.tvReport.setOnClickListener {
            Toast.makeText(requireContext(), "Report Text Click", Toast.LENGTH_SHORT).show()
        }
        binding.tvRate.setOnClickListener {
            Toast.makeText(requireContext(), "Rating Text Click", Toast.LENGTH_SHORT).show()
        }
        binding.tvLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        var builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Logout")
            .setMessage("Are you sure you want to logout your account ?")
            .setPositiveButton("YES", DialogInterface.OnClickListener { dialog, which ->
                logout()
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->

            })
        var dialog = builder.create()
        dialog.show()
    }

    private fun logout() {
        firebaseAuth.signOut()
        val intent = Intent(requireContext(), LoginSignupActivity::class.java)
        // Add the following flags to clear the back stack
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

    }
}