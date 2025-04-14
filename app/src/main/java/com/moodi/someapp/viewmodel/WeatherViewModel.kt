package com.moodi.someapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moodi.someapp.repository.Resource
import com.moodi.someapp.repository.WeatherAppData
import com.moodi.someapp.repository.WeatherCondition
import com.moodi.someapp.repository.WeatherRepository
import com.moodi.someapp.repository.WeatherUnit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

sealed class UIEvent {
    data class FetchWeather(val lat: Double, val lng: Double) : UIEvent()
}

data class WeatherUIState(
    val loading: Boolean = false, val weatherData: WeatherAppData? = null, val error: String? = null
)


class WeatherViewModel(val weatherRepository: WeatherRepository) : ViewModel() {

    private val _state = mutableStateOf(WeatherUIState())
    val state get() = _state.value


    fun sendEvent(event: UIEvent) {
        when (event) {
            is UIEvent.FetchWeather -> fetchWeather(event.lat, event.lng)
        }
    }

    private fun fetchWeather(lat: Double, lng: Double) {
        viewModelScope.launch {
            weatherRepository.getWeather(lat, lng).collect {
                when (it) {
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            loading = false, error = it.message
                        )
                    }

                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            loading = true
                        )
                    }

                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            loading = false, weatherData = it.data
                        )
                    }
                }
            }

        }
    }
}