package com.example.foodsapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodsapp.models.singlemeal.Meal

@Database(entities = [Meal::class],version = 1)
abstract class  MealsDatabase : RoomDatabase(){

    abstract fun getMealDao() :MealDao

    companion object {

        @Volatile
        private var instance: MealsDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MealsDatabase::class.java,
                "meals_db.db"
            ).build()
    }
}