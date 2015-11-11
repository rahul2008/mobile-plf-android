package com.philips.cdp.uikit.costumviews;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.com.philips.cdp.uikit.utils.HamburgerUtil;
import com.philips.cdp.uikit.com.philips.cdp.uikit.utils.OnDataNotified;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.hamburger.PhilipsHamburgerAdapter;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PhilipsDrawerLayout extends DrawerLayout implements OnDataNotified {

    private HamburgerUtil hamburgerUtil;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private Context context;
    private TextView actionBarTitle;
    private PhilipsBadgeView actionBarCount;
    private boolean isExpandable = false;
    private RelativeLayout listViewParentLayout;
    private VectorDrawableImageView footerImageView;

    public PhilipsDrawerLayout(final Context context) {
        this(context, null);
    }

    public PhilipsDrawerLayout(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhilipsDrawerLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        validateIsExpandable(context, attrs);
        processDrawerLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void processDrawerLayout() {
        initializeDrawer();
        hamburgerUtil.updateSmartFooter(footerImageView);
        if (isExpandable)
            disableGroupCollapse();
    }

    private void validateIsExpandable(Context context, AttributeSet attrs) {
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.hamburger, 0, 0);
        isExpandable = a.getBoolean(R.styleable.hamburger_expandable, false);
        a.recycle();
    }

    @Override
    public void onDataSetChanged(String dataCount) {
        actionBarCount.setText(dataCount);
        hamburgerUtil.updateSmartFooter(footerImageView);
    }

    public void setCounterListener(BaseAdapter baseAdapter) {
        if (baseAdapter != null) {
            PhilipsHamburgerAdapter philipsHamburgerAdapter = (PhilipsHamburgerAdapter) baseAdapter;
            philipsHamburgerAdapter.setOnDataNotified(this);
        }
    }

    private void initializeDrawer() {
        context = getContext();
        AppCompatActivity activity = (AppCompatActivity) context;
        initActionBar(activity.getSupportActionBar());
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View drawer;
        if (isExpandable) {
            drawer = inflater.inflate(R.layout.uikit_hamburger_menu_expandable, null, false);
            drawerListView = (ExpandableListView) drawer.findViewById(R.id.hamburger_list);
        } else {
            drawer = inflater.inflate(R.layout.uikit_hamburger_menu, null, false);
            drawerListView = (ListView) drawer.findViewById(R.id.hamburger_list);
        }
        initializeDrawerViews(drawer);
        hamburgerUtil.moveDrawerToTop(drawer);
    }


    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hamburgerUtil.updateSmartFooter(footerImageView);
        }
    }

    private void initializeDrawerViews(View drawer) {
        listViewParentLayout = (RelativeLayout) drawer.findViewById(R.id.list_view_parent);
        footerImageView = (VectorDrawableImageView) drawer.findViewById(R.id.philips_logo);
        drawerLayout = (DrawerLayout) drawer.findViewById(R.id.philips_drawer_layout);
        hamburgerUtil = new HamburgerUtil(getContext(), drawerListView);
    }

    public ListView getDrawerListView() {
        return drawerListView;
    }

    public void closeDrawer() {
        int width = (int) getResources().getDimension(R.dimen.uikit_hamburger_width);
        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(width,
                ViewGroup.LayoutParams.MATCH_PARENT, Gravity.LEFT);
        drawerLayout.updateViewLayout(listViewParentLayout, layoutParams);
        drawerLayout.closeDrawer(listViewParentLayout);
    }

    public void initActionBar(ActionBar actionBar) {
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.uikit_action_bar_title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(VectorDrawable.create(context, R.drawable.uikit_hamburger_icon));
        actionBarTitle = (TextView) actionBar.getCustomView().findViewById(R.id.hamburger_title);
        actionBarCount = (PhilipsBadgeView) actionBar.getCustomView().findViewById(R.id.hamburger_count);
        actionBarCount.setText("0");
    }

    public void setTitle(CharSequence title) {
        if (actionBarTitle != null)
            actionBarTitle.setText(title);
    }

    public ExpandableListView getExpandableDrawerListView() {
        return (ExpandableListView) drawerListView;
    }

    private void disableGroupCollapse() {
        ExpandableListView expandableListView = (ExpandableListView) drawerListView;
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void setHamburgerCount(String count) {
        actionBarCount.setText(count);
    }

}
