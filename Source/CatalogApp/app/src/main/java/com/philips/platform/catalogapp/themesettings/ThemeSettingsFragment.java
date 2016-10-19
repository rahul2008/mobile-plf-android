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
import com.philips.platform.catalogapp.ThemeSettingsChanged;
import com.philips.platform.catalogapp.fragments.BaseFragment;
import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ContentColor;
import com.philips.platform.uit.thememanager.NavigationColor;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ThemeSettingsFragment extends BaseFragment {

    private ColorRange colorRange = ColorRange.GROUP_BLUE;

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

    int colorPickerWidth = 48;
    private SharedPreferences defaultSharedPreferences;
    private ThemeColorAdapter themeColorAdapter;
    private ThemeColorAdapter tonalRangeAdapter;
    private ThemeColorAdapter navigationListAdapter;

    private ContentColor contentColor = ContentColor.ULTRA_LIGHT;
    private NavigationColor navigationColor = NavigationColor.ULTRA_LIGHT;
    private ThemeHelper themeHelper;

    public void setThemeSettingsChanged(final ThemeSettingsChanged themeSettingsChanged) {
        this.themeSettingsChanged = themeSettingsChanged;
    }

    private ThemeSettingsChanged themeSettingsChanged;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_theme_settings, container, false);
        view.setVisibility(View.GONE);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        ButterKnife.bind(this, view);
        themeColorHelper = new ThemeColorHelper();
        themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(getContext()));
        colorRange = themeHelper.initColorRange();
        navigationColor = themeHelper.initNavigationRange();
        contentColor = themeHelper.initTonalRange();
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
        themeColorAdapter = new ThemeColorAdapter(themeColorHelper.getColorRangeItemsList(), new ThemeChangedListener() {
            @Override
            public void onThemeSettingsChanged(final String changedColorRange) {
                colorRange = ColorRange.valueOf(changedColorRange.toUpperCase());
                if (themeSettingsChanged != null) {
                    themeSettingsChanged.onColorRangeChanged(colorRange);
                }
                updateTonalRangeColors();
                updateNavigationRangeColors();
            }
        }, colorPickerWidth);
        themeColorAdapter.setSelected(colorRange.ordinal());
        return themeColorAdapter;
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
                if (themeSettingsChanged != null) {
                    themeSettingsChanged.onContentColorChanged(contentColor);
                }
            }
        }, colorPickerWidth);
        tonalRangeAdapter.setSelected(getSelectedContentTonalRangePosition());
        return tonalRangeAdapter;
    }

    private ContentColor getContentTonalRangeByPosition() {
        final ThemeColorAdapter adapter = (ThemeColorAdapter) tonalRangeListview.getAdapter();
        final int selectedPosition = adapter.getSelectedPosition();
        final ContentColor[] values = ContentColor.values();
        return values[values.length - selectedPosition - 1];
    }

    private int getSelectedContentTonalRangePosition() {
        return contentColor.values().length - contentColor.ordinal() - 1;
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
                final int selectedPosition = adapter.getSelectedPosition();

                NavigationColor[] values = NavigationColor.values();
                navigationColor = values[values.length - selectedPosition - 1];
                if (themeSettingsChanged != null) {
                    themeSettingsChanged.onNavigationColorChanged(navigationColor);
                }
            }
        }, colorPickerWidth);
        navigationListAdapter.setSelected(getSelectedNavigationPosition());
        return navigationListAdapter;
    }

    private int getSelectedNavigationPosition() {
        return NavigationColor.values().length - navigationColor.ordinal() - 1;
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

    @Override
    public void onDetach() {
        super.onDetach();
        themeSettingsChanged = null;
    }

    private void setLayoutOrientation(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getPageTitle() {
        return R.string.page_tittle_theme_settings;
    }
}

