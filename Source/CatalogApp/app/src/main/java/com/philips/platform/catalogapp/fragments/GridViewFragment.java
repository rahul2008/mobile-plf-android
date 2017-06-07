package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.dataUtils.GridAdapter;
import com.philips.platform.catalogapp.dataUtils.GridData;
import com.philips.platform.catalogapp.dataUtils.GridDataHelper;
import com.philips.platform.catalogapp.databinding.FragmentGridviewBinding;
import com.philips.platform.uid.thememanager.ThemeUtils;

import java.util.ArrayList;

public class GridViewFragment extends BaseFragment {

    @Override
    public int getPageTitle() {
        return R.string.page_title_gridView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final FragmentGridviewBinding fragmentGridviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_gridview, container, false);
        fragmentGridviewBinding.setFragment(this);

        GridDataHelper gridDataHelper = new GridDataHelper(getContext());
        updateHeaderAndBackground(gridDataHelper, fragmentGridviewBinding);
        updateGutterSize(gridDataHelper, fragmentGridviewBinding);

        initGridAdapter(fragmentGridviewBinding);

        return fragmentGridviewBinding.getRoot();
    }

    public void onSettingsClicked() {
        showFragment(new GridViewSettingsFragment());
    }

    protected void updateHeaderAndBackground(final GridDataHelper gridDataHelper, final FragmentGridviewBinding fragmentGridviewBinding) {
        Resources.Theme theme = ThemeUtils.getTheme(fragmentGridviewBinding.gridView.getContext(), null);
        ColorStateList colorStateList = ThemeUtils.buildColorStateList(fragmentGridviewBinding.gridView.getResources(), theme, R.color.uid_gridview_background_selector);
        final int selectedStateColor = colorStateList.getDefaultColor();
        final boolean darkBackgroundEnabled = gridDataHelper.isDarkBackgroundEnabled();
        int color = darkBackgroundEnabled ? selectedStateColor : Color.WHITE;
        fragmentGridviewBinding.gridView.setBackgroundColor(color);

        fragmentGridviewBinding.uidGridviewHeader.setBackgroundColor(color);
        final TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
                new int[]{darkBackgroundEnabled ? R.attr.uidGridViewBackgroundDarkHeaderTextColor : R.attr.uidGridViewBackgroundLightHeaderTextColor});

        fragmentGridviewBinding.uidGridviewHeader.setTextColor(typedArray.getColor(0, Color.WHITE));
        typedArray.recycle();
    }

    protected void updateGutterSize(final GridDataHelper gridDataHelper, final FragmentGridviewBinding fragmentGridviewBinding) {
        int spacing = gridDataHelper.isEnlargedGutterEnabled() ? R.dimen.grid_spacing_enlarged : R.dimen.grid_spacing_normal;
        fragmentGridviewBinding.gridView.setHorizontalSpacing(getResources().getDimensionPixelSize(spacing));
        fragmentGridviewBinding.gridView.setVerticalSpacing(getResources().getDimensionPixelSize(spacing));
    }

    protected void initGridAdapter(final FragmentGridviewBinding fragmentGridviewBinding) {
        final ArrayList<GridData> cardList = populateList();
        final GridAdapter adapter = new GridAdapter(getContext(), cardList);
        fragmentGridviewBinding.gridView.setAdapter(adapter);
        fragmentGridviewBinding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                GridData gridData = cardList.get(position);
                gridData.toggleFavorite();
                cardList.remove(position);
                cardList.add(position, gridData);
                fragmentGridviewBinding.gridView.invalidateViews();
                adapter.updateGrid(cardList);
            }
        });
    }

    public ArrayList<GridData> populateList() {
        Context mContext = getContext();
        final ArrayList<GridData> cardList = new ArrayList<>();
        cardList.add(new GridData(R.drawable.gridview_asset_1, mContext.getString(R.string.gridView_title_short), mContext.getString(R.string.gridView_description_short), false));
        cardList.add(new GridData(R.drawable.gridview_asset_2, mContext.getString(R.string.gridView_title_short), mContext.getString(R.string.gridView_description_short), false));
        cardList.add(new GridData(R.drawable.gridview_asset_3, mContext.getString(R.string.gridView_title_short), mContext.getString(R.string.gridView_description_short), false));
        cardList.add(new GridData(R.drawable.gridview_asset_4, mContext.getString(R.string.gridView_title_short), mContext.getString(R.string.gridView_description_short), false));
        cardList.add(new GridData(R.drawable.gridview_asset_5, mContext.getString(R.string.gridView_title_short), mContext.getString(R.string.gridView_description_short), false));
        cardList.add(new GridData(R.drawable.gridview_asset_6, mContext.getString(R.string.gridView_title_short), mContext.getString(R.string.gridView_description_short), false));
        cardList.add(new GridData(R.drawable.gridview_asset_7, mContext.getString(R.string.gridView_title_long), mContext.getString(R.string.gridView_description_long), false));
        return cardList;
    }
}
