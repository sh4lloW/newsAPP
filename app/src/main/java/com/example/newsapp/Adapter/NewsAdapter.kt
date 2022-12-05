package com.example.newsapp.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.Activity.NewsDetailActivity
import com.example.newsapp.MyApplication
import com.example.newsapp.model.News
import com.example.newsapp.Holder.NewsViewHolder
import com.example.newsapp.R

class NewsAdapter(private val newsList: List<News>): RecyclerView.Adapter<NewsViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val newsView = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(newsView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]
        holder.title.text = news.title
        holder.source.text = news.source
        // 使用Glide加载图片
        Glide.with(MyApplication.context).load(news.picUrl).into(holder.image)
        holder.itemView.setOnClickListener {
            val intent = Intent(MyApplication.context, NewsDetailActivity::class.java)
            intent.putExtra("url", newsList[holder.adapterPosition].url)
            //必须加这一行，不然报错Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag.
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(MyApplication.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

}