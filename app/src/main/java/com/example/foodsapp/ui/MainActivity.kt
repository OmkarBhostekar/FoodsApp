package com.example.foodsapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodsapp.R
import com.example.foodsapp.db.MealsDatabase
import com.example.foodsapp.repositories.MealRepository
import com.example.foodsapp.ui.viewmodels.MealViewModel
import com.example.foodsapp.ui.viewmodels.ViewModelProviderFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mealRepository = MealRepository(MealsDatabase(this))
        val viewModelProviderFactory = ViewModelProviderFactory(application,mealRepository)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(MealViewModel::class.java)

        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
    }
}