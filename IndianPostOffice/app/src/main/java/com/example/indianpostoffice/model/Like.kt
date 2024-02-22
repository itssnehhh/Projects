package com.example.indianpostoffice.model

data class Like(
    var pincode: String?= null,
    var isLike: Boolean = false
){
    override fun toString(): String {
        return isLike.toString()
    }
}