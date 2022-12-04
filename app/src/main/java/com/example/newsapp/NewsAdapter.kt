package com.example.newsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class NewsAdapter(private val newsList: List<News>): RecyclerView.Adapter<NewsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val newsView = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(newsView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.title.text = news.title
        holder.content.text = news.content
        // 使用Glide加载图片
        Glide.with(MyApplication.context).load(news.imageURL).into(holder.image)
        holder.itemView.setOnClickListener {
            //TODO(写触发事件，跳转到新闻详细页面)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

}