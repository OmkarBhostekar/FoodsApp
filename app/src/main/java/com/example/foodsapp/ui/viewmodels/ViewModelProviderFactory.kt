package com.example.foodsapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodsapp.repositories.MealRepository

class ViewModelProviderFactory(
    val app: Application,
    val repository: MealRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MealViewModel(app,repository) as T
    }
}