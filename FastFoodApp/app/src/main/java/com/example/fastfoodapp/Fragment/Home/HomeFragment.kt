package com.example.fastfoodapp.Fragment.Home

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.fastfoodapp.Activity.OfferList.OfferListActivity
import com.example.fastfoodapp.Activity.LoginSignupActivity
import com.example.fastfoodapp.Adapter.BannerAdapter
import com.example.fastfoodapp.Model.Banner
import com.example.fastfoodapp.R
import com.example.fastfoodapp.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var bannerAdapter: BannerAdapter
    private var bannerList = mutableListOf<Banner>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = Firebase.auth
        var headerView = binding.drawerNavigationMenu.getHeaderView(0)
        var headerUserName = headerView.findViewById<TextView>(R.id.tv_name)
        var headerUserEmail = headerView.findViewById<TextView>(R.id.tv_email)

        var currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            headerUserName.text = currentUser.displayName.toString()
            headerUserEmail.text = currentUser.email

            val isAdmin = checkIfAdmin(currentUser)
            if (isAdmin) {
                // Show admin drawer items
                binding.drawerNavigationMenu.menu.findItem(R.id.add_cuisine).isVisible = true
                binding.drawerNavigationMenu.menu.findItem(R.id.manage_cuisine).isVisible = true
                binding.drawerNavigationMenu.menu.findItem(R.id.manage_product).isVisible = true
            } else {
                // Hide admin drawer items
                binding.drawerNavigationMenu.menu.findItem(R.id.add_cuisine).isVisible = false
                binding.drawerNavigationMenu.menu.findItem(R.id.manage_cuisine).isVisible = false
                binding.drawerNavigationMenu.menu.findItem(R.id.manage_product).isVisible = false
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }
        binding.drawerNavigationMenu.setNavigationItemSelectedListener {
            binding.drawerLayout.close()

            when (it.itemId) {
                R.id.add_cuisine -> {
                    startActivity(Intent(requireContext(), OfferListActivity::class.java))
                    true
                }

                R.id.manage_cuisine -> {
                    startActivity(Intent(requireContext(), OfferListActivity::class.java))
                    true
                }

                R.id.manage_product -> {
                    true
                }

                R.id.nav_logout -> {
                    showLogoutDialog()
                    true
                }

                else -> false
            }
        }

        bannerListData()

        bannerAdapter = BannerAdapter(requireContext(), bannerList, binding.viewPager)
        binding.viewPager.adapter = bannerAdapter

    }

    private fun checkIfAdmin(currentUser: FirebaseUser): Boolean {
        return currentUser.email?.startsWith("admin@") == true
    }

    private fun bannerListData() {
        bannerList.add(Banner(1, R.drawable.home_banner_1))
        bannerList.add(Banner(2, R.drawable.home_banner_2))
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