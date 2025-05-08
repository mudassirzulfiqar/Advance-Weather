package com.moodi.someapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.moodi.someapp.data.local.dao.WeatherDao
import com.moodi.someapp.data.local.entity.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}


fun provideWeatherDatabase(applicationContext: Context): WeatherDao {
    return Room.databaseBuilder(
        applicationContext,
        WeatherDatabase::class.java, "weather-database"
    ).build().weatherDao()
}