package com.example.fastfoodapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.fastfoodapp.R
import com.example.fastfoodapp.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSendCode.setOnClickListener {
            Toast.makeText(requireContext(), "Code will send on your registered email account", Toast.LENGTH_SHORT).show()
        }

        binding.btnNext.setOnClickListener {
            Toast.makeText(requireContext(), "Navigate to OTP verification page", Toast.LENGTH_SHORT).show()
        }

    }

}