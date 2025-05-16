package com.example.ebook_reader_app.utils;



import android.view.GestureDetector;
import android.view.MotionEvent;
import androidx.viewpager2.widget.ViewPager2;

public class GestureHandler extends GestureDetector.SimpleOnGestureListener {
    private ViewPager2 viewPager;

    public GestureHandler(ViewPager2 viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (velocityX > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
        } else if (velocityX < 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
        }
        return true;
    }
}