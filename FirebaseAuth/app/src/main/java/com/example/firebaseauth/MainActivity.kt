package com.example.firebaseauth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.firebaseauth.ui.theme.FirebaseAuthTheme
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val mAuth = FirebaseAuth.getInstance()
    var verificationOtp = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseAuthTheme {
                LoginScreen { mobileNum, otp ->
                    if (mobileNum.isNotEmpty()){
                        send(mobileNum)
                    }

                    if (otp.isNotEmpty()){
                        otpVerification(otp)
                    }
                }
            }
        }
    }

    private fun send(mobileNum: String) {
        val option = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber("+91$mobileNum")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    Toast.makeText(applicationContext, "Verification Completed", Toast.LENGTH_SHORT).show()
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(applicationContext, "Verification Failed", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(otp: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(otp, p1)
                    verificationOtp = otp
                    Toast.makeText(applicationContext, "OTP sent successfully", Toast.LENGTH_SHORT).show()
                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(option)
    }

    private fun otpVerification(otp:String){
        val credential = PhoneAuthProvider.getCredential(verificationOtp,otp)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    Toast.makeText(applicationContext, "OTP Verified successfully", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(application, "OTP Verification failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
