package com.example.newsapp.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.R

class NewsDetailActivity : AppCompatActivity() {
    // 新闻详细页，直接用WebView打开JSON传过来的URL，URL由intent传过来
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newsdetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val url = intent.getStringExtra("url")
        val webView = findViewById<WebView>(R.id.news_webview)
        // 加这一行是以应用内部打开浏览器，而不是启动手机自带浏览器
        webView.webViewClient = WebViewClient()
        if (url != null) {
             webView.loadUrl(url)
        }
    }

    // 左上角加一个返回按钮，终止本页面并跳回新闻列表
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}