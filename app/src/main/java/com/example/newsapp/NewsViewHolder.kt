package com.example.newsapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

open class NewsViewHolder(newsView: View): RecyclerView.ViewHolder(newsView) {
    val title: TextView = newsView.findViewById(R.id.news_title)
    val content: TextView = newsView.findViewById(R.id.news_content)
    val image: ImageView = newsView.findViewById(R.id.news_image)
}