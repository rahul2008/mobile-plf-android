/*
 * (C) Koninklijke Philips N.V., 2017.
 *  All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.dataUtils.PagerItem;
import com.philips.platform.catalogapp.databinding.FragmentDotNavigationBinding;
import com.philips.platform.catalogapp.databinding.ViewPagerDotNavigationItemBinding;

import java.util.ArrayList;
import java.util.List;

public class DotNavigationFragment extends BaseFragment {
    final static int[] drawableArray = new int[]{
            R.drawable.ic_dot_navigation_mom_n_baby,
            R.drawable.ic_dot_navigation_home,
            R.drawable.ic_dot_navigation_calender,
            R.drawable.ic_dot_navigation_3d_square,
            R.drawable.ic_dot_navigation_tooth,
    };

    public final static List<PagerItem> pagerItems = new ArrayList<PagerItem>();

    @Override
    public int getPageTitle() {
        return R.string.page_title_dot_navigation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final FragmentDotNavigationBinding dotNavigationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dot_navigation, container, false);
        dotNavigationBinding.setFragment(this);
        initPagerItems();
        dotNavigationBinding.dotNavigationPager.setAdapter(new NavigationPagerAdapter());
        dotNavigationBinding.dotNavigationPager.addOnPageChangeListener(dotNavigationBinding.pagerIndicator);
        dotNavigationBinding.pagerIndicator.setViewPager(dotNavigationBinding.dotNavigationPager);
        return dotNavigationBinding.getRoot();
    }

    private void initPagerItems() {
        int index = 1;
        pagerItems.clear();
        for (int drawable : drawableArray) {
            pagerItems.add(new PagerItem(VectorDrawableCompat.create(getResources(), drawable, getContext().getTheme()), index++));
        }
    }

    static final class NavigationPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pagerItems.size();
        }

        @Override
        public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
            final Context context = container.getContext();
            final ViewPagerDotNavigationItemBinding dotNavigationItemBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.view_pager_dot_navigation_item, container, false);
            dotNavigationItemBinding.setItem(pagerItems.get(position));
            container.addView(dotNavigationItemBinding.getRoot());
            return dotNavigationItemBinding.getRoot();
        }

        @Override
        public boolean isViewFromObject(@NonNull final View view, final Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull final ViewGroup container, final int position, final Object object) {
            container.removeView((View) object);
        }
    }
}
