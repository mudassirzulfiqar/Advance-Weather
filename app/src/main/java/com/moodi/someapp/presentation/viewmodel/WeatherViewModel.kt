package com.moodi.someapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moodi.someapp.core.common.Resource
import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class UIEvent {
    data class FetchWeather(
        val lat: Double,
        val lng: Double,
        val unit: WeatherUnit,
    ) : UIEvent()

    data class LocationResultFailure(val message: String) : UIEvent()
}

data class WeatherUIState(
    val loading: Boolean = false,
    val weatherData: WeatherAppData? = null,
    val unit: WeatherUnit = WeatherUnit.METRIC,
    val error: String? = null
)


class WeatherViewModel(val weatherRepository: WeatherRepository) : ViewModel() {

    private val _state = MutableStateFlow(WeatherUIState())
    val state get() = _state.asStateFlow()

    fun sendEvent(event: UIEvent) {
        when (event) {
            is UIEvent.FetchWeather -> fetchWeather(event.lat, event.lng, event.unit)
            is UIEvent.LocationResultFailure -> _state.update { state ->
                state.copy(
                    error = event.message
                )
            }
        }
    }

    private fun fetchWeather(lat: Double, lng: Double, unit: WeatherUnit) {
        viewModelScope.launch {
            weatherRepository.getWeather(lat, lng, unit).collect {
                when (it) {
                    is Resource.Error -> {
                        _state.update { state ->
                            state.copy(
                                loading = false,
                                error = it.message
                            )
                        }

                    }

                    is Resource.Loading -> {
                        _state.update { state ->
                            state.copy(
                                loading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _state.update { state ->
                            state.copy(
                                loading = false,
                                weatherData = it.data,
                                unit = unit
                            )
                        }
                    }
                }
            }

        }
    }
}