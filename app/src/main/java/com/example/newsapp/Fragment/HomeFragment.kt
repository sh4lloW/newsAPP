package com.example.newsapp.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapp.Adapter.NewsAdapter
import com.example.newsapp.R
import com.example.newsapp.model.News
import com.example.newsapp.model.NewsResponse
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread

class HomeFragment(private var newType: Int) : Fragment() {
    private val newsList = ArrayList<News>()
    private lateinit var newsRecyclerView:RecyclerView
    private lateinit var  swipeLayout:SwipeRefreshLayout
    @SuppressLint("InflateParams")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, null)
        swipeLayout = view.findViewById(R.id.swipeLayout)
        newsRecyclerView = view.findViewById(R.id.news_RecyclerView)
        return view
    }
    // 刷新新闻，重新发送网络请求
    @SuppressLint("NotifyDataSetChanged")
    private fun refresh() {
        // 网络请求放在主线程里会报错，这里创一个子线程
        thread {
            // 天行数据：综合新闻API接口
            val request = Request.Builder()
                .url("https://apis.tianapi.com/allnews/index?key=ff9f5806d6202b2a71827c98ad0ecc47&num=10&col=$newType")
                .build()
            val response = OkHttpClient().newCall(request).execute()
            val json = response.body.string()
            val newsResponse = Gson().fromJson(json, NewsResponse::class.java)
            if (newsResponse?.result != null) {
                val data = newsResponse.result.newslist
                // 先清空再重新塞
                newsList.clear()
                newsList.addAll(data)
                activity?.runOnUiThread {
                    // 通知newsList发生变化
                    newsRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        newsRecyclerView.layoutManager = LinearLayoutManager(this.activity)
        newsRecyclerView.adapter = NewsAdapter(newsList)
        refresh()
        swipeLayout.setOnRefreshListener {
            swipeLayout.isRefreshing = false
            Toast.makeText(activity, "刷新成功", Toast.LENGTH_SHORT).show()
            refresh()
        }
    }
}