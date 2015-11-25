package com.philips.cdp.ui.catalog.activity;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.ActionBarOverlayLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.philips.cdp.ui.catalog.CustomListView.ListViewPagerAdapter;
import com.philips.cdp.ui.catalog.CustomListView.ListViewWithOptions;
import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.utils.TabUtils;

public class TabViewWithViewPager extends CatalogActivity {
    //LinearLayout ratingBarLayout;
    ListViewWithOptions adapter;
    ListView list;
    TabLayout bottomLayout;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabview_viewpager);
        disableActionbarShadow(this);
        setBottomBar();
        setViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TabUtils.adjustTabs(bottomLayout, this);
    }

    public void disableActionbarShadow(Activity activity) {
        if (activity == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (activity instanceof AppCompatActivity) {
                if (((AppCompatActivity) activity).getSupportActionBar() != null)
                    ((AppCompatActivity) activity).getSupportActionBar().setElevation(0);
            } else {
                if (activity.getActionBar() != null)
                    activity.getActionBar().setElevation(0);
            }
        } else {
            View content = activity.findViewById(android.R.id.content);
            if (content != null && content.getParent() instanceof ActionBarOverlayLayout) {
                ((ViewGroup) content.getParent()).setWillNotDraw(true);

                if (content instanceof FrameLayout) {
                    ((FrameLayout) content).setForeground(null);
                }
            }
        }
    }

    private void setViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new ListViewPagerAdapter(getSupportFragmentManager(),bottomLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(bottomLayout));

        bottomLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                            viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(final TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(final TabLayout.Tab tab) {

            }
        });
    }

    private void setBottomBar() {
        bottomLayout = (TabLayout) findViewById(R.id.tab_bar_for_list);
        TabUtils utils = new TabUtils(this, bottomLayout, false);
        TabLayout.Tab tab = utils.newTab(0, 0, 0, false);
        utils.setTitle(tab, "Filter \n Option");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0, false);
        utils.setTitle(tab, "Filter \n" +
                " Option");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0, false);
        utils.setTitle(tab, "Filter \n" +
                " Option");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0, true);
        utils.setTitle(tab, "Filter \n" +
                " Option");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0, false);
        utils.setTitle(tab, "Filter \n" +
                " Option");
        bottomLayout.addTab(tab);
        bottomLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }
}

