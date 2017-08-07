package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.philips.platform.uid.R;

/**
 * Created by Kunal on 31/07/17.
 */

public class SideBar extends DrawerLayout {

    private Context context;
    private RelativeLayout headerContainerLayout;
    private RelativeLayout footerContainerLayout;
    private LinearLayout headerLayout;
    private LinearLayout menuLayout;
    private LinearLayout footerLayout;
    private LayoutInflater inflater;

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;

        /*inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.uid_sidebar_layout, this);
        headerContainerLayout = (RelativeLayout) findViewById(R.id.sidebar_header_container_layout);
        footerContainerLayout = (RelativeLayout) findViewById(R.id.sidebar_footer_container_layout);
        headerLayout = (LinearLayout) findViewById(R.id.sidebar_header_layout);
        menuLayout = (LinearLayout) findViewById(R.id.sidebar_menu_layout);
        footerLayout = (LinearLayout) findViewById(R.id.sidebar_footer_layout);*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        /*switch (MeasureSpec.getMode(widthMeasureSpec)) {
            case MeasureSpec.EXACTLY:
                // Nothing to do
                break;
            case MeasureSpec.AT_MOST:
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                        Math.min(MeasureSpec.getSize(widthMeasureSpec), getMaxWidth()), MeasureSpec.EXACTLY);
                break;
            case MeasureSpec.UNSPECIFIED:
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMaxWidth(), MeasureSpec.EXACTLY);
                break;
        }*/
        // Let super sort out the height
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int width = MeasureSpec.getSize(widthMeasureSpec);
//
//        switch (widthMode) {
//            case MeasureSpec.AT_MOST:
//            case MeasureSpec.UNSPECIFIED:
//                width = Math.min(getMaxWidth(), width);
//                break;
//        }
//        widthMode = MeasureSpec.EXACTLY;

//        super.onMeasure(MeasureSpec.makeMeasureSpec(width, widthMode), heightMeasureSpec);
        int childCount = getChildCount();
        for (int index = 0; index < childCount; index++) {
            View child = getChildAt(index);
            if (isDrawerView(child)) {
                int widthMode = MeasureSpec.EXACTLY;
                int width = Math.min(child.getMeasuredWidth(), getMaxWidth());
                child.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), heightMeasureSpec);
            }
        }
    }

    private int getMaxWidth() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.uid_sidebar_max_width);
    }

    boolean isDrawerView(View child) {
        final int gravity = ((LayoutParams) child.getLayoutParams()).gravity;
        final int absGravity = GravityCompat.getAbsoluteGravity(gravity,
                ViewCompat.getLayoutDirection(child));
        if ((absGravity & Gravity.LEFT) != 0) {
            // This child is a left-edge drawer
            return true;
        }
        if ((absGravity & Gravity.RIGHT) != 0) {
            // This child is a right-edge drawer
            return true;
        }
        return false;
    }

    /*public void addHeaderView(View view) {
        headerContainerLayout.setVisibility(View.VISIBLE);
        headerLayout.addView(view);
    }

    public void addMenuView(View view) {
        menuLayout.addView(view);
    }

    public void addFooterView(View view) {
        footerContainerLayout.setVisibility(View.VISIBLE);
        footerLayout.addView(view);
    }

    public void addHeaderView(int res) {
        headerContainerLayout.setVisibility(View.VISIBLE);
        View child = inflater.inflate(res, null);
        headerLayout.addView(child);
    }

    public void addMenuView(int res) {
        View child = inflater.inflate(res, null);
        menuLayout.addView(child);
    }

    public void addFooterView(int res) {
        footerContainerLayout.setVisibility(View.VISIBLE);
        View child = inflater.inflate(res, null);
        footerLayout.addView(child);
    }*/
}
