package com.example.foodsapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.foodsapp.R
import com.example.foodsapp.models.singlemeal.Meal
import com.example.foodsapp.other.Resource
import com.example.foodsapp.ui.MainActivity
import com.example.foodsapp.ui.viewmodels.MealViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_random.*
import java.util.*

class RandomFragment: Fragment(R.layout.fragment_random) {

    lateinit var viewModel: MealViewModel
    private var randomMeal: Meal? = null

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).bottomNavigationView.visibility = View.VISIBLE
        viewModel = (activity as MainActivity).viewModel

        viewModel.randomMealLiveData.observe(viewLifecycleOwner, Observer { response ->
           when(response){
                is Resource.Success -> {
                    val meals = response.data!!
                    randomMeal = meals.meals[0]
                    viewRandomMeal.visibility = View.VISIBLE
                    tvMealName.text = randomMeal!!.strMeal
                    tvCategory.text = "CATEGORY:  ${randomMeal!!.strCategory.toString()
                        .toUpperCase(Locale.ROOT)}"
                    tvCountry.text =  "COUNTRY:    ${randomMeal!!.strArea.toString().toUpperCase(
                        Locale.ROOT
                    )}"
                    Glide.with(this).load(randomMeal!!.strMealThumb).into(ivMeal)
                }
               is Resource.Error -> {
                   Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
               }
           }
        })

        viewRandomMeal.setOnClickListener {
            val mealInfo = com.example.foodsapp.models.mealsbycategory.Meal(randomMeal!!.idMeal,"","")
            val bundle = Bundle().apply {
                putSerializable("MealInfo",mealInfo)
            }
            findNavController().navigate(
                R.id.action_randomFragment_to_mealFragment,
                bundle
            )
        }

        btnRandomMeal.setOnClickListener {
            viewModel.randomMeal()
        }
    }
}