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
import com.philips.platform.catalogapp.ThemeColorHelper;
import com.philips.platform.catalogapp.ThemeHelper;
import com.philips.platform.catalogapp.fragments.BaseFragment;
import com.philips.platform.uit.thememanager.ColorRange;
import com.philips.platform.uit.thememanager.ContentTonalRange;
import com.philips.platform.uit.thememanager.NavigationColor;
import com.philips.platform.uit.thememanager.UITHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ThemeSettingsFragment extends BaseFragment {

    ColorRange colorRange = ColorRange.GROUP_BLUE;

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
    SharedPreferences defaultSharedPreferences;
    private ThemeColorAdapter themeColorAdapter;
    private ThemeColorAdapter tonalRangeAdapter;
    private ThemeColorAdapter navigationListAdapter;

    private ContentTonalRange contentTonalRange = ContentTonalRange.ULTRA_LIGHT;
    private NavigationColor navigationColor = NavigationColor.ULTRA_LIGHT;
    private ThemeHelper themeHelper;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_theme_settings, container, false);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        ButterKnife.bind(this, view);
        themeColorHelper = new ThemeColorHelper();
        themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(getContext()));
        colorRange = themeHelper.initColorRange();
        navigationColor = themeHelper.initNavigationRange();
        contentTonalRange = themeHelper.initTonalRange();
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                initColorPickerWidth();
                buildColorRangeList();
                buildContentTonalRangeList(colorRange);
                buildNavigationList(colorRange);

                buildAccentColorsList(colorRange);
            }
        });

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
            public void onColorRangeChanged(final String changedColorRange) {
                colorRange = ColorRange.valueOf(changedColorRange.toUpperCase());
                saveThemeValues(UITHelper.COLOR_RANGE, changedColorRange.toUpperCase());
                updateTonalRangeColors();
                updateNavigationRangeColors();
            }
        }, colorPickerWidth);
        themeColorAdapter.setSelected(colorRange.ordinal());
        return themeColorAdapter;
    }

    private void updateNavigationRangeColors() {
        tonalRangeAdapter.setColorModels(themeColorHelper.getContentTonalRangeItemsList(colorRange, getContext()));
    }

    private void updateTonalRangeColors() {
        navigationListAdapter.setColorModels(themeColorHelper.getNavigationColorRangeItemsList(colorRange, getContext()));
    }

    private void buildContentTonalRangeList(final ColorRange changedColorRange) {
        tonalRangeListview.setAdapter(getTonalRangeAdapter(changedColorRange));

        setLayoutOrientation(tonalRangeListview);
    }

    @NonNull
    private ThemeColorAdapter getTonalRangeAdapter(final ColorRange changedColorRange) {
        tonalRangeAdapter = new ThemeColorAdapter(themeColorHelper.getContentTonalRangeItemsList(changedColorRange, getContext()), new ThemeChangedListener() {
            @Override
            public void onColorRangeChanged(final String tonalRangeChanged) {
                final ContentTonalRange tonalRange = getContentTonalRangeByPosition();
                saveThemeValues(UITHelper.CONTENT_TONAL_RANGE, tonalRange.name());
            }
        }, colorPickerWidth);
        tonalRangeAdapter.setSelected(getSelectedContentTonalRangePosition());
        return tonalRangeAdapter;
    }

    private void saveThemeValues(final String contentTonalRange, final String name) {
        final SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putString(contentTonalRange, name);
        edit.commit();
    }

    private ContentTonalRange getContentTonalRangeByPosition() {
        final ThemeColorAdapter adapter = (ThemeColorAdapter) tonalRangeListview.getAdapter();
        final int selectedPosition = adapter.getSelectedPosition();
        final ContentTonalRange[] values = ContentTonalRange.values();
        return values[values.length - selectedPosition - 1];
    }

    private int getSelectedContentTonalRangePosition() {
        return contentTonalRange.values().length - contentTonalRange.ordinal() - 1;
    }

    private void buildNavigationList(final ColorRange colorRange) {
        notificationBarListview.setAdapter(getNavigationListAdapter(colorRange));

        setLayoutOrientation(notificationBarListview);
    }

    @NonNull
    private ThemeColorAdapter getNavigationListAdapter(final ColorRange colorRange) {
        navigationListAdapter = new ThemeColorAdapter(themeColorHelper.getNavigationColorRangeItemsList(colorRange, getContext()), new ThemeChangedListener() {
            @Override
            public void onColorRangeChanged(final String changedColorRange) {
                final ThemeColorAdapter adapter = (ThemeColorAdapter) notificationBarListview.getAdapter();
                final int selectedPosition = adapter.getSelectedPosition();

                NavigationColor[] values = NavigationColor.values();
                NavigationColor navigationColor = values[values.length - selectedPosition - 1];
                final SharedPreferences.Editor edit = defaultSharedPreferences.edit();
                edit.putString(UITHelper.NAVIGATION_RANGE, navigationColor.name());
                edit.commit();
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
            public void onColorRangeChanged(final String changedColorRange) {
                updateThemeSettingsLayout();
            }
        }, colorPickerWidth));

        setLayoutOrientation(accentColorRangeList);
    }

    private void setLayoutOrientation(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getTitle() {
        return R.string.tittle_theme_settings;
    }
}

