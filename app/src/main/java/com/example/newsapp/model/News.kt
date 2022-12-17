package com.example.newsapp.model


//
//// 返回的JSON映射成实体类
//data class NewsResponse(val code:Int, val msg:String, val result: NewsResult)
//
//// 上边的result结果
//data class NewsResult(val curpage:Int, val allnum:Int, val newslist:List<News>)
//
//// 新闻数据类，result里的list
//data class News(val id:String, val ctime:String, val title:String, val description:String, val source:String, val picUrl:String, val url:String)
//
//
// 实体类映射的是接口返回的JSON数据类型
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport

data class NewsResponse(
    val reason: String,
    val result: NewsResult,
    val error_code: Int
)

data class NewsResult(
    val stat: String,
    val data: List<News>
)

// 假设所有新闻的标题互不重复,为了方便查询建一下索引
// 真实的业务千万不要这样处理！！！
data class News(
    // id 默认自增
    var id: Long,
    @Column(unique = true, index = true)
    val title: String,
    val date: String,
    val category: String,
    val author_name: String,
    val thumbnail_pic_s: String,
    val thumbnail_pic_s02: String?,
    val thumbnail_pic_s03: String?,
    val url: String
) : LitePalSupport()
// 为 News添加 LitePal 支持，使之作为一张表存入数据库中


// 在数据库中记录一下网络请求的日志
data class NetWorkLog(
    // LitePal会自动添加一个自增的id字段
    val juHeKey: String,
    val type: String,
    val timestamp: String // 用字符串存储时间
) : LitePalSupport()