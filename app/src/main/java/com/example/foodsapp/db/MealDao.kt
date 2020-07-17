package com.example.foodsapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodsapp.models.singlemeal.Meal

@Dao
interface MealDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(meal: Meal):Long

    @Delete
    suspend fun deleteMeal(meal: Meal)

    @Query("SELECT * FROM meals_db")
    fun getAllSavedMeals():LiveData<List<Meal>>
}