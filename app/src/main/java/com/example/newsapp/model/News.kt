package com.example.newsapp.model

// 返回的JSON映射成实体类
data class NewsResponse(val code:Int, val msg:String, val result: NewsResult)

// 上边的result结果
data class NewsResult(val curpage:Int, val allnum:Int, val newslist:List<News>)

// 新闻数据类，result里的list
data class News(val id:String, val ctime:String, val title:String, val description:String, val source:String, val picUrl:String, val url:String)


// 实体类映射的是接口返回的JSON数据类型