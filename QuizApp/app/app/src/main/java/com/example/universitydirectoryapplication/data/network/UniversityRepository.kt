package com.example.universitydirectoryapplication.data.network

import com.example.universitydirectoryapplication.data.model.University
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UniversityRepository @Inject constructor(private val apiService: UniversityApiService){
    suspend fun getUniversityByCountry(country:String):List<University>{
        return apiService.getUniversityList(country)
    }
}