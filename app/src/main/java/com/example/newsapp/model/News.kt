package com.example.newsapp.model

// 返回的JSON映射成实体类
data class NewsResponse(val reason: String, val result: NewsResult, val error_code: Int)

// 上边的result结果
data class NewsResult(val stat: String, val data: List<News>)

// 新闻数据类，result里的list
data class News(val title: String, val date: String, val category: String, val author_name: String,val url: String, val thumbnail_pic_s: String, val thumbnail_pic_s02: String?, val thumbnail_pic_s03: String?)


// 实体类映射的是接口返回的JSON数据类型