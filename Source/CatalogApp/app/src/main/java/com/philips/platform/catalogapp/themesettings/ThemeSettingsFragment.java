/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.themesettings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.ThemeColorHelper;
import com.philips.platform.catalogapp.fragments.BaseFragment;
import com.philips.platform.uit.thememanager.ContentTonalRange;
import com.philips.platform.uit.thememanager.UITHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ThemeSettingsFragment extends BaseFragment {

    String colorRange = "group_blue";
    String tonalRange;

    @Bind(R.id.colorRangeList)
    RecyclerView colorRangeListview;

    @Bind(R.id.tonalRangeList)
    RecyclerView tonalRangeListview;

    @Bind(R.id.notificationBarList)
    RecyclerView notificationBarListview;

    @Bind(R.id.accentColorRangeList)
    RecyclerView accentColorRangeList;

    @Bind(R.id.warningText)
    TextView warningText;

    private ThemeColorHelper themeColorHelper;

    int colorPickerWidth = 48;
    SharedPreferences defaultSharedPreferences;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_theme_settings, container, false);
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        ButterKnife.bind(this, view);
        themeColorHelper = new ThemeColorHelper();
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                initColorPickerWidth();
                buildColorRangeList();
                updateThemeSettingsLayout();
            }
        });

        return view;
    }

    private void initColorPickerWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        setLayoutWidthForTablet(widthPixels);

        float pageMargin = getResources().getDimension(R.dimen.themeSettingsPageMargin);
        colorPickerWidth = (int) ((widthPixels - pageMargin) / 8);
    }

    private void setLayoutWidthForTablet(int widthPixels) {
        if (widthPixels > 1200) {
            final View settingLayout = getView().findViewById(R.id.setting_layout);
            final ViewGroup.LayoutParams layoutParams = settingLayout.getLayoutParams();
            layoutParams.width = widthPixels / 2;
            widthPixels = widthPixels / 2;
        }
    }

    private void updateThemeSettingsLayout() {
        buildContentTonalRangeList(colorRange);
        buildNavigationList(colorRange);

        buildAccentColorsList(colorRange);
    }

    private void buildColorRangeList() {
        colorRangeListview.setAdapter(new ThemeColorAdapter(themeColorHelper.getColorRangeItemsList(), new ThemeChangedListener() {
            @Override
            public void onColorRangeChanged(final String changedColorRange) {
                colorRange = changedColorRange;
                final SharedPreferences.Editor edit = defaultSharedPreferences.edit();
                edit.putString(UITHelper.COLOR_RANGE, changedColorRange.toUpperCase());
                edit.commit();
                updateThemeSettingsLayout();
            }
        }, colorPickerWidth));

        setLayoutOrientation(colorRangeListview);
    }

    private void buildContentTonalRangeList(final String changedColorRange) {
        tonalRangeListview.setAdapter(new ThemeColorAdapter(themeColorHelper.getContentTonalRangeItemsList(changedColorRange, getContext()), new ThemeChangedListener() {
            @Override
            public void onColorRangeChanged(final String tonalRangeChanged) {
                final ThemeColorAdapter  adapter = (ThemeColorAdapter) tonalRangeListview.getAdapter();
                final int selectedPosition = adapter.getSelectedPosition();
                final ContentTonalRange[] values = ContentTonalRange.values();
                final ContentTonalRange tonalRange = values[selectedPosition];
                final SharedPreferences.Editor edit = defaultSharedPreferences.edit();
                edit.putString(UITHelper.CONTENT_TONAL_RANGE, tonalRange.name());
                edit.commit();
                ThemeSettingsFragment.this.tonalRange = tonalRangeChanged;
            }
        }, colorPickerWidth));

        setLayoutOrientation(tonalRangeListview);
    }

    private void buildNavigationList(final String colorRange) {
        notificationBarListview.setAdapter(new ThemeColorAdapter(themeColorHelper.getNavigationColorRangeItemsList(colorRange, getContext()), new ThemeChangedListener() {
            @Override
            public void onColorRangeChanged(final String changedColorRange) {
                final ThemeColorAdapter  adapter = (ThemeColorAdapter) notificationBarListview.getAdapter();
                final int selectedPosition = adapter.getSelectedPosition();

            }
        }, colorPickerWidth));

        setLayoutOrientation(notificationBarListview);
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

    @Override
    public int getTitle() {
        return R.string.tittle_theme_settings;
    }
}

