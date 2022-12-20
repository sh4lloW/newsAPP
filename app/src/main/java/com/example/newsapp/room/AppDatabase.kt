package com.example.newsapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapp.app.App

@Database(version = 2, entities = [NewStar::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun NewStarDao(): NewStarDao
    companion object {
        private var instance: AppDatabase? = null
        @Synchronized
        fun getDatabase(context: Context): AppDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                App.context,
                AppDatabase::class.java, "app_database")
                .build().apply {
                    instance = this
                }
        }
    }
}