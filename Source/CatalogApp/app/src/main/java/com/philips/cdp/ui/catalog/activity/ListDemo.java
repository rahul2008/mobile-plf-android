/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.ui.catalog.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.philips.cdp.ui.catalog.CustomListView.ListViewPagerAdapter;
import com.philips.cdp.ui.catalog.CustomListView.ListViewWithOptions;
import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.utils.TabUtils;

/**
 * <b></b> ListDemo is class to demonstrate the use of Customized ListView with the help of adapters ({@link com.philips.cdp.ui.catalog.CustomListView.ListViewWithIcons}, {@link ListViewWithOptions}, {@link com.philips.cdp.ui.catalog.CustomListView.ListViewWithoutIcons})</b>
 * <p/>
 * <b></b> ListViewWithIcons is class to demonstrate the use of R.layout.uikit_list_with_icons with an adapter </b>
 * <p/>
 * <b></b> The Below items can be controlled by setting visibility as VISIBLE or GONE</b></br>
 * <pre>
 * holder.textView = (TextView) convertView.findViewById(R.id.text);
 * holder.mImage = (TintableImageView) convertView.findViewById(R.id.image);
 * holder.mBadge = (BadgeView) convertView.findViewById(R.id.notification_badge);
 * holder.arrow = (FontIconTextView) convertView.findViewById(R.id.arrow);
 * holder.name = (TextView) convertView.findViewById(R.id.off_on);
 * holder.value = (PuiSwitch) convertView.findViewById(R.id.switch_button);
 * holder.done = (TextView) convertView.findViewById(R.id.textdownnoicon);
 *</pre>
 * <b></b> ListWithOptions is class to demonstrate the use of uikit_listview_with_options_custom_layout with an adapter </b>
 * <p/>
 * <b></b> We have 2 types Of Such Lists.One with Header and one without Header</b></br>
 * <p/>
 * <b></b> Please find below the IDs of the customized layout of ListView</b></br>
 * <pre>
 * ImageView image = (ImageView) vi.findViewById(R.id.image);
 * TextView name = (TextView) vi.findViewById(R.id.text1Name);
 * TextView value = (TextView) vi.findViewById(R.id.text2value);
 * TextView from = (TextView) vi.findViewById(R.id.from);
 * </pre>
 *
 * <b></b> To Include Header in The ListView with Options use the below code</b>
 * <p/>
 * <pre>
 * LayoutInflater lf;
 * View headerView;
 * lf = getActivity().getLayoutInflater();
 * headerView = (View)lf.inflate(R.layout.uikit_listview_products_header, null, false);
 * list.addHeaderView(headerView, null, false);
 *
 * </pre>
 *
 * <b></b> ListViewWithoutIcons is class to demonstrate the use of R.layout.uikit_listview_without_icons with an adapter </b>
 * <p/>
 * <b></b> The Below items can be controlled by setting visibility as VISIBLE or GONE</b></br>
 * <pre>
 *  TextView name = (TextView) vi.findViewById(R.id.ifo);
 * PuiSwitch value = (PuiSwitch) vi.findViewById(R.id.switch_button);
 * TextView number = (TextView) vi.findViewById(R.id.numberwithouticon);
 * TextView on_off = (TextView) vi.findViewById(R.id.medium);
 * FontIconTextView arrow = (FontIconTextView) vi.findViewById(R.id.arrowwithouticons);
 * TextView description = (TextView) vi.findViewById(R.id.text_description_without_icons);
 * </pre>
 */
public class ListDemo extends CatalogActivity {
    ListViewWithOptions adapter;
    ListView list;
    TabLayout bottomLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabview_viewpager);
        TabUtils.disableActionbarShadow(this);
        setBottomBar();
        setViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TabUtils.adjustTabs(bottomLayout, this);
    }



    private void setViewPager() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new ListViewPagerAdapter(getSupportFragmentManager(), bottomLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(bottomLayout));
        viewPager.setOffscreenPageLimit(4);
        bottomLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

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

        TabLayout.Tab tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Filter \n Option");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Filter \n" +
                " Option");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Filter \n" +
                " Option");
        bottomLayout.addTab(tab);

        tab = utils.newTab(0, 0, 0);
        utils.setTitle(tab, "Filter \n" +
                " Option");
        bottomLayout.addTab(tab);

        bottomLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }
}

