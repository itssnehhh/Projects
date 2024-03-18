package com.example.fastfoodapp.Utils

import android.util.Patterns
import java.util.regex.Pattern

class Utils {

    companion object {

        private const val PASSWORD_REGEX =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,20}$"

        fun isValidEmail(email: String) :Boolean{
            return Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isValidPassword(password:String):Boolean{
            return Pattern.matches(PASSWORD_REGEX,password)
        }

        fun isValidContact(contact:String):Boolean{
            return contact.length == 10
        }
    }
}