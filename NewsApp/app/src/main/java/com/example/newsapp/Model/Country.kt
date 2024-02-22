package com.example.newsapp.Model

data class Country(
    var id: Int,
    var countryName : String,
    var countryCode :String
){
    override fun toString(): String {
        return countryCode
    }
}
