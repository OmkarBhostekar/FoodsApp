package com.example.foodsapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.*
import com.example.foodsapp.R
import com.example.foodsapp.adapters.FavMealsAdapter
import com.example.foodsapp.adapters.MealsByCategoryAdapter
import com.example.foodsapp.models.mealsbycategory.Meal
import com.example.foodsapp.ui.MainActivity
import com.example.foodsapp.ui.viewmodels.MealViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_favourite.*

class FavouriteFragment: Fragment(R.layout.fragment_favourite) {

    lateinit var viewModel: MealViewModel
    lateinit var adapter: FavMealsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).bottomNavigationView.visibility = View.VISIBLE
        viewModel = (activity as MainActivity).viewModel

        viewModel.getAllSavedMeals().observe(viewLifecycleOwner, Observer { response ->
            val meals = response!!
            rvFavourite.layoutManager = LinearLayoutManager(activity)
            adapter = FavMealsAdapter(meals)
            rvFavourite.adapter = adapter

            adapter.setOnItemClickListener {
                val meal = Meal(it.idMeal,it.strMeal!!,it.strMealThumb!!)
                val bundle = Bundle().apply {
                    putSerializable("MealInfo",meal)
                }
                findNavController().navigate(
                    R.id.action_favouriteFragment_to_mealFragment,
                    bundle
                )
            }
        })

        val itemTouchHelperCallback = object :ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val meal = adapter.meals[position]
                viewModel.deleteMealFromDb(meal)
                Snackbar.make(view,"Removed from favourites",Snackbar.LENGTH_LONG).apply {
                    setAction("UNDO"){
                        viewModel.saveMealToDb(meal)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvFavourite)
    }
}