package com.example.foodsapp.ui.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodsapp.models.categories.Categories
import com.example.foodsapp.models.mealsbycategory.MealsByCategories
import com.example.foodsapp.models.singlemeal.Meal
import com.example.foodsapp.models.singlemeal.SingleMeal
import com.example.foodsapp.other.Resource
import com.example.foodsapp.repositories.MealRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class MealViewModel(
    val app: Application,
    private val repository: MealRepository
): AndroidViewModel(app) {

    var categoriesLiveData: MutableLiveData<Resource<Categories>> = MutableLiveData()
    var categoriesResponse: Categories? = null

    var mealsByCategoriesLiveData: MutableLiveData<Resource<MealsByCategories>> = MutableLiveData()
    var mealsByCategoriesResponse: MealsByCategories? = null

    var singleMealLiveData: MutableLiveData<Resource<SingleMeal>> = MutableLiveData()
    var singleMealResponse: SingleMeal? = null

    var randomMealLiveData: MutableLiveData<Resource<SingleMeal>> = MutableLiveData()
    var randomMealResponse: SingleMeal? = null

    init {
        getCategories()
    }


    //  DATABASE OPERATIONS
    fun saveMealToDb(meal:Meal) = viewModelScope.launch {
        repository.saveMealToDb(meal)
    }

    fun deleteMealFromDb(meal:Meal) = viewModelScope.launch {
        repository.deleteMealFromDb(meal)
    }

    fun getAllSavedMeals() = repository.getAllSavedMeals()


    //    API CALLS

    fun getCategories() = viewModelScope.launch {
        safeGetCategoriesCall()
    }

    fun mealsByCategory(category: String) = viewModelScope.launch {
        safeMealsByCategory(category)
    }

    fun mealById(mealId: String) = viewModelScope.launch {
        safeMealByIdCall(mealId)
    }

    fun randomMeal() = viewModelScope.launch {
        safeRandomMealCall()
    }

    private suspend fun safeRandomMealCall() {
        try {
            if (hasInternetConnection()){
                randomMealLiveData.postValue(Resource.Loading())
                val response = repository.randomMeal()
                randomMealLiveData.postValue(handleRandomMealResponse(response))
            }else{
                randomMealLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> randomMealLiveData.postValue(Resource.Error("Network failure"))
                else -> randomMealLiveData.postValue(Resource.Error("Sorry :("))
            }
        }
    }

    private fun handleRandomMealResponse(response: Response<SingleMeal>): Resource<SingleMeal>? {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                randomMealResponse = resultResponse
                return Resource.Success(randomMealResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    //    SAFE API CALLS AND RESPONSE HANDLING

    private suspend fun safeGetCategoriesCall(){
        try {
            if (hasInternetConnection()){
                val response = repository.getCategories()
                categoriesLiveData.postValue(handleCategoriesResponse(response))
            }else{
                categoriesLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t:Throwable){
            when(t){
                is IOException -> categoriesLiveData.postValue(Resource.Error("Network Failure"))
                else -> categoriesLiveData.postValue(Resource.Error("Sorry :("))
            }
        }
    }

    private fun handleCategoriesResponse(response: Response<Categories>): Resource<Categories>? {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                categoriesResponse = resultResponse
                return Resource.Success(categoriesResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private suspend fun safeMealsByCategory(category: String){
        try {
            if (hasInternetConnection()){
                val response = repository.mealsByCategories(category)
                mealsByCategoriesLiveData.postValue(handleMealsByCategoryResponse(response))
            }else{
                mealsByCategoriesLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> mealsByCategoriesLiveData.postValue(Resource.Error("Network Failure"))
                else -> mealsByCategoriesLiveData.postValue(Resource.Error("Sorry :("))
            }
        }
    }

    private fun handleMealsByCategoryResponse(response: Response<MealsByCategories>): Resource<MealsByCategories>? {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                mealsByCategoriesResponse = resultResponse
                return Resource.Success(mealsByCategoriesResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private suspend fun safeMealByIdCall(mealId: String){
        try {
            if (hasInternetConnection()){
                val response = repository.mealById(mealId)
                singleMealLiveData.postValue(handleMealByIdResponse(response))
            }else{
                singleMealLiveData.postValue(Resource.Error("No Internet Connection"))
            }
        }catch (t: Throwable){
            when(t){
                is IOException -> singleMealLiveData.postValue(Resource.Error("Network Failure"))
                else -> singleMealLiveData.postValue(Resource.Error("Sorry :("))
            }
        }
    }

    private fun handleMealByIdResponse(response: Response<SingleMeal>): Resource<SingleMeal>? {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                singleMealResponse = resultResponse
                return Resource.Success(singleMealResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    private fun hasInternetConnection():Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork?:return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?: return false
            return when{
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type){
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}