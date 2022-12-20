package com.example.newsapp.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.newsapp.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    //6个分类fragment,1个收藏fragment
    val fragmentList = listOf(HomeFragment(5),HomeFragment(8),HomeFragment(10),HomeFragment(13),HomeFragment(12),HomeFragment(32), scFragment("第一个测试1111111111111111111111111111111111111111111111111111"))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val contentViewpage = findViewById<NoSwipeViewPager>(R.id.content_view_pager)
        val tablayout = findViewById<TabLayout>(R.id.tab_layout)

        //设置不可滑动
        contentViewpage.setCanSwipe(false)
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
                    "社会" -> contentViewpage.setCurrentItem(0,false)
                    "国际" -> contentViewpage.setCurrentItem(1,false)
                    "娱乐" -> contentViewpage.setCurrentItem(2,false)
                    "科技" -> contentViewpage.setCurrentItem(3,false)
                    "体育" -> contentViewpage.setCurrentItem(4,false)
                    "财经" -> contentViewpage.setCurrentItem(5,false)
                }
             }
        })



        // 给底部导航栏的菜单项添加点击事件
        bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.menu_message -> contentViewpage.setCurrentItem(0,false)
                R.id.menu_contacts -> contentViewpage.setCurrentItem(6,false)
            }
            //是否取消分类栏
            when(it.itemId){
                R.id.menu_message -> tablayout.isVisible = true
                R.id.menu_contacts -> tablayout.isVisible = false

            }
        }

        contentViewpage.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {


            }
            override fun onPageSelected(position: Int) {
                when(position){

                }
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


}