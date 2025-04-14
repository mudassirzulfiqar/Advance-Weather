package com.moodi.someapp.util

import kotlin.math.roundToInt


fun Double.asTemperature(): String {
    return this.roundToInt().toString()
}