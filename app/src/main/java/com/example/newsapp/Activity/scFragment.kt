package com.example.newsapp.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.newsapp.Adapter.NewStarAdapter
import com.example.newsapp.R
import com.example.newsapp.app.App
import com.example.newsapp.app.App.Companion.Dangqianuser
import com.example.newsapp.room.AppDatabase
import com.example.newsapp.room.NewStar
import kotlin.concurrent.thread

class scFragment(var testName:String) : Fragment() {
    private lateinit var list: List<NewStar>
    val newStarDao = AppDatabase.getDatabase(this).NewStarDao()
    lateinit var adapter: NewStarAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shou_cang)
        ToolBarview("返回",null,"收藏列表",null)
        //listciew加载收藏列表并且点击跳转
        thread {
            list=newStarDao.loadAllUsers(Dangqianuser)
            if(list.size>0){
                adapter=NewStarAdapter(this,R.layout.repo_item,list)
                LshouCang.adapter=adapter
            }else{
                Looper.prepare()
                Toast.makeText( App.context,"并未检测到收藏数据!!", Toast.LENGTH_LONG).show()
                Looper.loop()
            }
        }
        //带参数跳转到内容详情页  新闻ID+频道ID
        LshouCang.setOnItemClickListener{parent,view,position,id->
            val newstar=list[position]
            val intent= Intent(context,ContentActivity::class.java)
            intent.putExtra("newkey",newstar.keyID)
            intent.putExtra("channelkey",newstar.channelID)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }

    override fun onPause() {
        super.onPause()
    }
}