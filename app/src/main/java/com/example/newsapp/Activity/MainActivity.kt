package com.example.newsapp.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.example.newsapp.*
import com.example.newsapp.Adapter.NewsAdapter
import com.example.newsapp.model.News
import com.example.newsapp.model.NewsResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.concurrent.thread
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    val fragmentList = listOf(HomeFragment(), scFragment("第一个测试"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val contentViewpage = findViewById<ViewPager>(R.id.content_view_pager)
        //设置fragment页面的缓存数量
        contentViewpage.offscreenPageLimit = fragmentList.size
        contentViewpage.adapter = MyAdapter(supportFragmentManager)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.menu_message -> contentViewpage.currentItem = 0
                R.id.menu_contacts -> contentViewpage.currentItem = 1
            }
            false
        }
        contentViewpage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }
            override fun onPageSelected(position: Int) {
                bottomNav.menu.getItem(position).isCheckable = true
            }
            override fun onPageScrollStateChanged(state: Int) {

            }

        })
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_bottom_navigation,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_contacts-> Toast.makeText(this, "contacts", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}