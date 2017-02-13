package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.philips.platform.catalogapp.dataUtils.GridAdapter;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.dataUtils.GridData;
import com.philips.platform.catalogapp.dataUtils.GridDataHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GridViewFragment extends BaseFragment {

    private GridView gridView;
    private ArrayList<GridData> cardList;
    GridAdapter adapter;


    @Override
    public int getPageTitle() {
        return R.string.page_title_gridView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        GridDataHelper gridDataHelper = new GridDataHelper(getContext());

        //TODO:Replace below mocks with input from GridViewSettingsFragment
        gridDataHelper.setDarkBackgroundEnabled(true);
        gridDataHelper.setEnlargedGutterEnabled(true);
        gridDataHelper.setSecondaryActionEnabled(true);
        gridDataHelper.setSetDisableStateEnabled(false);

        View view = inflater.inflate(R.layout.fragment_gridview, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);

        int color = gridDataHelper.isDarkBackgroundEnabled() ? R.color.uidColorBlack : R.color.uidColorWhite;
        gridView.setBackgroundResource(color);

        int spacing = gridDataHelper.isEnlargedGutterEnabled() ? R.dimen.grid_spacing_enlarged : R.dimen.grid_spacing_normal;
        gridView.setHorizontalSpacing(getResources().getDimensionPixelSize(spacing));
        gridView.setVerticalSpacing(getResources().getDimensionPixelSize(spacing));

        populateList();
        adapter = new GridAdapter(getContext(), cardList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                GridData gridData = cardList.get(position);
                gridData.toggleFavorite();
                cardList.remove(position);
                cardList.add(position,gridData);
                gridView.invalidateViews();
                adapter.updateGrid(cardList);

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void populateList() {
        Context mContext = getContext();
        cardList = new ArrayList<>();
        cardList.add(new GridData(R.drawable.gridview_asset_1, "1. " + mContext.getString(R.string.gridView_title), mContext.getString(R.string.gridView_description), false));
        cardList.add(new GridData(R.drawable.gridview_asset_2, "2. " + mContext.getString(R.string.gridView_title), mContext.getString(R.string.gridView_description), false));
        cardList.add(new GridData(R.drawable.gridview_asset_3, "3. " + mContext.getString(R.string.gridView_title), mContext.getString(R.string.gridView_description), false));
        cardList.add(new GridData(R.drawable.gridview_asset_4, "4. " + mContext.getString(R.string.gridView_title), mContext.getString(R.string.gridView_description), false));
        cardList.add(new GridData(R.drawable.gridview_asset_5, "5. " + mContext.getString(R.string.gridView_title), mContext.getString(R.string.gridView_description), false));
        cardList.add(new GridData(R.drawable.gridview_asset_6, "6. " + mContext.getString(R.string.gridView_title), mContext.getString(R.string.gridView_description), false));
        cardList.add(new GridData(R.drawable.gridview_asset_7, "7. " + mContext.getString(R.string.gridView_title), mContext.getString(R.string.gridView_description), false));
    }

}
