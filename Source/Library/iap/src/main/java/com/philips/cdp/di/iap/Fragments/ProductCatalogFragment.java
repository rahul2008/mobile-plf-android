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
import com.philips.cdp.di.iap.adapters.ProductCatalogAdapter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.core.ProductCatalogAPI;
import com.philips.cdp.di.iap.core.ShoppingCartAPI;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogData;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.response.products.PaginationEntity;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.localematch.PILLocaleManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductCatalogFragment extends BaseAnimationSupportFragment implements EventListener, ProductCatalogPresenter.LoadListener {

    public static final String TAG = ProductCatalogFragment.class.getName();

    private ProductCatalogAdapter mAdapter;
    private ShoppingCartAPI mShoppingCartAPI;
    private RecyclerView mRecyclerView;
    private TextView mEmptyCatalogText;
    private int mTotalResults = 0;
    private final int PAGE_SIZE = 20;
    private int mCurrentPage = 0;
    private int mRemainingProducts = 0;
    private boolean mIsLoading = false;
    private int mTotalPages = -1;
    ProductCatalogAPI mPresenter;
    ArrayList<ProductCatalogData> mProductCatalog = new ArrayList<>();
    Bundle mBundle;

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
        mPresenter = ControllerFactory.getInstance()
                .getProductCatalogPresenter(getContext(), this, getFragmentManager());
        mAdapter = new ProductCatalogAdapter(getContext(), mProductCatalog);
        mBundle = getArguments();

        PILLocaleManager localeManager = new PILLocaleManager(getContext());
        String currentCountryCode = localeManager.getCountryCode();
        String savedCountry = Utility.getCountryFromPreferenceForKey(getContext(), IAPConstant.IAP_COUNTRY_KEY);

        if (mBundle != null) {
            if (mBundle.containsKey(IAPConstant.CAEGORIZED_PRODUCT_CTNS) && mBundle.getStringArrayList(IAPConstant.CAEGORIZED_PRODUCT_CTNS) != null) {
                onLoadFinished(getProductCatalog(), null);
            } else if (currentCountryCode.equals(savedCountry)) {
                if (CartModelContainer.getInstance().getProductCatalogData() != null && CartModelContainer.getInstance().getProductCatalogData().size() != 0) {
                    onLoadFinished(getProductCatalogDataFromStoredData(), null);
                } else {
                    loadProductCatalog();
                }
            } else {
                loadProductCatalog();
            }
        }
    }

    ArrayList<ProductCatalogData> getProductCatalogDataFromStoredData() {
        ArrayList<ProductCatalogData> catalogDatas = new ArrayList<>();
        HashMap<String, ProductCatalogData> productCatalogDataSaved = CartModelContainer.getInstance().getProductCatalogData();
        for (Map.Entry<String, ProductCatalogData> entry : productCatalogDataSaved.entrySet()) {
            if (entry != null) {
                catalogDatas.add(entry.getValue());
            }
        }
        return catalogDatas;
    }

    private ArrayList<ProductCatalogData> getProductCatalog() {
        ArrayList<ProductCatalogData> catalogDatas = new ArrayList<>();
        ArrayList<String> ctns = mBundle.getStringArrayList(IAPConstant.CAEGORIZED_PRODUCT_CTNS);

        if (ctns != null) {
            for (String ctn : ctns) {
                if (CartModelContainer.getInstance().isProductCatalogDataPresent(ctn)) {
                    catalogDatas.add(CartModelContainer.getInstance().getProductCatalogData(ctn));
                } else {
                    mPresenter.getProductCategorizedProduct(ctns);
                }
            }
        }

        return catalogDatas;
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
        mRecyclerView.addOnScrollListener(mRecyclerViewOnScrollListener);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.PRODUCT_CATALOG_PAGE_NAME);
        setCartIconVisibility(true);
        setTitleAndBackButtonVisibility(R.string.iap_product_catalog, false);
        if (!ControllerFactory.getInstance().loadLocalData()) {
            mShoppingCartAPI.getProductCartCount(getContext(), mProductCountListener);
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
            addFragment(ProductDetailFragment.createInstance(bundle, AnimationType.NONE), ProductDetailFragment.TAG);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT_CATALOG), this);
    }

    @Override
    public boolean handleBackEvent() {
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

    private RecyclerView.OnScrollListener
            mRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView,
                                         int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager lay = (LinearLayoutManager) mRecyclerView
                    .getLayoutManager();

            int visibleItemCount = lay.getChildCount();
            int firstVisibleItemPosition = lay.findFirstVisibleItemPosition();

            if (!mIsLoading) {
                //if scrolled beyond page size and we have more items to load
                if ((visibleItemCount + firstVisibleItemPosition) >= lay.getItemCount()
                        && firstVisibleItemPosition >= 0
                        && mRemainingProducts > PAGE_SIZE) {
                    mIsLoading = true;
                    IAPLog.d(TAG, "visibleItem " + visibleItemCount + ", firstvisibleItemPistion " + firstVisibleItemPosition + "itemCount " + lay.getItemCount());
                    loadMoreItems();
                }
            }
        }
    };

    private void loadProductCatalog() {
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
        }
        mPresenter.getProductCatalog(mCurrentPage++, PAGE_SIZE, null);
    }

    private void loadMoreItems() {
        if (mCurrentPage == mTotalPages) {
            if (Utility.isProgressDialogShowing())
                Utility.dismissProgressDialog();
            return;
        }

        if (!Utility.isProgressDialogShowing()) {
            IAPLog.i(TAG, "Loading Page = " + mCurrentPage + "Total Results = " + mTotalResults + "Size of Array = " + mProductCatalog.size());
            Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
        }

        if (mPresenter == null)
            mPresenter = ControllerFactory.getInstance().getProductCatalogPresenter(getContext(), this, getFragmentManager());
        mPresenter.getProductCatalog(++mCurrentPage, PAGE_SIZE, null);
    }

    @Override
    public void onLoadFinished(final ArrayList<ProductCatalogData> dataFetched, PaginationEntity paginationEntity) {
        updateProductCatalogList(dataFetched);
        mAdapter.notifyDataSetChanged();
        mAdapter.tagProducts();

        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();

        if (paginationEntity == null)
            return;

        if (mTotalResults == 0)
            mRemainingProducts = paginationEntity.getTotalResults();

        mTotalResults = paginationEntity.getTotalResults();
        mCurrentPage = paginationEntity.getCurrentPage();
        mTotalPages = paginationEntity.getTotalPages();
        mIsLoading = false;
    }

    private void updateProductCatalogList(final ArrayList<ProductCatalogData> dataFetched) {
        for (ProductCatalogData data : dataFetched) {
            if (!checkIfEntryExists(data))
                mProductCatalog.add(data);
        }
    }

    private boolean checkIfEntryExists(final ProductCatalogData data) {
        for (ProductCatalogData entry : mProductCatalog) {
            if (entry.getCtnNumber().equalsIgnoreCase(data.getCtnNumber())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLoadError(IAPNetworkError error) {
        if (error.getMessage() != null && error.getMessage().equalsIgnoreCase(getResources().getString(R.string.iap_no_product_available))) {
            if (mRecyclerView != null && mEmptyCatalogText != null) {
                mRecyclerView.setVisibility(View.GONE);
                mEmptyCatalogText.setVisibility(View.VISIBLE);
            }
        } else {
            if (mRecyclerView != null && mEmptyCatalogText != null) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyCatalogText.setVisibility(View.GONE);
            }
        }
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }
}
