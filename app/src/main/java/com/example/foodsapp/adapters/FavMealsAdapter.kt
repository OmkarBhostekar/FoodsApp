package com.example.foodsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodsapp.R
import com.example.foodsapp.models.singlemeal.Meal
import kotlinx.android.synthetic.main.item_fav_meal.view.*

class FavMealsAdapter(
    val meals: List<Meal>
) : RecyclerView.Adapter<FavMealsAdapter.FavMealsViewHolder>() {

    inner class FavMealsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavMealsViewHolder {
        return FavMealsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_fav_meal,parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavMealsViewHolder, position: Int) {
        val meal = meals[position]
        holder.itemView.apply {
            Glide.with(this).load(meal.strMealThumb).into(ivMeal)
            tvMealName.text = meal.strMeal
            tvCategory.text = "CATEGORY:  ${meal.strCategory.toString().toUpperCase()}"
            tvCountry.text =  "COUNTRY:   ${meal.strArea.toString().toUpperCase()}"
            setOnClickListener {
                onItemClickListener?.let {
                    it(meal)
                }
            }
        }
    }

    private var onItemClickListener: ((Meal) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Meal) -> Unit)){
        onItemClickListener = listener
    }
}