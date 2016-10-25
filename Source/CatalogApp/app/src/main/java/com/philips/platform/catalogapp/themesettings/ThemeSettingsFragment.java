/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.themesettings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.events.ColorRangeChangedEvent;
import com.philips.platform.catalogapp.events.NavigationColorChangedEvent;
import com.philips.platform.catalogapp.events.TonalRangeChangedEvent;
import com.philips.platform.catalogapp.fragments.BaseFragment;
import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ContentColor;
import com.philips.platform.uit.thememanager.NavigationColor;
import com.philips.platform.uit.thememanager.UITHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ThemeSettingsFragment extends BaseFragment {

    @Bind(R.id.colorRangeList)
    RecyclerView colorRangeListview;

    @Bind(R.id.tonalRangeList)
    RecyclerView tonalRangeListview;

    @Bind(R.id.notificationBarList)
    RecyclerView notificationBarListview;

    @Bind(R.id.accentColorRangeList)
    RecyclerView accentColorRangeList;

    @Bind(R.id.warningText)
    ViewGroup warningText;

    private ThemeColorHelper themeColorHelper;

    private int colorPickerWidth = 48;

    private SharedPreferences defaultSharedPreferences;
    private ThemeColorAdapter colorRangeAdapter;
    private ThemeColorAdapter tonalRangeAdapter;
    private ThemeColorAdapter navigationListAdapter;
    private ContentColor contentColor = ContentColor.ULTRA_LIGHT;

    private ColorRange colorRange = ColorRange.GROUP_BLUE;
    private NavigationColor navigationColor = NavigationColor.ULTRA_LIGHT;
    private ThemeHelper themeHelper;
    private int colorRangeSelectedPosition;
    private int contentSelectedPosition;
    private int navigationSelectedPosition;

    @Override
    public boolean showThemeSettingsIcon() {
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_theme_settings, container, false);
        view.setVisibility(View.GONE);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        ButterKnife.bind(this, view);
        themeColorHelper = new ThemeColorHelper();
        themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(getContext()));

        if (savedInstanceState != null) {
            colorRangeSelectedPosition = savedInstanceState.getInt(UITHelper.COLOR_RANGE, getColorRangeAdapter().getSelectedPosition());
            contentSelectedPosition = savedInstanceState.getInt(UITHelper.CONTENT_TONAL_RANGE, getTonalRangeAdapter(colorRange).getSelectedPosition());
            navigationSelectedPosition = savedInstanceState.getInt(UITHelper.NAVIGATION_RANGE, getNavigationListAdapter(colorRange).getSelectedPosition());
            colorRange = ColorRange.values()[colorRangeSelectedPosition];
            EventBus.getDefault().post(new ColorRangeChangedEvent(contentColor.name().toString(), colorRange));

            initNavigationColor(navigationSelectedPosition);
            initContentColor(contentSelectedPosition);
            notifyNavigationSettingsChanged();
        } else {
            colorRange = themeHelper.initColorRange();
            navigationColor = themeHelper.initNavigationRange();
            contentColor = themeHelper.initContentTonalRange();
        }
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                initColorPickerWidth();
                buildColorRangeList();
                buildContentTonalRangeList(colorRange);
                buildNavigationList(colorRange);

                buildAccentColorsList(colorRange);
                view.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void initContentColor(final int colorRangeSelectedPosition) {
        contentColor = ContentColor.values()[colorRangeSelectedPosition];
        EventBus.getDefault().post(new TonalRangeChangedEvent(contentColor.name(), contentColor));
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putInt(UITHelper.COLOR_RANGE, colorRange.ordinal());
        outState.putInt(UITHelper.CONTENT_TONAL_RANGE, getSelectedContentTonalRangePosition());
        outState.putInt(UITHelper.NAVIGATION_RANGE, getSelectedNavigationPosition());
        super.onSaveInstanceState(outState);
    }

    private void initColorPickerWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;

        float pageMargin = getResources().getDimension(R.dimen.theme_settings_pagemargin);
        colorPickerWidth = (int) ((widthPixels - pageMargin) / 8);
    }

    private void updateThemeSettingsLayout() {
        buildContentTonalRangeList(colorRange);
    }

    private void buildColorRangeList() {
        colorRangeListview.setAdapter(getColorRangeAdapter());

        setLayoutOrientation(colorRangeListview);
    }

    @NonNull
    private ThemeColorAdapter getColorRangeAdapter() {
        colorRangeAdapter = new ThemeColorAdapter(themeColorHelper.getColorRangeItemsList(), new ThemeChangedListener() {
            @Override
            public void onThemeSettingsChanged(final String changedColorRange) {
                colorRange = ColorRange.valueOf(changedColorRange.toUpperCase());

                updateTonalRangeColors();
                updateNavigationRangeColors();
                EventBus.getDefault().post(new ColorRangeChangedEvent(contentColor.name().toString(), colorRange));
            }
        }, colorPickerWidth);
        colorRangeAdapter.setSelected(colorRangeSelectedPosition == 0 ? colorRange.ordinal() : colorRangeSelectedPosition);
        return colorRangeAdapter;
    }

    private void updateNavigationRangeColors() {
        tonalRangeAdapter.setColorModels(themeColorHelper.getContentColorModelList(colorRange, getContext()));
    }

    private void updateTonalRangeColors() {
        navigationListAdapter.setColorModels(themeColorHelper.getNavigationColorModelsList(colorRange, getContext()));
    }

    private void buildContentTonalRangeList(final ColorRange changedColorRange) {
        tonalRangeListview.setAdapter(getTonalRangeAdapter(changedColorRange));

        setLayoutOrientation(tonalRangeListview);
    }

    @NonNull
    private ThemeColorAdapter getTonalRangeAdapter(final ColorRange changedColorRange) {
        tonalRangeAdapter = new ThemeColorAdapter(themeColorHelper.getContentColorModelList(changedColorRange, getContext()), new ThemeChangedListener() {
            @Override
            public void onThemeSettingsChanged(final String tonalRangeChanged) {
                contentColor = getContentTonalRangeByPosition();
                EventBus.getDefault().post(new TonalRangeChangedEvent(contentColor.name().toString(), contentColor));
            }
        }, colorPickerWidth);
        tonalRangeAdapter.setSelected(getSelectedContentTonalRangePosition());
        return tonalRangeAdapter;
    }

    private ContentColor getContentTonalRangeByPosition() {
        final ThemeColorAdapter adapter = (ThemeColorAdapter) tonalRangeListview.getAdapter();
        contentSelectedPosition = adapter.getSelectedPosition();
        final ContentColor[] values = ContentColor.values();
        return values[values.length - contentSelectedPosition - 1];
    }

    private int getSelectedContentTonalRangePosition() {
        return contentSelectedPosition == 0 ? (contentColor.values().length - contentColor.ordinal() - 1) : contentSelectedPosition;
    }

    private void buildNavigationList(final ColorRange colorRange) {
        notificationBarListview.setAdapter(getNavigationListAdapter(colorRange));

        setLayoutOrientation(notificationBarListview);
    }

    @NonNull
    private ThemeColorAdapter getNavigationListAdapter(final ColorRange colorRange) {
        navigationListAdapter = new ThemeColorAdapter(themeColorHelper.getNavigationColorModelsList(colorRange, getContext()), new ThemeChangedListener() {
            @Override
            public void onThemeSettingsChanged(final String changedColorRange) {
                final ThemeColorAdapter adapter = (ThemeColorAdapter) notificationBarListview.getAdapter();
                navigationSelectedPosition = adapter.getSelectedPosition();

                initNavigationColor(navigationSelectedPosition);
                notifyNavigationSettingsChanged();
            }
        }, colorPickerWidth);
        navigationListAdapter.setSelected(getSelectedNavigationPosition());
        return navigationListAdapter;
    }

    private void notifyNavigationSettingsChanged() {
        EventBus.getDefault().post(new NavigationColorChangedEvent(contentColor.name().toString(), navigationColor));
    }

    private void initNavigationColor(final int selectedPosition) {
        NavigationColor[] values = NavigationColor.values();
        navigationColor = values[values.length - selectedPosition - 1];
    }

    private int getSelectedNavigationPosition() {
        return navigationSelectedPosition == 0 ? NavigationColor.values().length - navigationColor.ordinal() - 1 : navigationSelectedPosition;
    }

    private void buildAccentColorsList(final ColorRange colorRange) {
        accentColorRangeList.setAdapter(new ThemeColorAdapter(themeColorHelper.getAccentColorsList(getContext(), colorRange), new ThemeChangedListener() {
            @Override
            public void onThemeSettingsChanged(final String changedColorRange) {
                updateThemeSettingsLayout();
            }
        }, colorPickerWidth));

        setLayoutOrientation(accentColorRangeList);
    }

    private void setLayoutOrientation(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getPageTitle() {
        return R.string.page_tittle_theme_settings;
    }
}

