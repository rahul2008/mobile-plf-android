/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.prdemoapp.theme.themesettings;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.philips.platform.prdemoapp.theme.events.AccentColorChangedEvent;
import com.philips.platform.prdemoapp.theme.events.ColorRangeChangedEvent;
import com.philips.platform.prdemoapp.theme.events.ContentTonalRangeChangedEvent;
import com.philips.platform.prdemoapp.theme.events.NavigationColorChangedEvent;
import com.philips.platform.prdemoapp.theme.fragments.BaseFragment;
import com.philips.platform.prdemoapplibrary.R;
import com.philips.platform.uid.drawable.SeparatorDrawable;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.UIDHelper;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.ButterKnife;

public class ThemeSettingsFragment extends BaseFragment {

    RecyclerView colorRangeListview;

    RecyclerView tonalRangeListview;

    RecyclerView notificationBarListview;

    RecyclerView accentColorRangeList;

    ViewGroup warningText;

    private ThemeColorHelper themeColorHelper;

    private int colorPickerWidth = 48;

    private ThemeColorAdapter colorRangeAdapter;
    private ThemeColorAdapter contentTonalRangeAdapter;
    private ThemeColorAdapter navigationListAdapter;
    private ContentColor contentColor = ContentColor.ULTRA_LIGHT;

    private ColorRange colorRange = ColorRange.GROUP_BLUE;
    private ThemeHelper themeHelper;
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
        colorRangeListview=(RecyclerView)view.findViewById(R.id.colorRangeList);
        tonalRangeListview=(RecyclerView)view.findViewById(R.id.tonalRangeList);
        notificationBarListview=(RecyclerView)view.findViewById(R.id.notificationBarList);
        accentColorRangeList=(RecyclerView)view.findViewById(R.id.accentColorRangeList);
        warningText=(ViewGroup) view.findViewById(R.id.warningText);

        view.setVisibility(View.GONE);
        ButterKnife.bind(this, view);
        themeColorHelper = new ThemeColorHelper();
        themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(getActivity()));

        if (savedInstanceState != null) {
            colorRangeSelectedPosition = savedInstanceState.getInt(UIDHelper.COLOR_RANGE, getColorRangeAdapter().getSelectedPosition());
            contentSelectedPosition = savedInstanceState.getInt(UIDHelper.CONTENT_TONAL_RANGE, getContentTonalRangeAdapter(colorRange).getSelectedPosition());
            navigationSelectedPosition = savedInstanceState.getInt(UIDHelper.NAVIGATION_RANGE, getNavigationListAdapter(colorRange).getSelectedPosition());
            accentSelectedPosition = savedInstanceState.getInt(UIDHelper.ACCENT_RANGE, getAccentColorAdapter(colorRange).getSelectedPosition());
            colorRange = ColorRange.values()[colorRangeSelectedPosition];
            EventBus.getDefault().post(new ColorRangeChangedEvent(contentColor.name().toString(), colorRange));

            initNavigationColor(navigationSelectedPosition);
            initContentColor(contentSelectedPosition);
            initAccentColor(accentSelectedPosition);
            notifyNavigationSettingsChanged();
        } else {
            colorRange = themeHelper.initColorRange();
            navigationColor = themeHelper.initNavigationRange();
            contentColor = themeHelper.initContentTonalRange();
            accentRange = themeHelper.initAccentRange();
//            setSeparatorBackground(view);
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

    private void initContentColor(final int colorRangeSelectedPosition) {
        contentColor = ContentColor.values()[colorRangeSelectedPosition];
        EventBus.getDefault().post(new ContentTonalRangeChangedEvent(contentColor.name(), contentColor));
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
        menu.findItem(R.id.menu_theme_settings).setVisible(false);
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

                EventBus.getDefault().post(new ColorRangeChangedEvent(contentColor.name().toString(), colorRange));
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
                EventBus.getDefault().post(new ContentTonalRangeChangedEvent(contentColor.name().toString(), contentColor));
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
        EventBus.getDefault().post(new NavigationColorChangedEvent(contentColor.name().toString(), navigationColor));
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
        EventBus.getDefault().post(new AccentColorChangedEvent("accentChanged", accentRange));
    }

    private void setLayoutOrientation(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_theme_settings;
    }
}

