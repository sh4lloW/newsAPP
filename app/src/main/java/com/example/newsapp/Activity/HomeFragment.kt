package com.example.newsapp.Activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.newsapp.R
import com.google.android.material.tabs.TabLayout
import java.util.ArrayList
import kotlin.concurrent.thread

class HomeFragment : Fragment() {private lateinit var newsRecyclerView: RecyclerView

    private val newsTypeList = listOf("shehui", "guoji", "yule", "keji", "tiyu", "caijing")
    private val titleList = listOf("社会", "国际", "娱乐", "科技", "体育", "财经")
    // 初始化一个空fragment列表
    private val fragmentList = ArrayList<NewFragment>()

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home,container,false)
        tabLayout = view.findViewById(R.id.news_tab_layout)
        viewPager = view.findViewById(R.id.news_view_pager)
        return  view
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        for(newsType in newsTypeList.indices) {
            fragmentList.add(NewFragment(newsTypeList[newsType],titleList[newsType]))
        }


        viewPager.adapter  = Adapter(childFragmentManager)
        //实现左右滑动
        tabLayout.setupWithViewPager(viewPager)
    }

    inner class Adapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getPageTitle(position: Int): CharSequence {
            // 设置标题
            return titleList[position]
        }

    }
}