package com.philips.cdp.di.iapdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.Fragments.IAPFragmentActionLayout;
import com.philips.cdp.di.iap.Fragments.ProductCatalogFragment;
import com.philips.cdp.di.iap.core.ControllerFactory;

/**
 * Created by 310164421 on 6/8/2016.
 */
public class IAPFragment extends Fragment {
    private IAPFragmentActionLayout mFragmentActionLayout;

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (mFragmentActionLayout == null) {
            mFragmentActionLayout = new IAPFragmentActionLayout(context, getActivity().getSupportFragmentManager());
        }
        mFragmentActionLayout.getCustomView(context);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addProductCatalog();
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        setBackButtonVisibility(View.VISIBLE);
        setCartIconVisibility(View.GONE);
    }

    void addProductCatalog() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.vertical_Container, new ProductCatalogFragment(), ProductCatalogFragment.class.getName());
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();

    }

    protected void setTitle(int resourceId) {
        mFragmentActionLayout.setHeaderTitle(resourceId);
    }

    protected void setTitle(String title) {
        mFragmentActionLayout.setHeaderTitle(title);
    }

    protected void setBackButtonVisibility(final int isVisible) {
        mFragmentActionLayout.setBackButtonVisibility(isVisible);
    }

    public void updateCount(final int count) {
        mFragmentActionLayout.updateCount(count);
    }

    public void setCartIconVisibility(final int visibility) {
        if (!ControllerFactory.getInstance().shouldDisplayCartIcon()) {
            mFragmentActionLayout.setCartIconVisibility(View.GONE);
        } else {
            mFragmentActionLayout.setCartIconVisibility(visibility);
        }
    }
}
