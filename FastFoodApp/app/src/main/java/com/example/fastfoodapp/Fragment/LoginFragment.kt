package com.example.fastfoodapp.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.fastfoodapp.Activity.HomeActivity
import com.example.fastfoodapp.R
import com.example.fastfoodapp.Utils.Utils
import com.example.fastfoodapp.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = Firebase.auth

        binding.btnLogin.setOnClickListener {
            var email = binding.etEmail.text.toString().trim()
            var password = binding.etPassword.text.toString().trim()

            resetFocus()
            if (!Utils.isValidEmail(email)) {
                showError(binding.etEmail, "Enter valid email")
            } else if (!Utils.isValidPassword(password)) {
                showError(binding.etPassword, "Enter valid password")
            } else {
                userLoggedIn(email, password)
            }
        }

        binding.btnForgot.setOnClickListener {

            var forgotPasswordFragment = ForgotPasswordFragment()


            var manager =requireActivity().supportFragmentManager
            var transaction =manager.beginTransaction()
            transaction.replace(R.id.container,forgotPasswordFragment)
            transaction.addToBackStack(null)
            transaction.setReorderingAllowed(true)
            transaction.commit()
        }

        binding.btnFacebook.setOnClickListener {
            Toast.makeText(requireContext(), "Have to watch video of this ", Toast.LENGTH_LONG).show()
        }
        binding.btnGoogle.setOnClickListener {
            Toast.makeText(requireContext(), "Have to watch video of this ", Toast.LENGTH_LONG).show()
        }

    }

    private fun resetFocus() {
        binding.etEmail.setBackgroundResource(R.drawable.edittext_white_bg)
        binding.etPassword.setBackgroundResource(R.drawable.edittext_white_bg)
    }

    private fun showError(editText: EditText, error: String) {
        editText.setBackgroundResource(R.drawable.edittext_red_bg)
        editText.requestFocus()
        Toast.makeText(requireContext(), "$error", Toast.LENGTH_SHORT).show()
    }


    private fun userLoggedIn(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Welcome back !", Toast.LENGTH_SHORT).show()
                startActivity(Intent(requireContext(), HomeActivity::class.java))
            } else {
                Toast.makeText(requireContext(), "${it.exception!!.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}