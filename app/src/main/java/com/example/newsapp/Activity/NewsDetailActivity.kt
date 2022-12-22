package com.example.newsapp.Activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.os.Looper
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.MyApplication.Companion.context
import com.example.newsapp.R
import com.example.newsapp.database.MyDatabaseHelper
import kotlin.concurrent.thread

class NewsDetailActivity : AppCompatActivity() {
    // 新闻详细页，直接用WebView打开JSON传过来的URL，URL由intent传过来
    @SuppressLint("SetJavaScriptEnabled", "Recycle", "Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newsdetail)

        //数据库
        val dbHelper = MyDatabaseHelper(this, "app.db", 1)
        val db = dbHelper.writableDatabase

        //爱心
        val ft_cb = findViewById<CheckBox>(R.id.ft_cb)
        //系统自带的返回
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //打开新闻
        val url = intent.getStringExtra("url")
        val source = intent.getStringExtra("source")
        val image = intent.getStringExtra("image")
        val title = intent.getStringExtra("title")
        val id = intent.getStringExtra("id")
        val webView = findViewById<WebView>(R.id.news_webview)
        // 加这一行是以应用内部打开浏览器，而不是启动手机自带浏览器
        webView.webViewClient = WebViewClient()
        if (url != null) {
             webView.loadUrl(url)
        }

//        val values1 = ContentValues().apply {
////组装第一条数据
//            put("title", "The Da Vinci Code")
//            put("source", "Dan Brown")
//            put("image", "ieo")
//            put("url", url)
//            put("id",1)
//        }
//        db.insert("app", null, values1) //插入第一条数据


//        Toast.makeText(this, cursor.getString(cursor.getColumnIndex("url")),
//            Toast.LENGTH_SHORT).show()

//        if(!cursor.getString(cursor.getColumnIndex("url")).equals(url)){
//            ft_cb.isChecked = true
//        }


        //查询数据库
        var cursor = db.rawQuery("select * from app where id='$id'" ,null)
        //如果有记录则红心，没有则黑心
        ft_cb.isChecked = cursor.count != 0

        //收藏按钮状态改变监听，
        // 如果状态由未收藏转为收藏，去数据库里找，如果没有则写入数据库
        //如果由收藏转为取消收藏，去数据库里找，如果有则删除
        ft_cb.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if(isChecked){

                    val values = ContentValues().apply {
                        put("title", title)
                        put("source", source)
                        put("image", image)
                        put("url", url)
                        put("id",id)
                    }
                    db.insert("app", null, values) //插入数据

                    Toast.makeText(context,"收藏成功!",Toast.LENGTH_LONG).show()
                }
                else{
                    db.execSQL("delete from app where id ='$id'")
                    Toast.makeText(context,"取消收藏成功!",Toast.LENGTH_LONG).show()
                }
            }
        })


    }



    // 左上角加一个返回按钮，终止本页面并跳回新闻列表
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}