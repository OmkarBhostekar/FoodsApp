package com.example.foodsapp.models.mealsbycategory

import java.io.Serializable

data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
) : Serializable