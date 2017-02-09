package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.philips.platform.catalogapp.dataUtils.CardAdapter;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.dataUtils.CardData;

import java.util.ArrayList;
import java.util.List;


public class GridViewFragment extends BaseFragment {

    private GridView gridView;
    private CardAdapter adapter;
    private List<CardData> cardList;

    @Override
    public int getPageTitle() {
        return R.string.page_title_gridView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gridview,container,false);
        gridView = (GridView) view.findViewById(R.id.gridView);

        populateList();
        adapter = new CardAdapter(getContext(), cardList);
        gridView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void populateList()
    {
        Context mContext = getContext();
        cardList = new ArrayList<>();
        cardList.add(new CardData(R.drawable.gridview_asset_1, "1. " + mContext.getString(R.string.gridView_title),mContext.getString(R.string.gridView_description)));
        cardList.add(new CardData(R.drawable.gridview_asset_2, "2. " + mContext.getString(R.string.gridView_title),mContext.getString(R.string.gridView_description)));
        cardList.add(new CardData(R.drawable.gridview_asset_3, "3. " + mContext.getString(R.string.gridView_title),mContext.getString(R.string.gridView_description)));
        cardList.add(new CardData(R.drawable.gridview_asset_4, "4. " + mContext.getString(R.string.gridView_title),mContext.getString(R.string.gridView_description)));
        cardList.add(new CardData(R.drawable.gridview_asset_5, "5. " + mContext.getString(R.string.gridView_title),mContext.getString(R.string.gridView_description)));
        cardList.add(new CardData(R.drawable.gridview_asset_6, "6. " + mContext.getString(R.string.gridView_title),mContext.getString(R.string.gridView_description)));
        cardList.add(new CardData(R.drawable.gridview_asset_7, "7. " + mContext.getString(R.string.gridView_title),mContext.getString(R.string.gridView_description)));
    }

}
