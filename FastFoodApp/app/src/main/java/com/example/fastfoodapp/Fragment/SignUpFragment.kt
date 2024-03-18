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
import com.example.fastfoodapp.Model.User
import com.example.fastfoodapp.R
import com.example.fastfoodapp.Utils.Utils
import com.example.fastfoodapp.databinding.FragmentSignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = Firebase.database.reference
        firebaseAuth = Firebase.auth

        binding.tvLogin.setOnClickListener {
            var loginFragment = LoginFragment()

            var manager = requireActivity().supportFragmentManager
            var transaction =manager.beginTransaction()
            transaction.replace(R.id.viewPager,loginFragment)
            transaction.setReorderingAllowed(true)
            transaction.commit()
        }

        binding.btnSignup.setOnClickListener {
            var name = binding.etName.text.toString().trim()
            var email = binding.etEmail.text.toString().trim()
            var contact = binding.etContact.text.toString().trim()
            var password = binding.etPassword.text.toString().trim()
            var cPassword = binding.etCPassword.text.toString().trim()

            resetFocus()
            if (name.isEmpty()) {
                showError(binding.etName, "Please enter your name")
            } else if (!Utils.isValidEmail(email)) {
                showError(binding.etEmail, "Enter your email")
            } else if (!Utils.isValidContact(contact)) {
                showError(binding.etContact, "Enter valid contact")
            } else if (!Utils.isValidEmail(email)) {
                showError(binding.etEmail, "Enter valid email address")
            } else if (!Utils.isValidPassword(password)) {
                showError(binding.etPassword, "Please enter valid Password")
            } else if (cPassword != password) {
                showError(binding.etCPassword, "Password Mismatched..")
                binding.etCPassword.setText("")
            } else {
                createAccount(name, email, contact, password)
            }


        }
    }

    private fun resetFocus() {
        binding.etName.setBackgroundResource(R.drawable.edittext_white_bg)
        binding.etEmail.setBackgroundResource(R.drawable.edittext_white_bg)
        binding.etContact.setBackgroundResource(R.drawable.edittext_white_bg)
        binding.etPassword.setBackgroundResource(R.drawable.edittext_white_bg)
        binding.etCPassword.setBackgroundResource(R.drawable.edittext_white_bg)
    }

    private fun showError(editText: EditText, error: String) {
        editText.setBackgroundResource(R.drawable.edittext_red_bg)
        editText.requestFocus()
        Toast.makeText(requireContext(), "$error", Toast.LENGTH_SHORT).show()
    }

    private fun createAccount(
        name: String,
        email: String,
        contact: String,
        password: String
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

            if (it.isSuccessful) {

                var userCredential = it.result?.user

                var user = User(userCredential!!.uid, name, userCredential.email!!,contact)

                databaseReference.child("user-node").child(user.id!!).setValue(user)
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            //Navigate to home
                            Toast.makeText(requireContext(), "Welcome ${user.name}", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(requireContext(),HomeActivity::class.java))
                        }
                    }
            }else{
                //Error
                Toast.makeText(requireContext(), "${it.exception!!.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}