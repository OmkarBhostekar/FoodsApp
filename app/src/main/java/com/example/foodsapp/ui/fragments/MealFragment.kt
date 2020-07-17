package com.example.foodsapp.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.foodsapp.R
import com.example.foodsapp.models.mealsbycategory.Meal
import com.example.foodsapp.models.singlemeal.SingleMeal
import com.example.foodsapp.other.Resource
import com.example.foodsapp.ui.MainActivity
import com.example.foodsapp.ui.viewmodels.MealViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_meal.*

class MealFragment: Fragment(R.layout.fragment_meal) {

    lateinit var mealInfo: Meal
    lateinit var meal: com.example.foodsapp.models.singlemeal.Meal
    lateinit var viewModel: MealViewModel
    var ingredients: String = ""
    var measures: String = ""
    private val args: MealFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mealInfo = args.MealInfo
        viewModel = (activity as MainActivity).viewModel
        viewModel.mealById(mealInfo.idMeal)

        (activity as AppCompatActivity).setSupportActionBar(toolbar_meal)
        (activity as AppCompatActivity).supportActionBar!!.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
        toolbar_meal.setNavigationOnClickListener { (activity as MainActivity).onBackPressed() }

        viewModel.singleMealLiveData.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    meal = response.data!!.meals[0]
                    Glide.with(this).load(meal.strMealThumb).into(ivMealImage)
                    (activity as MainActivity).supportActionBar!!.title = meal.strMeal
                    tvCategory.text = meal.strCategory
                    tvCountry.text = meal.strArea
                    tvInstructions.text = meal.strInstructions
                    allIngredients()
                    tvIngredient.text = ingredients
                    tvMeasure.text = measures

                    btnSource.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meal.strSource))
                        (activity as MainActivity).startActivity(intent)
                    }

                    btnYoutube.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(meal.strYoutube))
                        (activity as MainActivity).startActivity(intent)
                    }
                    btnAddToFav.setOnClickListener {
                        viewModel.saveMealToDb(meal)
                        Snackbar.make(view,"Added to Favourites",Snackbar.LENGTH_LONG).apply {
                            setAction("UNDO"){
                                viewModel.deleteMealFromDb(meal)
                            }
                            show()
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {}
            }
        })
    }

    private fun allIngredients(){
        meal.apply {
            addString(strIngredient1,strMeasure1)
            addString(strIngredient2,strMeasure2)
            addString(strIngredient3,strMeasure3)
            addString(strIngredient4,strMeasure4)
            addString(strIngredient5,strMeasure5)
            addString(strIngredient6,strMeasure6)
            addString(strIngredient7,strMeasure7)
            addString(strIngredient8,strMeasure8)
            addString(strIngredient9,strMeasure9)
            addString(strIngredient10,strMeasure10)
            addString(strIngredient11,strMeasure11)
            addString(strIngredient12,strMeasure12)
            addString(strIngredient13,strMeasure13)
            addString(strIngredient14,strMeasure14)
            addString(strIngredient15,strMeasure15)
            addString(strIngredient16,strMeasure16)
            addString(strIngredient17,strMeasure17)
            addString(strIngredient18,strMeasure18)
            addString(strIngredient19,strMeasure19)
            addString(strIngredient20,strMeasure20)
        }
    }

    private fun addString(str: String?,m:String?) {
        if (str != "" && str != null && m != null){
            if (m.length > 35){
                measures = measures + m + System.lineSeparator()
                if (str.length > 17){
                    ingredients = ingredients + str + System.lineSeparator() + System.lineSeparator()
                }else{
                    ingredients = ingredients + str + System.lineSeparator()+ System.lineSeparator()+ System.lineSeparator()
                }
            }else{
                if (str.length > 17){
                    ingredients = ingredients + str +System.lineSeparator()
                    measures = if (m.length > 17 ){
                        measures + m + System.lineSeparator()
                    }else{
                        measures + m + System.lineSeparator()+ System.lineSeparator()
                    }
                }else{
                    if (m.length > 17){
                        ingredients = ingredients + str + System.lineSeparator()+ System.lineSeparator()
                        measures = measures + m + System.lineSeparator()
                    }else{
                        ingredients = ingredients + str + System.lineSeparator()+ System.lineSeparator()
                        measures = measures + m + System.lineSeparator()+ System.lineSeparator()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar!!.title = ""
        tvCategory.text = "---"
        tvCountry.text = "---"
        tvInstructions.text = "---"
        ingredients =""
        measures = ""
    }
}