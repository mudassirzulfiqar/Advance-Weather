package com.moodi.someapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.moodi.someapp.core.location.LocationManager
import com.moodi.someapp.presentation.page.MainScreen
import com.moodi.someapp.ui.theme.SomeAppTheme
import com.moodi.someapp.presentation.viewmodel.WeatherViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : ComponentActivity() {

    private val locationManager: LocationManager by inject()
    private val viewModel: WeatherViewModel by viewModel()

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            SomeAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        locationManager = locationManager
                    )
                }
            }
        }
    }

}



