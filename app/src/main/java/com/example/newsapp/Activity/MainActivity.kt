package com.example.newsapp.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapp.*
import com.example.newsapp.Adapter.NewsAdapter
import com.example.newsapp.model.News
import com.example.newsapp.model.NewsResponse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private val newsList = ArrayList<News>()
    lateinit var newsRecyclerView: RecyclerView

    // 刷新新闻，重新发送网络请求
    @SuppressLint("NotifyDataSetChanged")
    private fun refresh() {
        // 网络请求放在主线程里会报错，这里创一个子线程
        thread {
            // 天行数据：综合新闻API接口
            val request = Request.Builder().url("https://apis.tianapi.com/generalnews/index?key=" + "ff9f5806d6202b2a71827c98ad0ecc47").build()
            val response = OkHttpClient().newCall(request).execute()
            val json = response.body?.string()
            val newsResponse = Gson().fromJson(json, NewsResponse::class.java)
            if (newsResponse?.result != null) {
                val data = newsResponse.result.newslist
                // 先清空再重新塞
                newsList.clear()
                newsList.addAll(data)
                runOnUiThread {
                    // 通知newsList发生变化
                    newsRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        newsRecyclerView =  findViewById<RecyclerView>(R.id.news_RecyclerView)
        val swipeLayout = findViewById<SwipeRefreshLayout>(R.id.swipeLayout)
        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        newsRecyclerView.adapter = NewsAdapter(newsList)
        refresh()
        swipeLayout.setOnRefreshListener{
            swipeLayout.isRefreshing = false
            Toast.makeText(this@MainActivity, "刷新成功", Toast.LENGTH_SHORT).show()
            refresh()
        }

    }




}