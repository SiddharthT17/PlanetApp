package com.dummyapp.myapplication.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dummyapp.myapplication.network.Planet
import com.dummyapp.myapplication.repository.PlanetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlanetViewModel(private val repository: PlanetRepository) : ViewModel() {

    private val _planets = MutableStateFlow(Planet("", "", emptyList()))
    val planet = _planets.asStateFlow()

    var progress by mutableStateOf(false)

    init {
        getPlanetDetails("1")
    }
    fun getPlanetDetails(planets: String) {
        progress = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getPlanets(planets)
            val planet = response.body()
            Log.d("response","planet res:$planet")
            planet?.let {
                _planets.value = Planet(planet.name, planet.climate, planet.films)
            }
            progress = false
        }
    }
}

class PlanetViewModelFactory(private val repository: PlanetRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PlanetViewModel::class.java)) {
            return PlanetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}