package com.dummyapp.myapplication.repository

import com.dummyapp.myapplication.network.ApiService
import com.dummyapp.myapplication.network.Planet
import retrofit2.Response


class PlanetRepository(private val apiService: ApiService) {
    suspend fun getPlanets(planetNumber: String): Response<Planet> {
        return apiService.getPlanetsDetails(planetNumber)
    }
}