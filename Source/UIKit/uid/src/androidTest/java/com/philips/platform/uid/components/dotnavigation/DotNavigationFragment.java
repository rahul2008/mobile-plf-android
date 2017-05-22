/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.uid.components.dotnavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.core.deps.guava.annotations.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uid.R;
import com.philips.platform.uid.view.widget.DotNavigationIndicator;

import java.util.ArrayList;
import java.util.List;

public class DotNavigationFragment extends Fragment {

    static int[] drawableArray = new int[]{
            R.drawable.uid_ic_cross_icon,
            R.drawable.uid_ic_data_validation,
    };

    @SuppressLint("ValidFragment")
    public DotNavigationFragment(final int i) {
        drawableArray = new int[i];
    }

    public DotNavigationFragment() {
    }

    public static DotNavigationFragment create() {
        return new DotNavigationFragment(0);
    }

    @VisibleForTesting
    public static int[] getDrawableArray() {
        return drawableArray;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(com.philips.platform.uid.test.R.layout.fragment_dot_navigation, container, false);
        final ViewPager viewpager = (ViewPager) view.findViewById(com.philips.platform.uid.test.R.id.dot_navigation_pager);
        final List list = initPagerItems();
        viewpager.setAdapter(new NavigationPager(list));
        final DotNavigationIndicator dotNavigationIndicator = (DotNavigationIndicator) view.findViewById(com.philips.platform.uid.test.R.id.pager_indicator);
        dotNavigationIndicator.setViewPager(viewpager);
        return view;
    }

    private List initPagerItems() {
        int index = 1;
        List<PagerItem> pagerItems = new ArrayList<>();
        pagerItems.clear();
        for (int drawable : drawableArray) {
            pagerItems.add(new PagerItem(ContextCompat.getDrawable(getContext(), drawable), index));
            index++;
        }
        return pagerItems;
    }

    static final class NavigationPager extends PagerAdapter {

        private List<PagerItem> pagerItems;

        public NavigationPager(final List<PagerItem> pagerItems) {
            this.pagerItems = pagerItems;
        }

        @Override
        public int getCount() {
            return pagerItems.size();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final Context context = container.getContext();
            final View view = LayoutInflater.from(context).inflate(com.philips.platform.uid.test.R.layout.dot_navigation_item_layout, container, false);
            container.addView(view);
            return view;
        }

        @Override
        public boolean isViewFromObject(final View view, final Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
            container.removeView((View) object);
        }
    }
}
