package com.moodi.someapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val uId: Int = 0,
    @ColumnInfo(name = "temperature") val temperature: Double?,
    @ColumnInfo(name = "location") val location: String?,
    @ColumnInfo(name = "condition") val condition: String?,
    @ColumnInfo(name = "timestamp") val timestamp: Long,
)