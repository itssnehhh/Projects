package com.example.fastfoodapp.Preference

import android.content.Context
import android.content.SharedPreferences

class PrefManager(context: Context) {

    private val PREF_NAME = "com.example.fastfoodapp"
    private val KEY_ONBOARDING = "on_boarding"

    private var preferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private var editor = preferences.edit()

    fun updateOnBoardingStatus(status:Boolean){
        editor.putBoolean(KEY_ONBOARDING,status)
        editor.commit()
    }

    fun getOnBoardingStatus():Boolean{
        return preferences.getBoolean(KEY_ONBOARDING,false)
    }

}