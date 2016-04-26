package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.tagging.Tagging;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class EmptyCartFragment extends BaseAnimationSupportFragment implements View.OnClickListener, EventListener {

    private Button mContinueShopping;

    public static EmptyCartFragment createInstance(Bundle args, BaseAnimationSupportFragment.AnimationType animType) {
        EmptyCartFragment fragment = new EmptyCartFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_empty_shopping_cart, container, false);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG_FROM_EMPTY_CART), this);
        mContinueShopping = (Button) rootView.findViewById(R.id.btn_continue_shopping);
        mContinueShopping.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG_FROM_EMPTY_CART), this);
    }

    @Override
    public void onClick(final View v) {
        if (v == mContinueShopping) {
            //Track continue shopping action
            Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.SPECIAL_EVENTS,
                    IAPAnalyticsConstant.CONTINUE_SHOPPING_SELECTED);

            EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG_FROM_EMPTY_CART);
        }
    }

    @Override
    public boolean onBackPressed() {
        finishActivity();
        return true;
    }

    @Override
    public void raiseEvent(final String event) {

    }

    @Override
    public void onEventReceived(final String event) {
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(ProductCatalogFragment.TAG);
        if (fragment != null) {
            if (event.equalsIgnoreCase(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG_FROM_EMPTY_CART))) {
                replaceFragment(getActivity().getSupportFragmentManager().findFragmentByTag(ProductCatalogFragment.TAG), ProductCatalogFragment.TAG);
            }
        } else {
            addProductCatalog();
        }
    }

    private void addProductCatalog() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_mainFragmentContainer, new ProductCatalogFragment(), ProductCatalogFragment.TAG);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
    }
}
