package com.moodi.someapp.core.common

import kotlin.math.roundToInt

fun Double.asTemperature(): String {
    return this.roundToInt().toString()
}