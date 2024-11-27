package com.dummyapp.myapplication.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.dummyapp.myapplication.network.RetrofitClient
import com.dummyapp.myapplication.repository.PlanetRepository
import com.dummyapp.myapplication.viewmodel.PlanetViewModel
import com.dummyapp.myapplication.viewmodel.PlanetViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen() {
    var planets by remember { mutableStateOf("") }
    val planetViewModel = ViewModelProvider(
        LocalContext.current as ViewModelStoreOwner,
        PlanetViewModelFactory(PlanetRepository(RetrofitClient.apiService))
    ).get(PlanetViewModel::class.java)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Search Planet") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                singleLine = true,
                value = planets,
                onValueChange = { planets = it },
                label = { Text("Enter planet") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Button(
                onClick = {
                    if (planets.isNotBlank()) {
                        planetViewModel.getPlanetDetails(planets)
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Get planet")
            }
            PlanetUI(planetViewModel)
        }

    }
}

@Composable
fun PlanetUI(planetViewModel: PlanetViewModel) {
    val planet by planetViewModel.planet.collectAsState()
    if (planet.name.isNotBlank()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            if (planetViewModel.progress) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Name: ${planet.name}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "climate: ${planet.climate}")
                    Spacer(modifier = Modifier.height(8.dp))
                    planet.films.forEach { item ->
                        Text(text = "${item}")
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }
    }
}
