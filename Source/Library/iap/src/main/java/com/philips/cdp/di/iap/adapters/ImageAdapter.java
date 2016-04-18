package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.philips.cdp.di.iap.Fragments.ProductDetailImageNavigationFragment;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;

import java.util.ArrayList;

public class ImageAdapter extends FragmentStatePagerAdapter {
    protected static ArrayList<String> mAssetsFromPRX;

    public ImageAdapter(FragmentManager fm) {
        super(fm);
        mAssetsFromPRX = new ArrayList<>();
    }

    public void setAsset(ArrayList<String> assets){
        mAssetsFromPRX = assets;
    }

    @Override
    public Fragment getItem(int position) {
        ProductDetailImageNavigationFragment productDetailImageNavigationFragment = ProductDetailImageNavigationFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(NetworkConstants.IAP_ASSET_URL,mAssetsFromPRX.get(position % mAssetsFromPRX.size()));
        bundle.putBoolean(IAPConstant.IS_PRODUCT_CATALOG,true);
        productDetailImageNavigationFragment.setArguments(bundle);
        return productDetailImageNavigationFragment;
    }

    @Override
    public int getCount() {
        return mAssetsFromPRX.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ImageAdapter.mAssetsFromPRX.get(position % mAssetsFromPRX.size());
    }
}