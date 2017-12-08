/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.themesettings;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.*;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.events.AccentColorChangedEvent;
import com.philips.platform.catalogapp.events.ColorRangeChangedEvent;
import com.philips.platform.catalogapp.events.ContentTonalRangeChangedEvent;
import com.philips.platform.catalogapp.events.NavigationColorChangedEvent;
import com.philips.platform.catalogapp.fragments.BaseFragment;
import com.philips.platform.uid.drawable.SeparatorDrawable;
import com.philips.platform.uid.thememanager.*;
import org.greenrobot.eventbus.EventBus;

import java.util.List;

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
        view.setVisibility(View.GONE);
        ButterKnife.bind(this, view);
        themeColorHelper = new ThemeColorHelper();
        themeHelper = new ThemeHelper(PreferenceManager.getDefaultSharedPreferences(getContext()), getContext());

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

        ((MainActivity)getActivity()).getSideBarController().getSideBar().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
        ((MainActivity)getActivity()).getSideBarController().getSideBar().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.END);

        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSeparatorBackground(view);
    }

    private void setSeparatorBackground(final View view) {
        View viewById = view.findViewById(R.id.divider1);
        final SeparatorDrawable separatorDrawable = new SeparatorDrawable(getContext());
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
        EventBus.getDefault().post(new AccentColorChangedEvent("accentChanged", accentRange));
    }

    private void setLayoutOrientation(final RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_theme_settings;
    }
}
