package com.example.movieexplorer.model

import com.google.gson.annotations.SerializedName

data class MovieDetail(
    @SerializedName("Title")
    val title:String,
    @SerializedName("Year")
    val year:String,
    @SerializedName("Rated")
    val rated:String,
    @SerializedName("Released")
    val release:String,
    @SerializedName("Runtime")
    val runtime:String,
    @SerializedName("Genre")
    val genre:String,
    @SerializedName("Director")
    val director:String,
    @SerializedName("Writer")
    val writer:String,
    @SerializedName("Actors")
    val actor:String,
    @SerializedName("Plot")
    val plot:String,
    @SerializedName("Language")
    val language:String,
    @SerializedName("Country")
    val country:String,
    @SerializedName("Awards")
    val awards:String,
    @SerializedName("Poster")
    val poster:String,
    @SerializedName("Ratings")
    val ratings:List<Ratings>,
    @SerializedName("imdbRating")
    val imdbRating:String,
    @SerializedName("imdbID")
    val imdbId:String
)

data class Ratings(
    @SerializedName("Source")
    val source:String,
    @SerializedName("Value")
    val value:String
)
