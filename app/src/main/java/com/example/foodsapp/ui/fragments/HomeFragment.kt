package com.example.foodsapp.ui.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodsapp.R
import com.example.foodsapp.adapters.CategoriesAdapter
import com.example.foodsapp.models.categories.Categories
import com.example.foodsapp.other.Resource
import com.example.foodsapp.ui.MainActivity
import com.example.foodsapp.ui.viewmodels.MealViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment: Fragment(R.layout.fragment_home) {

    lateinit var viewModel: MealViewModel
    lateinit var categories: Categories
    lateinit var categoriesAdapter: CategoriesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        var job: Job? = null
        (activity as MainActivity).bottomNavigationView.visibility = View.VISIBLE
        val gridLayout = GridLayoutManager(activity,3,GridLayoutManager.VERTICAL,false)
        rvCategories.layoutManager = gridLayout
        (activity as MainActivity).setSupportActionBar(toolbar_home)
        (activity as MainActivity).supportActionBar!!.title = ""
        setHasOptionsMenu(true)


        etSearch.addTextChangedListener { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(1000L)
                editable?.let {
                    if (editable.toString().isNotEmpty()){
                        Toast.makeText(activity, "Search is not implemented", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        viewModel.categoriesLiveData.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    categories = response.data!!
                    categoriesAdapter = CategoriesAdapter(categories.categories)
                    rvCategories.adapter = categoriesAdapter
                    categoriesAdapter.setOnItemClickListener {
                        val bundle = Bundle().apply {
                            putSerializable("Category",it)
                        }
                        findNavController().navigate(
                            R.id.action_homeFragment_to_categoryMealsFragment,
                            bundle
                        )
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {

                }
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.miAbout ->{
                MaterialAlertDialogBuilder(context!!).apply {
                    setTitle("ABOUT")
                    setView(R.layout.alert_content)
                    setPositiveButton("OK"){ dialog: DialogInterface, i: Int ->
                        dialog.dismiss()
                    }
                    background = resources.getDrawable(R.drawable.dialog_bg)
                    show()
                }
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }
}