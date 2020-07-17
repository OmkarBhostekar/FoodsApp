package com.example.foodsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodsapp.R
import com.example.foodsapp.models.categories.Category
import kotlinx.android.synthetic.main.item_layout.view.*

class CategoriesAdapter(
    private val categories:List<Category>
) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>(){

    inner class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout,parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val category = categories[position]
        holder.itemView.apply {
            Glide.with(this).load(category.strCategoryThumb).into(ivCategory)
            tvTitle.text = category.strCategory
            setOnClickListener {
                onItemClickListener?.let {
                    it(category)
                }
            }
        }
    }

    private var onItemClickListener: ((Category) -> Unit)? = null

    fun setOnItemClickListener(listener:(Category)-> Unit){
        onItemClickListener = listener
    }
}