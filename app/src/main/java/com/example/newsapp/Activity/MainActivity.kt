package com.example.newsapp.Activity

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.newsapp.*
import com.example.newsapp.model.News
import com.example.newsapp.model.NewsResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    //6个分类fragment,1个收藏fragment
    val fragmentList = listOf(HomeFragment("shehui"),HomeFragment("guoji"),HomeFragment("yule"),HomeFragment("keji"),HomeFragment("tiyu"),HomeFragment("caijing"), scFragment("第一个测试"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contentViewpage = findViewById<ViewPager>(R.id.content_view_pager)
        val tablayout = findViewById<TabLayout>(R.id.tab_layout)
        val shehuiItem = findViewById<TabItem>(R.id.shehui)
        val guojiItem = findViewById<TabItem>(R.id.guoji)
        val yuleItem = findViewById<TabItem>(R.id.yule)
        val kejiItem = findViewById<TabItem>(R.id.keji)
        val tiyuItem = findViewById<TabItem>(R.id.tiyu)
        val caijingItem = findViewById<TabItem>(R.id.caijing)
        //设置fragment页面的缓存数量
        contentViewpage.offscreenPageLimit = fragmentList.size
        contentViewpage.adapter = MyAdapter(supportFragmentManager)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        //分类添加点击事件
        tablayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab) {
            }
            override fun onTabUnselected(p0: TabLayout.Tab) {
            }
            override fun onTabSelected(p0: TabLayout.Tab) {
                when(p0.text){
                    "社会" -> contentViewpage.currentItem = 0
                    "国际" -> contentViewpage.currentItem = 1
                    "娱乐" -> contentViewpage.currentItem = 2
                    "科技" -> contentViewpage.currentItem = 3
                    "体育" -> contentViewpage.currentItem = 4
                    "财经" -> contentViewpage.currentItem = 5
                }
             }
        })



        // 给底部导航栏的菜单项添加点击事件
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.menu_message -> contentViewpage.currentItem = 0
                R.id.menu_contacts -> contentViewpage.currentItem = 6
            }
        }

    }
    inner class MyAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }
    }


}