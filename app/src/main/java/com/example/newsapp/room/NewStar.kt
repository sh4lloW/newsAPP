package com.example.newsapp.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NewStar")
data class NewStar(var user: String, var title: String, var pic1: String
                   ,var date: String, var source: String, var keyID: String,var channelID:String) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

