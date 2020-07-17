package com.example.foodsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodsapp.R
import com.example.foodsapp.adapters.MealsByCategoryAdapter
import com.example.foodsapp.models.categories.Categories
import com.example.foodsapp.models.categories.Category
import com.example.foodsapp.models.mealsbycategory.Meal
import com.example.foodsapp.models.mealsbycategory.MealsByCategories
import com.example.foodsapp.other.Resource
import com.example.foodsapp.ui.MainActivity
import com.example.foodsapp.ui.viewmodels.MealViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_meals_by_category.*

class CategoryMealsFragment: Fragment(R.layout.fragment_meals_by_category) {

    lateinit var viewModel: MealViewModel
    lateinit var meals: MealsByCategories
    lateinit var mealsByCategoryAdapter: MealsByCategoryAdapter
    val args: CategoryMealsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        val category = args.Category
        val categoryName = category.strCategory
        tvCategoryTitle.text = categoryName
        viewModel.mealsByCategory(categoryName)
        val gridLayout = GridLayoutManager(activity,2,GridLayoutManager.VERTICAL,false)
        rvMealsByCategory.layoutManager = gridLayout


        (activity as AppCompatActivity).apply {
            setSupportActionBar(toolbar_mealsbycategory)
            supportActionBar!!.title = ""
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            bottomNavigationView.visibility = View.GONE
        }
        toolbar_mealsbycategory.setNavigationOnClickListener { (activity as MainActivity).onBackPressed() }

        viewModel.mealsByCategoriesLiveData.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    meals = response.data!!
                    mealsByCategoryAdapter = MealsByCategoryAdapter(meals.meals)
                    rvMealsByCategory.adapter = mealsByCategoryAdapter
                    mealsByCategoryAdapter.setOnItemClickListener {
                        val bundle = Bundle().apply {
                            putSerializable("MealInfo",it)
                        }
                        findNavController().navigate(
                            R.id.action_categoryMealsFragment_to_mealFragment,
                            bundle
                        )
                    }
                }
                is Resource.Error ->{}
                is Resource.Loading -> {}
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val defaultList = listOf<Meal>()
        mealsByCategoryAdapter = MealsByCategoryAdapter(defaultList)
        rvMealsByCategory.adapter = mealsByCategoryAdapter
    }
}