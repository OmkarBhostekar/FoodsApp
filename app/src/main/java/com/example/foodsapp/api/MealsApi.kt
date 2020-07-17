package com.example.foodsapp.api

import com.example.foodsapp.models.categories.Categories
import com.example.foodsapp.models.mealsbycategory.MealsByCategories
import com.example.foodsapp.models.singlemeal.SingleMeal
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealsApi {

    @GET("/api/json/v1/1/search.php")
    suspend fun searchMeal(
        @Query("s")
        searchQuery: String
    ) : Response<SingleMeal>


    @GET("/api/json/v1/1/categories.php")
    suspend fun getCategories() : Response<Categories>


    @GET("/api/json/v1/1/filter.php")
    suspend fun mealsByCategory(
        @Query("c")
        categoryName: String
    ):Response<MealsByCategories>


    @GET("/api/json/v1/1/lookup.php")
    suspend fun mealById(
        @Query("i")
        mealId: String
    ):Response<SingleMeal>


    @GET("/api/json/v1/1/random.php")
    suspend fun randomMeal(): Response<SingleMeal>
}