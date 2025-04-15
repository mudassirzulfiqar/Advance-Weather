package com.moodi.someapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.moodi.someapp.core.location.FakeLocationClient
import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.domain.model.WeatherCondition
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.domain.remote.client.RemoteClientImpl
import com.moodi.someapp.domain.remote.api.WeatherApiClient
import com.moodi.someapp.domain.remote.client.client
import com.moodi.someapp.domain.remote.dto.WeatherDto
import com.moodi.someapp.domain.remote.dto.WeatherRequest
import com.moodi.someapp.ui.theme.Purple80
import com.moodi.someapp.ui.theme.SomeAppTheme
import com.moodi.someapp.util.Result
import com.moodi.someapp.util.asTemperature
import com.moodi.someapp.viewmodel.WeatherUIState


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

                    val result = remember { mutableStateOf<WeatherDto?>(null) }
                    val error = remember { mutableStateOf<Boolean>(false) }

                    LaunchedEffect(true) {
                        val weatherResult = weatherApiClient.getWeather(
                            WeatherRequest(
                                location = locationManager.getCurrentLocation()
                            )
                        )
                        if (weatherResult is Result.Failure) {
                            error.value = true
                        } else {
                            result.value = (weatherResult as Result.Success).data
                        }
                    }

                    MainScreen(
                        modifier = Modifier.padding(innerPadding), state = WeatherUIState(
                            loading = false,
                            WeatherAppData(
                                22.0, WeatherCondition.Snow, WeatherUnit.METRIC
                            ),
                            error = null,
                        )
                    )
                }
            }
        }
    }

}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier, state: WeatherUIState
) {
    if (state.error != null || state.weatherData == null) {
        ErrorSection("Something went wrong")
    } else if (state.loading) {
        LoadingSection()
    } else Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            LocationSection("Hilversum")
            Spacer(modifier = Modifier.padding(top = 2.dp, bottom = 2.dp))
            ChanceOfRainSection(12)
            Spacer(modifier = Modifier.padding(top = 2.dp, bottom = 2.dp))
            TemperatureSection(state.weatherData.temperature, state.weatherData.unit)
            Spacer(modifier = Modifier.padding(top = 2.dp, bottom = 2.dp))
            ConditionSection(state.weatherData.condition)
        }

    }
}

@Composable
fun LoadingSection() {
    Box {
        CircularProgressIndicator()
    }

}

@Composable
fun ErrorSection(error: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = error,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight(300))
        )
    }

}

@Composable
fun ChanceOfRainSection(value: Int) {
    Text(
        text = "Chance of rain : $value%"
    )
}

@Composable
fun LocationSection(value: String) {
    Text(
        text = value, style = TextStyle(
            fontSize = 20.sp, fontWeight = FontWeight(300)
        )
    )

}

@Composable
fun TemperatureSection(
    value: Double, unit: WeatherUnit
) {
    Row {
        Text(
            text = value.asTemperature(), style = TextStyle(
                fontSize = 80.sp, fontWeight = FontWeight(800)
            )
        )
        Text(
            text = unit.symbol, style = TextStyle(
                fontSize = 14.sp, fontWeight = FontWeight(300)
            )
        )

    }
}

@Composable
fun ConditionSection(value: WeatherCondition) {
    Text(
        text = value.condition, style = TextStyle(
            fontSize = 18.sp, fontWeight = FontWeight(600), color = Purple80
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMain() {
    SomeAppTheme {
        MainScreen(
            state = WeatherUIState(
                loading = false,
                WeatherAppData(
                    temperature = 22.0,
                    condition = WeatherCondition.Cloudy,
                    unit = WeatherUnit.METRIC
                ),
                error = null,
            )
        )
    }
}

