package com.lifecard.vpreca.utils

import androidx.viewpager2.widget.ViewPager2

open class SimpleOnPageChangeCallback() : ViewPager2.OnPageChangeCallback() {
    override fun onPageScrollStateChanged(state: Int) {
        super.onPageScrollStateChanged(state)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
    }

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
    }
}