package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;

public class ShoppingCartHomeFragment extends BaseNoAnimationFragment {
    private Context mContext;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_cart__home, container, false);
        mContext = getActivity().getApplicationContext();
        replaceShoppingCartFragment();
        return view;
    }

    @Override
    protected void updateTitle() {

    }

    public void replaceEmptyCartFragment() {
        EmptyCartFragment emptyCartFragment = new EmptyCartFragment();
        replaceFragment(emptyCartFragment);
    }

    public void replaceShippingAddressFragment() {
        ShippingAddressFragment shippingAddressFragment = new ShippingAddressFragment();
        replaceFragment(shippingAddressFragment);
    }

    public void replaceShoppingCartFragment() {
        ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
        replaceFragment(shoppingCartFragment);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        if (childFragmentManager == null)
            return;

        FragmentTransaction transaction = childFragmentManager.beginTransaction();
        transaction.replace(getFragmentContainerId(), fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    protected int getFragmentContainerId() {
        return R.id.fl_child_fragment_container;
    }
}
