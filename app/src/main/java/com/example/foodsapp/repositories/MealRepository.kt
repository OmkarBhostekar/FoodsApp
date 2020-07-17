package com.example.foodsapp.repositories

import com.example.foodsapp.api.RetrofitInstance
import com.example.foodsapp.db.MealsDatabase
import com.example.foodsapp.models.singlemeal.Meal

class MealRepository(
    val db: MealsDatabase
) {

    //Api functions
    suspend fun searchMeal(searchQuery: String) = RetrofitInstance.api.searchMeal(searchQuery)

    suspend fun getCategories() = RetrofitInstance.api.getCategories()

    suspend fun mealsByCategories(categoryName: String) = RetrofitInstance.api.mealsByCategory(categoryName)

    suspend fun mealById(mealId: String) = RetrofitInstance.api.mealById(mealId)

    suspend fun randomMeal() = RetrofitInstance.api.randomMeal()

    //Database Functions
    suspend fun saveMealToDb(meal:Meal) = db.getMealDao().upsert(meal)

    suspend fun deleteMealFromDb(meal:Meal) = db.getMealDao().deleteMeal(meal)

    fun getAllSavedMeals() = db.getMealDao().getAllSavedMeals()
}