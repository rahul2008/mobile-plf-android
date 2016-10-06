/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.catalogapp.themesettings.ThemeChangedListener;
import com.philips.platform.catalogapp.themesettings.ThemeColorAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ThemeSettingsFragment extends Fragment {

    String colorRange = "group_blue";
    String tonalRange;

    @Bind(R.id.colorRangeList)
    RecyclerView colorRangeListview;

    @Bind(R.id.tonalRangeList)
    RecyclerView tonalRangeListview;

    @Bind(R.id.notificationBarList)
    RecyclerView notificationBarListview;

    @Bind(R.id.primaryControlsColorList)
    RecyclerView primaryControlsListview;

    @Bind(R.id.accentColorRangeList)
    RecyclerView accentColorRangeList;

    @Bind(R.id.warningText)
    TextView warningText;

    private ThemeColorHelper themeColorHelper;

    int colorPickerWidth = 48;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_theme_settings, container, false);

        ButterKnife.bind(this, view);
        themeColorHelper = new ThemeColorHelper();
        initColorPickerWidth();
        buildColorRangeList();
        updateThemeSettingsLayout();

        return view;
    }

    private void initColorPickerWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        float pageMargin = getResources().getDimension(R.dimen.themeSettingsPageMargin);
        colorPickerWidth = (int) ((widthPixels - pageMargin) / 8);
    }

    private void updateThemeSettingsLayout() {
        buildTonalRangeList(colorRange);
        buildNavigationBar(colorRange);
        buildPrimaryColorsList(colorRange);

        buildAccentColorsList(colorRange);
        warningText.setVisibility(View.VISIBLE);
    }

    private void buildColorRangeList() {
        colorRangeListview.setAdapter(new ThemeColorAdapter(themeColorHelper.getColorRangeItemsList(), new ThemeChangedListener() {
            @Override
            public void onColorRangeChanged(final String changedColorRange) {
                colorRange = changedColorRange;
                updateThemeSettingsLayout();
            }
        }, colorPickerWidth));

        setLayoutOrientation(colorRangeListview);
    }

    private void buildTonalRangeList(final String changedColorRange) {
        tonalRangeListview.setAdapter(new ThemeColorAdapter(themeColorHelper.getTonalRangeItemsList(changedColorRange, getContext()), new ThemeChangedListener() {
            @Override
            public void onColorRangeChanged(final String tonalRangeChanged) {
                tonalRange = tonalRangeChanged;
                updateThemeSettingsLayout();
            }
        }, colorPickerWidth));

        setLayoutOrientation(tonalRangeListview);
    }

    private void buildNavigationBar(final String colorRange) {
        notificationBarListview.setAdapter(new ThemeColorAdapter(themeColorHelper.getNavigationColorRangeItemsList(colorRange, getContext()), new ThemeChangedListener() {
            @Override
            public void onColorRangeChanged(final String changedColorRange) {
                updateThemeSettingsLayout();
            }
        }, colorPickerWidth));

        setLayoutOrientation(notificationBarListview);
    }

    private void buildPrimaryColorsList(final String colorRange) {
        primaryControlsListview.setAdapter(new ThemeColorAdapter(themeColorHelper.getPrimaryColors(getResources(), colorRange, getContext().getPackageName()), new ThemeChangedListener() {
            @Override
            public void onColorRangeChanged(final String changedColorRange) {
                updateThemeSettingsLayout();
            }
        }, colorPickerWidth));
        setLayoutOrientation(primaryControlsListview);
    }

    private void buildAccentColorsList(final String colorRange) {
        accentColorRangeList.setAdapter(new ThemeColorAdapter(themeColorHelper.getAccentColorsList(getContext(), colorRange), new ThemeChangedListener() {
            @Override
            public void onColorRangeChanged(final String changedColorRange) {
                updateThemeSettingsLayout();
            }
        }, colorPickerWidth));

        setLayoutOrientation(accentColorRangeList);
    }

    private void setLayoutOrientation(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }
}

