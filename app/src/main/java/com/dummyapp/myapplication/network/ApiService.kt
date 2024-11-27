package com.dummyapp.myapplication.network

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

data class Planet(
    val name: String,
    val climate: String,
    val films: List<String>
)

interface ApiService {
    @GET("api/planets/{planetNumber}")
    suspend fun getPlanetsDetails(
        @Path("planetNumber") planetNumber: String
    ): Response<Planet>
}

object RetrofitClient {
    val apiService: ApiService = Retrofit.Builder()
        .baseUrl("https://swapi.dev/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}