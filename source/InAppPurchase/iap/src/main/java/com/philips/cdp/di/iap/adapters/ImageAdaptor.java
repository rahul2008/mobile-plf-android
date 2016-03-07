package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.philips.cdp.di.iap.Fragments.ProductDetailImageNavigationFragment;

import java.util.ArrayList;

public class ImageAdaptor extends FragmentPagerAdapter {
    protected static ArrayList<String> mAssetsFromPRX;
    private Context mContext;

    public ImageAdaptor(FragmentManager fm, Context context) {
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
        return ImageAdaptor.mAssetsFromPRX.get(position % mAssetsFromPRX.size());
    }
}