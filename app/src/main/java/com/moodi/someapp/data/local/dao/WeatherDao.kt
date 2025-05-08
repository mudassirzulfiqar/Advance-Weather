package com.moodi.someapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.moodi.someapp.data.local.entity.WeatherEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather")
    fun getLatestWeather(): Flow<List<WeatherEntity>>

    @Insert
    fun insertAll(vararg weatherEntity: WeatherEntity)

    @Delete
    fun delete(weatherEntity: WeatherEntity)

    @Query("DELETE FROM weather")
    fun deleteAll()
}