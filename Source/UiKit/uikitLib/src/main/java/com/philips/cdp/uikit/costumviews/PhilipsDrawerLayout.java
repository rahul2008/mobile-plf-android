package com.philips.cdp.uikit.costumviews;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.ListView;
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
    private TextView actionBarCount;
    private boolean isExpandable = false;
    private LinearLayout listViewParentLayout;


    public PhilipsDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.expandable});
        isExpandable = typedArray.getBoolean(0, false);
        initializeDrawer();
        hamburgerUtil.updateSmartFooter();
    }

    public PhilipsDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(new int[]{R.attr.expandable});
        isExpandable = typedArray.getBoolean(0, false);
        initializeDrawer();
        hamburgerUtil.updateSmartFooter();
        if (isExpandable)
            disableGroupCollapse();
    }

    @Override
    public void onDataSetChanged(BaseAdapter adapter, String dataCount) {
        actionBarCount.setText(dataCount);
        hamburgerUtil.updateSmartFooter();
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
            drawer = inflater.inflate(R.layout.uikit_hamburger_menu_expandable, null);
            drawerListView = (ExpandableListView) drawer.findViewById(R.id.hamburger_list);
        } else {
            drawer = inflater.inflate(R.layout.uikit_hamburger_menu, null);
            drawerListView = (ListView) drawer.findViewById(R.id.hamburger_list);
        }
        initializeDrawerViews(drawer);
        hamburgerUtil.moveDrawerToTop(drawer);
    }

    private void initializeDrawerViews(View drawer) {
        listViewParentLayout = (LinearLayout) drawer.findViewById(R.id.list_view_parent);
        drawerLayout = (DrawerLayout) drawer.findViewById(R.id.philips_drawer_layout);
        hamburgerUtil = new HamburgerUtil(getContext(), drawerListView);
    }

    public ListView getDrawerListView() {
        return drawerListView;
    }

    public void closeDrawer() {
        DrawerLayout.LayoutParams layoutParams = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
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
        actionBarCount = (TextView) actionBar.getCustomView().findViewById(R.id.hamburger_count);
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
