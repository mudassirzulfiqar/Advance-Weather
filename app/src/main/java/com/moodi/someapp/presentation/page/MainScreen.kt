package com.moodi.someapp.presentation.page

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.moodi.someapp.core.common.asTemperature
import com.moodi.someapp.core.location.LocationManager
import com.moodi.someapp.core.location.LocationResult
import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.domain.model.WeatherCondition
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.presentation.viewmodel.UIEvent
import com.moodi.someapp.presentation.viewmodel.WeatherUIState
import com.moodi.someapp.presentation.viewmodel.WeatherViewModel
import com.moodi.someapp.ui.OnLifecycleResume
import com.moodi.someapp.ui.theme.Purple80
import com.moodi.someapp.ui.theme.SomeAppTheme

@SuppressLint("MissingPermission")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel,
    locationManager: LocationManager
) {
    val state = viewModel.state.collectAsState()

    OnLifecycleResume {
        locationManager.getCurrentLocation().collect { result ->
            if (result is LocationResult.Success) {
                viewModel.sendEvent(
                    UIEvent.FetchWeather(
                        result.latitude, result.longitude, WeatherUnit.METRIC
                    )
                )
            } else {
                viewModel.sendEvent(UIEvent.LocationResultFailure((result as LocationResult.Failure).reason))
            }

        }

    }
    MainContent(modifier, state.value)
}

@Composable
fun MainContent(
    modifier: Modifier = Modifier, state: WeatherUIState
) {

    if (state.error != null) {
        ErrorSection(state.error)
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
            style = MaterialTheme.typography.bodyMedium
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
        text = value, style = MaterialTheme.typography.titleMedium
    )

}

@Composable
fun TemperatureSection(
    value: Double, unit: WeatherUnit
) {
    Row {
        Text(
            text = value.asTemperature(), style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = unit.symbol, style = MaterialTheme.typography.labelMedium
        )

    }
}

@Composable
fun ConditionSection(value: WeatherCondition) {
    Text(
        text = value.condition, style = MaterialTheme.typography.bodyMedium, color = Purple80
    )
}

class WeatherStatePreviewProvider : PreviewParameterProvider<WeatherUIState> {
    override val values = sequenceOf(
        WeatherUIState(
            loading = false, weatherData = WeatherAppData(
                temperature = 22.0, condition = WeatherCondition.Cloudy, locationName = "Hilversum"
            ), error = null
        ), WeatherUIState(
            loading = false, weatherData = WeatherAppData(
                temperature = 28.5, condition = WeatherCondition.Sunny, locationName = "Amsterdam"
            ), error = null
        ), WeatherUIState(
            loading = true, weatherData = null, error = null
        ), WeatherUIState(
            loading = false, weatherData = null, error = "Unable to fetch weather data"
        )
    )
}

@Preview(showBackground = true, name = "Weather States Preview")
@Composable
fun PreviewMain(@PreviewParameter(WeatherStatePreviewProvider::class) state: WeatherUIState) {
    SomeAppTheme {
        MainContent(state = state)
    }
}