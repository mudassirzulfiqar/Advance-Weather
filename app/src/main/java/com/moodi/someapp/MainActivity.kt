package com.moodi.someapp

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.core.content.ContextCompat
import com.moodi.someapp.core.location.LocationManager
import com.moodi.someapp.core.location.LocationResult
import com.moodi.someapp.domain.model.WeatherAppData
import com.moodi.someapp.domain.model.WeatherCondition
import com.moodi.someapp.domain.model.WeatherUnit
import com.moodi.someapp.ui.page.MainScreen
import com.moodi.someapp.ui.theme.Purple80
import com.moodi.someapp.ui.theme.SomeAppTheme
import com.moodi.someapp.util.asTemperature
import com.moodi.someapp.viewmodel.UIEvent
import com.moodi.someapp.viewmodel.WeatherUIState
import com.moodi.someapp.viewmodel.WeatherViewModel
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

fun showPermissionMessage() {

}


