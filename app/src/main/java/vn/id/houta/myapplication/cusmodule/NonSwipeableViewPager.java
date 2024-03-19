package vn.id.houta.myapplication.cusmodule;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class NonSwipeableViewPager extends ViewPager {

    private boolean swipeEnabled;

    public NonSwipeableViewPager(Context context) {
        super(context);
        this.swipeEnabled = false;
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.swipeEnabled = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.swipeEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.swipeEnabled && super.onInterceptTouchEvent(event);
    }

    public void setSwipeEnabled(boolean enabled) {
        this.swipeEnabled = enabled;
    }
}
