package com.example.newsapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(val mContext: Context, name: String,
                       version: Int) : SQLiteOpenHelper(mContext, name, null, version) {
    private val createApp = "create table app (" +
            "id text ," +
            "title text," +
            "source text," +
            "url text," +
            "image text)"
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createApp)
        Toast.makeText(mContext, "Create succeeded",
            Toast.LENGTH_SHORT).show()
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {
        db.execSQL("drop table if exists app")
        onCreate(db)

    }

}