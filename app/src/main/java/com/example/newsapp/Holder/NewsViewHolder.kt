package com.example.newsapp.Holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R

open class NewsViewHolder(newsView: View): RecyclerView.ViewHolder(newsView) {
    val title: TextView = newsView.findViewById(R.id.news_title)
    val source: TextView = newsView.findViewById(R.id.news_source)
    val image: ImageView = newsView.findViewById(R.id.news_image)
}