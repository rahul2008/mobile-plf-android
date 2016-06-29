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
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.ShoppingCart.IAPCartListener;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.core.ProductCatalogAPI;
import com.philips.cdp.di.iap.core.ShoppingCartAPI;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogAdapter;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogData;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;

public class ProductCatalogFragment extends BaseAnimationSupportFragment implements EventListener, ShoppingCartPresenter.ShoppingCartLauncher, ProductCatalogPresenter.LoadListener {

    public static final String TAG = ProductCatalogFragment.class.getName();

    private ProductCatalogAdapter mAdapter;
    private ShoppingCartAPI mShoppingCartAPI;
    private RecyclerView mRecyclerView;
    private TextView mEmptyCatalogText;

    private IAPCartListener mProductCountListener = new IAPCartListener() {
        @Override
        public void onSuccess(final int count) {
            updateCount(count);
        }

        @Override
        public void onFailure(final Message msg) {
            IAPLog.i(ProductCatalogFragment.class.getName(), "Get Count Failed ");
            Utility.dismissProgressDialog();
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
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.product_catalog_recycler_view);
        mEmptyCatalogText = (TextView) rootView.findViewById(R.id.empty_product_catalog_txt);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mShoppingCartAPI = new ShoppingCartPresenter(getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    private void loadProducts() {
        ProductCatalogAPI presenter = ControllerFactory.getInstance()
                .getProductCatalogPresenter(getContext(), this, getFragmentManager());
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
        }
        presenter.getProductCatalog();
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.PRODUCT_CATALOG_PAGE_NAME);
        setCartIconVisibility(View.VISIBLE);
        setTitle(R.string.iap_product_catalog);

        if (!ControllerFactory.getInstance().loadLocalData()) {
            mShoppingCartAPI.getProductCartCount(getContext(), mProductCountListener, this);
        }
        mAdapter.tagProducts();
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
            bundle.putString(IAPConstant.PRODUCT_VALUE_PRICE, productCatalogData.getPriceValue());
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
        if (getActivity() != null && getActivity() instanceof IAPActivity) {
            int count = getFragmentManager().getBackStackEntryCount();
            IAPLog.d(IAPLog.LOG, "Count in Backstack =" + count);
            for (int i = 0; i < count; i++) {
                getFragmentManager().popBackStack();
            }
            finishActivity();
        }
        return false;
    }

    @Override
    public void onLoadFinished(final ArrayList<ProductCatalogData> data) {
        mAdapter = new ProductCatalogAdapter(getContext(), data);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.tagProducts();
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    @Override
    public void onLoadError(IAPNetworkError error) {
        if (error.getMessage().equalsIgnoreCase(getResources().getString(R.string.iap_no_product_available))) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyCatalogText.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyCatalogText.setVisibility(View.GONE);
        }
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }
}
