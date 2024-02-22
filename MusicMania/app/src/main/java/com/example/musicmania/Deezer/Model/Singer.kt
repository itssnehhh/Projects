package com.example.musicmania.Deezer.Model

data class Singer(
    var id : Int,
    var name :String
){
    override fun toString(): String {
        return name
    }
}
