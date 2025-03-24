package com.example.weatherapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppScreen(weatherViewModel)
        }
    }
}

@Composable
fun WeatherAppScreen(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("London") }
    val weather by viewModel.weatherData.observeAsState()
    val error by viewModel.error.observeAsState()

    // Fetch weather when screen loads
    LaunchedEffect(Unit) {
        viewModel.fetchWeather(city, "YOUR_API_KEY")
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Weather App", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter City") }
        )

        Button(onClick = {
            viewModel.fetchWeather(city, "4bc79beabb893f411514bc24ca9c9de6")
        }) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(8.dp))

        weather?.let { data ->
            Text("City: ${data.name}")
            Text("Temperature: ${data.main.temp}°C")
            Text("Description: ${data.weather.firstOrNull()?.description ?: "No description"}")
        }

        error?.let {
            Snackbar(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(it)
            }
        }
    }
}