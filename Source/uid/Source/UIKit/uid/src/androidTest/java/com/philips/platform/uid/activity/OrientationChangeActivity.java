/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.activity;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.TabLayout;
import com.philips.platform.uid.view.widget.UIDTabItem;


public class OrientationChangeActivity extends BaseTestActivity{



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIDHelper.init(new ThemeConfiguration(this, ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT));
        setContentView(com.philips.platform.uid.test.R.layout.layout_orientation_change);
        setUpTabLayout();
    }

    private void setUpTabLayout() {
        com.philips.platform.uid.view.widget.TabLayout tabLayout =  findViewById(com.philips.platform.uid.test.R.id.tab_layout);
        tabLayout.setPreferredHeight(com.philips.platform.uid.test.R.dimen.tablayout_height);

        Drawable drawable = getDrawable(R.drawable.uid_social_media_amazon_icon);

        UIDTabItem item1 = new UIDTabItem(this, true);
        item1.setIcon(drawable);
        item1.setTitle("Item1");
        item1.setBadgeCount("99");
        tabLayout.addView(item1);

        UIDTabItem item2 = new UIDTabItem(this, true);
        item2.setIcon(R.drawable.uid_social_media_amazon_icon);
        item2.setTitle(com.philips.platform.uid.test.R.string.item2);
        item2.setBadgeCount(com.philips.platform.uid.test.R.string.badge_count);
        tabLayout.addView(item2);

        UIDTabItem item3 = new UIDTabItem(this, false);
        item3.setIcon(drawable);
        item3.setBadgeCount("99");
        tabLayout.addView(item3);

        UIDTabItem item4 = new UIDTabItem(this);
        item4.setIcon(R.drawable.uid_social_media_amazon_icon);
        tabLayout.addView(item4);

        UIDTabItem item5 = new UIDTabItem(this, true);
        item5.setIcon(R.drawable.uid_social_media_amazon_icon);
        item5.setTitle("Item5");
        item5.setTitleColor(R.attr.uidTabsDefaultNormalOnIconColor);
        tabLayout.addView(item5);

        UIDTabItem item6 = new UIDTabItem(this, true);
        item6.setIcon(R.drawable.uid_social_media_amazon_icon);
        item6.setIconColor(ThemeUtils.buildColorStateList(this, R.color.uid_tab_icon_selector));
        item6.setIconColorTintMode(PorterDuff.Mode.SRC_IN);
        item6.setTitle(com.philips.platform.uid.test.R.string.item2);
        item6.setTitleColor(ThemeUtils.buildColorStateList(this, R.color.uid_tab_text_selector));
        tabLayout.addView(item6);

        com.philips.platform.uid.view.widget.TabLayout tabLayout2 = new TabLayout(this);
        tabLayout2.addView(item1, 0);
        tabLayout2.addView(item2, 1, item2.getLayoutParams());

        try {
            tabLayout2.addTab(tabLayout2.newTab().setText("Search").setIcon(R.drawable.uid_social_media_amazon_icon));
        } catch (IllegalArgumentException ex) {
            Log.e("", ex.getMessage());
        }
    }
}