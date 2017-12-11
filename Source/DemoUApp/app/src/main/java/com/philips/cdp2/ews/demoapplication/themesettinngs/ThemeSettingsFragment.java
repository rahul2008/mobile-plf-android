/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.demoapplication.themesettinngs;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.philips.cdp2.ews.demoapplication.EWSDemoUActivity;
import com.philips.cdp2.ews.demoapplication.R;
import com.philips.cdp2.ews.microapp.EWSActionBarListener;
import com.philips.platform.uid.drawable.SeparatorDrawable;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.util.List;

public class ThemeSettingsFragment extends Fragment {

    private RecyclerView colorRangeListview;

    private RecyclerView tonalRangeListview;

    private RecyclerView notificationBarListview;

    private RecyclerView accentColorRangeList;

    ViewGroup warningText;

    private ThemeColorHelper themeColorHelper;

    private int colorPickerWidth = 48;

    private ThemeColorAdapter contentTonalRangeAdapter;
    private ThemeColorAdapter navigationListAdapter;
    private ContentColor contentColor = ContentColor.ULTRA_LIGHT;

    private ColorRange colorRange = ColorRange.GROUP_BLUE;
    private int colorRangeSelectedPosition;
    private int contentSelectedPosition;
    private int navigationSelectedPosition;
    private NavigationColor navigationColor;
    private AccentRange accentRange;
    private List<ColorModel> accentColorsList;
    private int accentSelectedPosition;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_theme_settings, container, false);
        view.setVisibility(View.GONE);
        colorRangeListview = view.findViewById(R.id.colorRangeList);
        tonalRangeListview = view.findViewById(R.id.tonalRangeList);
        notificationBarListview = view.findViewById(R.id.notificationBarList);
        accentColorRangeList = view.findViewById(R.id.accentColorRangeList);
        warningText = view.findViewById(R.id.warningText);
        themeColorHelper = new ThemeColorHelper();
        ThemeHelper themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(getActivity()));

        colorRange = themeHelper.initColorRange();
        navigationColor = themeHelper.initNavigationRange();
        contentColor = themeHelper.initContentTonalRange();
        accentRange = themeHelper.initAccentRange();
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
    public void onResume() {
        super.onResume();
        ((EWSActionBarListener) getActivity()).updateActionBar("EWS Theme", true);
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSeparatorBackground(view);
    }

    private void setSeparatorBackground(final View view) {
        View viewById = view.findViewById(R.id.divider1);
        final SeparatorDrawable separatorDrawable = new SeparatorDrawable(getActivity());
        viewById.setBackground(separatorDrawable);
        viewById = view.findViewById(R.id.divider2);
        viewById.setBackground(separatorDrawable);
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

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_set_theme_settings).setVisible(true);
    }

    @NonNull
    private ThemeColorAdapter getColorRangeAdapter() {
        ThemeColorAdapter colorRangeAdapter = new ThemeColorAdapter(themeColorHelper.getColorRangeItemsList(), new ThemeChangedListener() {
            @Override
            public void onThemeSettingsChanged(final String changedColorRange) {
                colorRange = ColorRange.valueOf(changedColorRange.toUpperCase());

                updateTonalRangeColors();
                updateNavigationRangeColors();
                buildAccentColorsList(colorRange);
                ((EWSDemoUActivity) getActivity()).updateColorRange(colorRange);
            }
        }, colorPickerWidth);
        colorRangeAdapter.setSelected(colorRangeSelectedPosition == 0 ? colorRange.ordinal() : colorRangeSelectedPosition);
        return colorRangeAdapter;
    }

    private void updateNavigationRangeColors() {
        contentTonalRangeAdapter.setColorModels(themeColorHelper.getContentColorModelList(colorRange, getActivity()));
    }

    private void updateTonalRangeColors() {
        navigationListAdapter.setColorModels(themeColorHelper.getNavigationColorModelsList(colorRange, getActivity()));
    }

    private void buildContentTonalRangeList(final ColorRange changedColorRange) {
        tonalRangeListview.setAdapter(getContentTonalRangeAdapter(changedColorRange));

        setLayoutOrientation(tonalRangeListview);
    }

    @NonNull
    private ThemeColorAdapter getContentTonalRangeAdapter(final ColorRange changedColorRange) {
        contentTonalRangeAdapter = new ThemeColorAdapter(themeColorHelper.getContentColorModelList(changedColorRange, getActivity()), new ThemeChangedListener() {
            @Override
            public void onThemeSettingsChanged(final String tonalRangeChanged) {
                contentColor = getContentTonalRangeByPosition();
                ((EWSDemoUActivity) getActivity()).updateContentColor(contentColor);
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
        navigationListAdapter = new ThemeColorAdapter(themeColorHelper.getNavigationColorModelsList(colorRange, getActivity()), new ThemeChangedListener() {
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
        ((EWSDemoUActivity) getActivity()).updateNavigationColor(navigationColor);
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
        accentColorsList = themeColorHelper.getAccentColorsList(colorRange, getResources(), getActivity().getPackageName());

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
        ((EWSDemoUActivity) getActivity()).updateAccentColor(accentRange);
    }

    private void setLayoutOrientation(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    public int getPageTitle() {
        return R.string.page_title_theme_settings;
    }
}

