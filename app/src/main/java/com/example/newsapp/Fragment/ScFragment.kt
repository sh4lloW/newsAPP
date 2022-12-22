package com.example.newsapp.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapp.Adapter.NewsAdapter
import com.example.newsapp.R
import com.example.newsapp.model.News

class scFragment(var newsList: ArrayList<News>) : Fragment() {
    lateinit var newsRecyclerView: RecyclerView
    lateinit var  swipeLayout: SwipeRefreshLayout
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
    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        newsRecyclerView.layoutManager = LinearLayoutManager(this.activity)
        newsRecyclerView.adapter = NewsAdapter(newsList)

        swipeLayout.setOnRefreshListener {
            swipeLayout.isRefreshing = false
            activity?.runOnUiThread {
                // 通知newsList发生变化
                newsRecyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }
}