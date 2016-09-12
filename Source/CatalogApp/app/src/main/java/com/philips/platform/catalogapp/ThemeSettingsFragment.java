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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.themesettings.ThemeChangedListener;
import com.philips.platform.catalogapp.themesettings.ThemeColorAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ThemeSettingsFragment extends Fragment implements ThemeChangedListener {

    @Bind(R.id.colorRangeList)
    RecyclerView colorRangeListview;

    @Bind(R.id.tonalRangeList)
    RecyclerView tonalRangeListview;

    @Bind(R.id.notificationBarList)
    RecyclerView notificationBarListview;

    @Bind(R.id.primaryControlsColorList)
    RecyclerView primaryControlsListview;

    @Bind(R.id.dimColorList)
    RecyclerView dimColorListview;

    @Bind(R.id.accentColorRangeList)
    RecyclerView accentColorRangeList;

    private ColorListHelper colorListHelper;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_theme_settings, container, false);

        ButterKnife.bind(this, view);
        colorListHelper = new ColorListHelper();
        buildColorRangeList();
        buildTonalRangeList("group_blue");
        buildNavigationBar("group_blue");
        buildPrimaryColorsList("group_blue");
        buildDimColorsList("group_blue");

        buildAccentColorsList("group_blue");

        return view;
    }

    private void buildAccentColorsList(final String colorRange) {
        accentColorRangeList.setAdapter(new ThemeColorAdapter(colorListHelper.getAccentColorsList(getContext(), colorRange), this));

        setLayoutOrientation(accentColorRangeList);
    }

    private void buildDimColorsList(final String colorRange) {
        dimColorListview.setAdapter(new ThemeColorAdapter(colorListHelper.getDimColors(getContext().getPackageName(), getResources(), colorRange), this));

        setLayoutOrientation(dimColorListview);
    }

    private void buildPrimaryColorsList(final String colorRange) {
        primaryControlsListview.setAdapter(new ThemeColorAdapter(colorListHelper.getPrimaryColors(getResources(), colorRange, getContext().getPackageName()), this));
        setLayoutOrientation(primaryControlsListview);
    }

    private void buildNavigationBar(final String colorRange) {
        notificationBarListview.setAdapter(new ThemeColorAdapter(colorListHelper.getNavigationColorRangeItemsList(colorRange, getContext()), this));

        setLayoutOrientation(notificationBarListview);
    }

    private void buildTonalRangeList(final String changedColorRange) {
        tonalRangeListview.setAdapter(new ThemeColorAdapter(colorListHelper.getTonalRangeItemsList(changedColorRange, getContext()), this));

        setLayoutOrientation(tonalRangeListview);
    }

    private void setLayoutOrientation(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void buildColorRangeList() {
        colorRangeListview.setAdapter(new ThemeColorAdapter(colorListHelper.getColorRangeItemsList(), this));

        setLayoutOrientation(colorRangeListview);
    }

    @Override
    public void onColorRangeChanged(final String changedColorRange) {
        buildTonalRangeList(changedColorRange);
        buildNavigationBar(changedColorRange);
        buildNavigationBar(changedColorRange);
        buildPrimaryColorsList(changedColorRange);
        buildDimColorsList(changedColorRange);
    }
}

