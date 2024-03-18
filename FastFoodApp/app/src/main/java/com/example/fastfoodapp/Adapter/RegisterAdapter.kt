package com.example.fastfoodapp.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fastfoodapp.Fragment.LoginFragment
import com.example.fastfoodapp.Fragment.SignUpFragment

class RegisterAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> LoginFragment()
            1 -> SignUpFragment()
            else -> throw IllegalArgumentException("Invalid position : $position")
        }
    }


}