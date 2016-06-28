package com.philips.cdp.di.iapdemo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Apple on 27/06/16.
 */
public class FragmentLauncher extends Fragment implements View.OnClickListener {
    Button mShopNow;
    Button mPurchaseHistory;
    Button mProductDetail;
    Button mCart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.launch_as_fragment, container, false);

        mShopNow = (Button) v.findViewById(R.id.btn_shop_now);
        mPurchaseHistory = (Button) v.findViewById(R.id.btn_purchase_history);
        mProductDetail = (Button) v.findViewById(R.id.btn_launch_product_detail);
        mCart = (Button) v.findViewById(R.id.btn_cart);
        mShopNow.setOnClickListener(this);
        mProductDetail.setOnClickListener(this);
        mPurchaseHistory.setOnClickListener(this);
        mCart.setOnClickListener(this);
        return v;
    }
    Bundle bundle = new Bundle();
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cart:
                bundle.putString("FRAGMENT_ENTRY", "SHOPPING_CART");
                addFragment(new InAppPurchaseHostFragment(), bundle);
                break;
            case R.id.btn_shop_now:
                bundle.putString("FRAGMENT_ENTRY", "SHOP_NOW");
                addFragment(new InAppPurchaseHostFragment(),bundle);
                break;
            case R.id.btn_launch_product_detail:
                bundle.putString("FRAGMENT_ENTRY", "PRODUCT_DETAIL");
                addFragment(new InAppPurchaseHostFragment(), bundle);
                break;
            case R.id.btn_purchase_history:
                bundle.putString("FRAGMENT_ENTRY", "PURCHASE_HISTORY");
                addFragment(new InAppPurchaseHostFragment(),bundle);
                break;
        }
    }

    private void addFragment(Fragment pFragment, Bundle bundle) {
        pFragment.setArguments(bundle);
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(getId(), pFragment);
        transaction.addToBackStack(pFragment.getClass().getName());
        transaction.commitAllowingStateLoss();
    }
}
