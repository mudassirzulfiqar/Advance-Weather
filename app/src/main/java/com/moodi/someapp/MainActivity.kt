package com.moodi.someapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.moodi.someapp.core.location.FakeLocationClient
import com.moodi.someapp.core.location.LocationManager
import com.moodi.someapp.core.location.LocationResult
import com.moodi.someapp.data.repository.WeatherRepositoryImpl
import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.domain.model.WeatherCondition
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.domain.remote.api.WeatherApiClient
import com.moodi.someapp.domain.remote.client.RemoteClientImpl
import com.moodi.someapp.domain.remote.client.client
import com.moodi.someapp.ui.theme.Purple80
import com.moodi.someapp.ui.theme.SomeAppTheme
import com.moodi.someapp.util.asTemperature
import com.moodi.someapp.viewmodel.UIEvent
import com.moodi.someapp.viewmodel.WeatherUIState
import com.moodi.someapp.viewmodel.WeatherViewModel


class MainActivity : ComponentActivity() {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locationManager = LocationManager(
            LocationServices.getFusedLocationProviderClient(this)
        )

        val viewModel = WeatherViewModel(
            WeatherRepositoryImpl(
                WeatherApiClient(
                    RemoteClientImpl(client = client)
                )
            )
        )


        enableEdgeToEdge()
        setContent {
            SomeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val state = viewModel.state.collectAsState()

                    LaunchedEffect(true) {
                        // TODO: add location Permission Check
                        val locationPermissionResult = locationManager.getCurrentLocation()
                        if (locationPermissionResult is LocationResult.Success) {
                            viewModel.sendEvent(
                                UIEvent.FetchWeather(
                                    locationPermissionResult.latitude,
                                    locationPermissionResult.longitude,
                                    WeatherUnit.METRIC
                                )
                            )

                        } else {
                            viewModel.sendEvent(
                                UIEvent.FetchWeather(
                                    0.00, 0.00,
                                    WeatherUnit.METRIC
                                )
                            )
                        }

                    }

                    MainScreen(
                        modifier = Modifier.padding(innerPadding), state = state.value
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
    if (state.error != null) {
        ErrorSection("Something went wrong")
    } else if (state.loading || state.weatherData == null) {
        LoadingSection()
    } else Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            LocationSection(state.weatherData.locationName)
            Spacer(modifier = Modifier.padding(top = 2.dp, bottom = 2.dp))
            ChanceOfRainSection(12)
            Spacer(modifier = Modifier.padding(top = 2.dp, bottom = 2.dp))
            TemperatureSection(state.weatherData.temperature, state.unit)
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
                    locationName = "Hilversum"
                ),
                error = null,
            )
        )
    }
}

