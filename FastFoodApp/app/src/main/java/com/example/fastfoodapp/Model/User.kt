package com.example.fastfoodapp.Model

data class User(
    var id: String? = "",
    var name: String? = "",
    var email: String? = "",
    var contact : String?= "",
    var address : String ?= "null",
    var image : String ?= "",
//    var password : String,
//    var cPassword : String,
    var createdAt : Long = System.currentTimeMillis()
)
