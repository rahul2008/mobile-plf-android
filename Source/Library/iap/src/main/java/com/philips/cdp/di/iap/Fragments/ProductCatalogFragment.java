/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.IAPCartListener;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogAdapter;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogData;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class ProductCatalogFragment extends BaseAnimationSupportFragment implements EventListener {

    public static final String TAG = ProductCatalogFragment.class.getName();

    private ProductCatalogAdapter mAdapter;
    private ShoppingCartPresenter mShoppingCartPresenter;

    private IAPCartListener mProductCountListener = new IAPCartListener() {
        @Override
        public void onSuccess(final int count) {
            updateCount(count);
        }

        @Override
        public void onFailure(final Message msg) {
            IAPLog.i(ProductCatalogFragment.class.getName(), "Get Count Failed ");
        }
    };

    public static ProductCatalogFragment createInstance(Bundle args, BaseAnimationSupportFragment.AnimationType animType) {
        ProductCatalogFragment fragment = new ProductCatalogFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ProductCatalogAdapter(getContext(), new ArrayList<ProductCatalogData>());
        loadProducts();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EventHelper.getInstance().registerEventNotification(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT_CATALOG), this);
        View rootView = inflater.inflate(R.layout.iap_product_catalog_view, container, false);
        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.product_catalog_recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mShoppingCartPresenter = new ShoppingCartPresenter(getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private void loadProducts() {
        ProductCatalogPresenter presenter = new ProductCatalogPresenter(getContext(), mAdapter, getFragmentManager());
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_get_product_catalog_details));
        }
        presenter.getProductCatalog();
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.PRODUCT_CATALOG_PAGE_NAME);
        setCartIconVisibility(View.VISIBLE);
        setTitle(R.string.iap_product_catalog);
        mShoppingCartPresenter.getProductCartCount(getContext(), mProductCountListener);
    }

    @Override
    public void onEventReceived(final String event) {
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT_CATALOG))) {
            startProductDetailFragment();
        }
    }

    private void startProductDetailFragment() {
        ProductCatalogData productCatalogData = mAdapter.getTheProductDataForDisplayingInProductDetailPage();
        Bundle bundle = new Bundle();
        if (productCatalogData != null) {
            bundle.putString(IAPConstant.PRODUCT_TITLE, productCatalogData.getProductTitle());
            bundle.putString(IAPConstant.PRODUCT_CTN, productCatalogData.getCtnNumber());
            bundle.putString(IAPConstant.PRODUCT_PRICE, productCatalogData.getFormatedPrice());
            bundle.putString(IAPConstant.PRODUCT_OVERVIEW, productCatalogData.getMarketingTextHeader());
            bundle.putBoolean(IAPConstant.IS_PRODUCT_CATALOG, true);
            bundle.putString(IAPConstant.IAP_PRODUCT_DISCOUNTED_PRICE, productCatalogData.getDiscountedPrice());
            addFragment(ProductDetailFragment.createInstance(bundle, AnimationType.NONE), null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT_CATALOG), this);
    }

    @Override
    public boolean onBackPressed() {
        finishActivity();
        return false;
    }
}
