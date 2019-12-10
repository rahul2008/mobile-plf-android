/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.themesettings;

import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.util.List;


public class ThemeSettingsFragment extends Fragment{


    RecyclerView colorRangeListview;

    RecyclerView tonalRangeListview;

    RecyclerView notificationBarListview;

    RecyclerView accentColorRangeList;

    private ThemeColorHelper themeColorHelper;

    private int colorPickerWidth = 48;

    private ThemeColorAdapter colorRangeAdapter;
    private ThemeColorAdapter contentTonalRangeAdapter;
    private ThemeColorAdapter navigationListAdapter;
    ContentColor contentColor = ContentColor.ULTRA_LIGHT;

    ColorRange colorRange = ColorRange.GROUP_BLUE;
    private ThemeHelper themeHelper;
    private int colorRangeSelectedPosition;
    private int contentSelectedPosition;
    private int navigationSelectedPosition;
    NavigationColor navigationColor;
    AccentRange accentRange;
    private List<ColorModel> accentColorsList;
    private int accentSelectedPosition;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_theme_settings, container, false);
        themeColorHelper = new ThemeColorHelper();
        themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(getActivity()), getContext());
        colorRangeListview = (RecyclerView)view.findViewById(R.id.colorRangeList);
        tonalRangeListview = (RecyclerView)view.findViewById(R.id.tonalRangeList);
        accentColorRangeList = (RecyclerView)view.findViewById(R.id.accentColorRangeList);

        notificationBarListview = (RecyclerView)view.findViewById(R.id.notificationBarList);

        if (savedInstanceState != null) {
            colorRangeSelectedPosition = savedInstanceState.getInt(UIDHelper.COLOR_RANGE, getColorRangeAdapter().getSelectedPosition());
            contentSelectedPosition = savedInstanceState.getInt(UIDHelper.CONTENT_TONAL_RANGE, getContentTonalRangeAdapter(colorRange).getSelectedPosition());
            navigationSelectedPosition = savedInstanceState.getInt(UIDHelper.NAVIGATION_RANGE, getNavigationListAdapter(colorRange).getSelectedPosition());
            accentSelectedPosition = savedInstanceState.getInt(UIDHelper.ACCENT_RANGE, getAccentColorAdapter(colorRange).getSelectedPosition());
            colorRange = ColorRange.values()[colorRangeSelectedPosition];

            initNavigationColor(navigationSelectedPosition);
            initContentColor(contentSelectedPosition);
            initAccentColor(accentSelectedPosition);
            notifyNavigationSettingsChanged();
        } else {
            colorRange = themeHelper.initColorRange();
            navigationColor = themeHelper.initNavigationRange();
            contentColor = themeHelper.initContentTonalRange();
            accentRange = themeHelper.initAccentRange();
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

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {

    }

    private void initContentColor(final int colorRangeSelectedPosition) {
        contentColor = ContentColor.values()[colorRangeSelectedPosition];
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putInt(UIDHelper.COLOR_RANGE, colorRange.ordinal());
        outState.putInt(UIDHelper.CONTENT_TONAL_RANGE, getSelectedContentTonalRangePosition());
        outState.putInt(UIDHelper.NAVIGATION_RANGE, getSelectedNavigationPosition());
        outState.putInt(UIDHelper.ACCENT_RANGE, getSelectedAccentPosition());
        super.onSaveInstanceState(outState);
    }

    private int getSelectedAccentPosition() {
        return accentSelectedPosition == 0 ? AccentRange.values().length - accentRange.ordinal() - 1 : accentSelectedPosition;
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
                buildAccentColorsList(colorRange);

            }
        }, colorPickerWidth);
        colorRangeAdapter.setSelected(colorRangeSelectedPosition == 0 ? colorRange.ordinal() : colorRangeSelectedPosition);
        return colorRangeAdapter;
    }

    private void updateNavigationRangeColors() {
        contentTonalRangeAdapter.setColorModels(themeColorHelper.getContentColorModelList(colorRange, getContext()));
    }

    private void updateTonalRangeColors() {
        navigationListAdapter.setColorModels(themeColorHelper.getNavigationColorModelsList(colorRange, getContext()));
    }

    private void buildContentTonalRangeList(final ColorRange changedColorRange) {
        tonalRangeListview.setAdapter(getContentTonalRangeAdapter(changedColorRange));

        setLayoutOrientation(tonalRangeListview);
    }

    @NonNull
    private ThemeColorAdapter getContentTonalRangeAdapter(final ColorRange changedColorRange) {
        contentTonalRangeAdapter = new ThemeColorAdapter(themeColorHelper.getContentColorModelList(changedColorRange, getContext()), new ThemeChangedListener() {
            @Override
            public void onThemeSettingsChanged(final String tonalRangeChanged) {
                contentColor = getContentTonalRangeByPosition();
            }
        }, colorPickerWidth);
        contentTonalRangeAdapter.setSelected(getSelectedContentTonalRangePosition());
        return contentTonalRangeAdapter;
    }

    private ContentColor getContentTonalRangeByPosition() {
        final ThemeColorAdapter adapter = (ThemeColorAdapter) tonalRangeListview.getAdapter();
        contentSelectedPosition = adapter.getSelectedPosition();
        final ContentColor[] values = ContentColor.values();
        return values[values.length - contentSelectedPosition - 1];
    }

    private int getSelectedContentTonalRangePosition() {
        return contentSelectedPosition == 0 ? (ContentColor.values().length - contentColor.ordinal() - 1) : contentSelectedPosition;
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
    }

    private void initNavigationColor(final int selectedPosition) {
        NavigationColor[] values = NavigationColor.values();
        navigationColor = NavigationColor.values()[values.length - selectedPosition - 1];
    }

    private int getSelectedNavigationPosition() {
        return navigationSelectedPosition == 0 ? NavigationColor.values().length - (navigationColor != null ? navigationColor.ordinal() : 0) - 1 : navigationSelectedPosition;
    }

    private void buildAccentColorsList(final ColorRange colorRange) {
        final ThemeColorAdapter accentThemeAdapter = getAccentColorAdapter(colorRange);
        final int selection = getSelectedPositionFromList();
        initAccentColor(selection);
        accentThemeAdapter.setSelected(selection);
        accentColorRangeList.setAdapter(accentThemeAdapter);
        setLayoutOrientation(accentColorRangeList);
    }

    @NonNull
    private ThemeColorAdapter getAccentColorAdapter(ColorRange colorRange) {
        accentColorsList = themeColorHelper.getAccentColorsList(colorRange, getResources(), getContext().getPackageName());

        return new ThemeColorAdapter(accentColorsList, new ThemeChangedListener() {
            @Override
            public void onThemeSettingsChanged(final String changedColorRange) {
                final ThemeColorAdapter accentColorRangeListAdapter = (ThemeColorAdapter) accentColorRangeList.getAdapter();
                final int selectedPosition = accentColorRangeListAdapter.getSelectedPosition();
                accentSelectedPosition = selectedPosition;
                initAccentColor(selectedPosition);
                updateThemeSettingsLayout();
            }
        }, colorPickerWidth);
    }

    private int getSelectedPositionFromList() {
        final String shortName = themeColorHelper.getShortName(accentRange.name());
        for (ColorModel colorModel : accentColorsList) {
            if (colorModel.getTitle().equals(shortName)) {
                return accentColorsList.indexOf(colorModel);
            }
        }
        return 0;
    }

    private void initAccentColor(final int selectedPosition) {
        final ColorModel colorModel = accentColorsList.get(selectedPosition);
        accentRange = AccentRange.valueOf(colorModel.getName());
    }

    private void setLayoutOrientation(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

}

