package com.philips.cl.di.fragments;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DiViewPager extends ViewPager {

private boolean enabled;

public DiViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.enabled = false;
}

@Override
public boolean onTouchEvent(MotionEvent event) {
    if (this.enabled) {
        return super.onTouchEvent(event);
    }

    return false;
}

@Override
public boolean onInterceptTouchEvent(MotionEvent event) {
    if (this.enabled) {
        return super.onInterceptTouchEvent(event);
    }

    return false;
}

public void setPagingEnabled(boolean enabled) {
    this.enabled = enabled;
} 

}
 