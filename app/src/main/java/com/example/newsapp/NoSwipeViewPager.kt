package com.example.newsapp

import android.content.Context
import android.util.AttributeSet
import androidx.viewpager.widget.ViewPager
import android.view.MotionEvent

//禁止滑动的viewpager
class NoSwipeViewPager(context: Context?, attributeSet: AttributeSet?) : ViewPager(
    context!!, attributeSet
) {
    private var canSwipe = true

    //是否禁止滑动
    fun setCanSwipe(canSwipe: Boolean) {
        this.canSwipe = canSwipe
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return canSwipe && super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return canSwipe && super.onInterceptTouchEvent(ev)
    }
}