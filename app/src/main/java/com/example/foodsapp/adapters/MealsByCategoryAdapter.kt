package com.example.foodsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodsapp.R
import com.example.foodsapp.models.mealsbycategory.Meal
import kotlinx.android.synthetic.main.item_mealsbycategory.view.*

class MealsByCategoryAdapter(
    val meals: List<Meal>
) : RecyclerView.Adapter<MealsByCategoryAdapter.MealsByCategoryViewHolder>() {

    inner class MealsByCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealsByCategoryViewHolder {
        return MealsByCategoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_mealsbycategory,parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    override fun onBindViewHolder(holder: MealsByCategoryViewHolder, position: Int) {
        val meal = meals[position]
        holder.itemView.apply {
            Glide.with(this).load(meal.strMealThumb).into(ivMealThumbNail)
            tvMealName.text = meal.strMeal
            setOnClickListener {
                onItemClickListener?.let {
                    it(meal)
                }
            }
        }
    }

    private var onItemClickListener: ((Meal) -> Unit)? = null

    fun setOnItemClickListener(listener:(Meal)-> Unit){
        onItemClickListener = listener
    }
}