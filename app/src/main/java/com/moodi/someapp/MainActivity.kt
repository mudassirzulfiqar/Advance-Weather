package com.moodi.someapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.location.LocationServices
import com.moodi.someapp.location.FakeLocationClient
import com.moodi.someapp.remote.RemoteClientImpl
import com.moodi.someapp.remote.WeatherApiClient
import com.moodi.someapp.remote.WeatherRequest
import com.moodi.someapp.remote.WeatherResponse
import com.moodi.someapp.remote.client
import com.moodi.someapp.repository.WeatherAppData
import com.moodi.someapp.repository.WeatherCondition
import com.moodi.someapp.repository.WeatherUnit
import com.moodi.someapp.ui.theme.Purple80
import com.moodi.someapp.ui.theme.SomeAppTheme
import com.moodi.someapp.viewmodel.WeatherUIState
import kotlin.math.roundToInt


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationModule = LocationServices.getFusedLocationProviderClient(this)

        val locationManager = FakeLocationClient()

        val weatherApiClient = WeatherApiClient(RemoteClientImpl(client = client))


        enableEdgeToEdge()
        setContent {
            SomeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val result = remember { mutableStateOf<WeatherResponse?>(null) }
                    val error = remember { mutableStateOf<Boolean>(false) }

                    LaunchedEffect(true) {
                        val weatherResult = weatherApiClient.getWeather(
                            WeatherRequest(
                                location = locationManager.getCurrentLocation()
                            )
                        )
                        if (weatherResult is Result.RemoteError || weatherResult is Result.Error) {
                            error.value = true
                        } else {
                            result.value = weatherResult.result
                        }
                    }

//                    MainScreen(modifier = Modifier.padding(innerPadding), )
                }
            }
        }
    }

}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: WeatherUIState
) {
    if (state.error != null || state.weatherData == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Error fetching weather data",
                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight(300))
            )
        }
    } else if (state.loading) {
        Box {
            CircularProgressIndicator()
        }
    } else Box(modifier = modifier.fillMaxSize()) {
        Column {
            Text(
                text = "Hilversum", style = TextStyle(
                    fontSize = 20.sp, fontWeight = FontWeight(300)
                )
            )
            Text(
                text = "Chance of rain : 0%"
            )
            Text(
                text = state.weatherData.temperature.toString(), style = TextStyle(
                    fontSize = 80.sp, fontWeight = FontWeight(800)
                )
            )
            Spacer(modifier = Modifier.padding(top = 2.dp, bottom = 2.dp))
            Text(
                text = state.weatherData.condition.condition, style = TextStyle(
                    fontSize = 18.sp, fontWeight = FontWeight(600), color = Purple80
                )
            )
        }
        Text(
            text = state.weatherData.unit.unit, style = TextStyle(
                fontSize = 14.sp, fontWeight = FontWeight(300)
            )
        )

    }
}

@Preview
@Composable
fun PreviewMain() {
    MainScreen(
        state = WeatherUIState(
            loading = false,
            WeatherAppData(
                22.0,
                WeatherCondition.Snow,
                WeatherUnit.Celcius
            ),
            error = null,
        )
    )
}

