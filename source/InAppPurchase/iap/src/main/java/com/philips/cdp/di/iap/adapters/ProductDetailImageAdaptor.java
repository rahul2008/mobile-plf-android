package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philips.cdp.di.iap.Fragments.ProductDetailImageNavigationFragment;
import com.philips.cdp.prxclient.prxdatamodels.assets.Asset;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailImageAdaptor extends FragmentPagerAdapter {
    protected static ArrayList<String> mAssetsFromPRX;
    private Context mContext;

    public ProductDetailImageAdaptor(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        mAssetsFromPRX = new ArrayList<>();
    }

    public void setAsset(ArrayList<String> assets){
        mAssetsFromPRX = assets;
    }

    @Override
    public Fragment getItem(int position) {
        return ProductDetailImageNavigationFragment.newInstance(mAssetsFromPRX.get(position % mAssetsFromPRX.size()), mContext);
    }

    @Override
    public int getCount() {
        return mAssetsFromPRX.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ProductDetailImageAdaptor.mAssetsFromPRX.get(position % mAssetsFromPRX.size());
    }
}