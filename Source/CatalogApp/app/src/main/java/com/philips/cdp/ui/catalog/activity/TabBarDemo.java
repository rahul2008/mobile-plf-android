package com.philips.cdp.ui.catalog.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.ActionBarOverlayLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.TabUtils;

/**
 * <p>
 * UIKit uses design library TabLayout {@link TabLayout}.
 * Please refer {@link com.philips.cdp.uikit.utils.TabUtils} for managing tabs.
 *<br>
 * Due to different requirement of tabs on phone and tablet,
 * {@link TabUtils#adjustTabs(TabLayout, Context)} must be called in onResume.
 *
 * Tab can be created with two variants.
 * <h5>With Icons</h5>
 * <pre> style="@style/PTablayout.Image"</pre></pre></p>
 *
 * <h5>Text</h5>
 * <pre> style="@style/PTablayout"</pre></pre></p>
 *
 * <h5>Creating Tabs</h5>
 * <p>
 *      Use {@link TabUtils#newTab(int, int, int)}  for creating new tabs
 * </p>
 *
 * <p>
 * Examples:
 * <pre>
 *             &lt;android.support.design.widget.TabLayout
 *                      android:id="@+id/tab_bar"
 *                      <font color="red">style="@style/PTablayout.Image"</font>/&gt;
 *
 *             &lt;android.support.design.widget.TabLayout
 *                      android:id="@+id/tab_bar_text"
 *                      <font color="red">style="@style/PTablayout"</font>/&gt;
 *     </pre>
 * </p>
 */
public class TabBarDemo extends CatalogActivity {

    TabLayout topLayout;
    TabLayout bottomLayout;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_demo);
        disableActionbarShadow(this);
        TabLayout view = (TabLayout) findViewById(R.id.tab_bar);
        setTopBar();
        setBottomBar();
        if(savedInstanceState != null) {
            topLayout.post(new Runnable() {
                @Override
                public void run() {
                    topLayout.getTabAt(savedInstanceState.getInt("top")).select();
                    bottomLayout.getTabAt(savedInstanceState.getInt("bottom")).select();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TabUtils.adjustTabs(topLayout, this);
        TabUtils.adjustTabs(bottomLayout, this);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("top", topLayout.getSelectedTabPosition());
        outState.putInt("bottom", bottomLayout.getSelectedTabPosition());
    }

    private void setTopBar() {
        topLayout = (TabLayout) findViewById(R.id.tab_bar);
        TabUtils utils = new TabUtils(this, topLayout, true);

        TabLayout.Tab tab = utils.newTab(R.string.uikit_splash_title, R.drawable.alarm, 0);
        utils.setIcon(tab,VectorDrawable.create(this,R.drawable.uikit_clock_32x32),true);
        utils.setTitle(tab, "Alarm");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.apple, 0);
        utils.setIcon(tab,VectorDrawable.create(this,R.drawable.uikit_apple_32x32),true);
        utils.setTitle(tab, "Wellness");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.barchart, 3);
        utils.setIcon(tab,VectorDrawable.create(this,R.drawable.uikit_stats_39x32),true);
        utils.setTitle(tab, "Statistics");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.gear, 0);
        utils.setIcon(tab,VectorDrawable.create(this,R.drawable.uikit_gear_32x32),true);
        utils.setTitle(tab, "Settings");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, R.drawable.alarm, 0);
        utils.setIcon(tab,VectorDrawable.create(this,R.drawable.uikit_clock_32x32),true);
        utils.setTitle(tab, "Alarm");
        topLayout.addTab(tab);
    }

    private void setBottomBar() {
        bottomLayout = (TabLayout) findViewById(R.id.tab_bar_text);
        TabUtils utils = new TabUtils(this, bottomLayout, false);
        TabLayout.Tab tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Alarm");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Wellness");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Statistics");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Settings");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Alarm");
        bottomLayout.addTab(tab);
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
}
