/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogAdapter;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogData;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.session.IAPHandlerListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class ProductCatalogFragment extends BaseAnimationSupportFragment implements EventListener {

    private RecyclerView mRecyclerView;
    private ProductCatalogAdapter mAdapter;
    private IAPHandler mIapHandler;

    private IAPHandlerListener mProductCountListener = new IAPHandlerListener() {
        @Override
        public void onSuccess(final int count) {
            if (count > 0) {
                updateCount(count);
            } else {
                setCartIconVisibility(View.GONE);
            }
            if(Utility.isProgressDialogShowing()) {
                Utility.dismissProgressDialog();
            }
        }

        @Override
        public void onFailure(final int errorCode) {
            if(Utility.isProgressDialogShowing()) {
                Utility.dismissProgressDialog();
            }
            Toast.makeText(getContext(),"errorCode",Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT_CATALOG), this);
        View rootView = inflater.inflate(R.layout.iap_product_catalog_view, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.product_catalog_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new ProductCatalogAdapter(getContext(), new ArrayList<ProductCatalogData>(), getFragmentManager());
        mIapHandler = new IAPHandler();
        loadProducts();
        return rootView;
    }

    private void loadProducts() {
        ProductCatalogPresenter presenter = new ProductCatalogPresenter(getContext(), mAdapter, getFragmentManager());
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_get_product_catalog_details));
            updateProducts(presenter);
            mRecyclerView.setAdapter(mAdapter);
        }
    }


    private void updateProducts(ProductCatalogPresenter presenter) {
        presenter.getProductCatalog();
    }

    @Override
    public void onResume() {
        super.onResume();
        setCartIconVisibility(View.VISIBLE);
        setTitle(R.string.iap_product_catalog);
        mIapHandler.getProductCartCount(getContext(), mProductCountListener);

    }

    @Override
    public void raiseEvent(final String event) {

    }

    @Override
    public void onEventReceived(final String event) {
        if (event.equalsIgnoreCase(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED)) {
            addFragment(EmptyCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
        }
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT_CATALOG))) {
            startProductDetailFragment();
        }
    }

    private void startProductDetailFragment() {
        ProductCatalogData productCatalogData = mAdapter.getTheProductDataForDisplayingInProductDetailPage();
        Bundle bundle = new Bundle();
        if(productCatalogData!=null) {
            bundle.putString(IAPConstant.PRODUCT_TITLE, productCatalogData.getProductTitle());
            bundle.putString(IAPConstant.PRODUCT_CTN, productCatalogData.getCtnNumber());
            bundle.putString(IAPConstant.PRODUCT_PRICE, productCatalogData.getFormatedPrice());
            bundle.putString(IAPConstant.PRODUCT_OVERVIEW, productCatalogData.getMarketingTextHeader());
            bundle.putBoolean(IAPConstant.IS_PRODUCT_CATALOG, true);
            addFragment(ProductDetailFragment.createInstance(bundle, AnimationType.NONE), null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivity();
    }
}
