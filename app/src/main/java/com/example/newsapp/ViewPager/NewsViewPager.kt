package com.example.newsapp.ViewPager

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager

class NewsViewPager : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun setCurrentItem(item: Int) {
        // smoothScroll=false 这个参数能解决切换时的多页闪烁问题
        super.setCurrentItem(item, false)
    }
}