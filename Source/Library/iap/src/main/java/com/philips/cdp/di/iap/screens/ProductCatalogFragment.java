/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.adapters.ProductCatalogAdapter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.cart.ShoppingCartAPI;
import com.philips.cdp.di.iap.cart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.ControllerFactory;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.products.ProductCatalogAPI;
import com.philips.cdp.di.iap.products.ProductCatalogData;
import com.philips.cdp.di.iap.products.ProductCatalogPresenter;
import com.philips.cdp.di.iap.response.products.PaginationEntity;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;
import com.philips.platform.uid.view.widget.SearchBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductCatalogFragment extends InAppBaseFragment
        implements EventListener, ProductCatalogPresenter.ProductCatalogListener, SearchBox.ExpandListener {

    public static final String TAG = ProductCatalogFragment.class.getName();
    private static final String SEARCH_TYPE = "search_selection";

    private Context mContext;

    private TextView mEmptyCatalogText;

    private ProductCatalogAdapter mAdapter;
    private ShoppingCartAPI mShoppingCartAPI;
    private RecyclerView mRecyclerView;
    private SearchBox mSearchBox;
    private ProductCatalogAPI mPresenter;
    private ArrayList<ProductCatalogData> mProductCatalog = new ArrayList<>();

    private final int PAGE_SIZE = 20;
    private int mTotalResults = 0;
    private int mCurrentPage = 0;
    private int mRemainingProducts = 0;
    private int mTotalPages = -1;
    private Bundle mBundle;
    private boolean mIsLoading = false;
    private boolean mIsProductsAvailable = true;


    private String query;
    private boolean isExpandableSearch;
    boolean searchBoxCollpased = true;
    private SharedPreferences sharedPreferences;
    private ImageView mClearIconView;
    private AppCompatAutoCompleteTextView mSearchTextView;

    public static ProductCatalogFragment createInstance(Bundle args, InAppBaseFragment.AnimationType animType) {
        ProductCatalogFragment fragment = new ProductCatalogFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = ControllerFactory.getInstance()
                .getProductCatalogPresenter(mContext, this);
        mAdapter = new ProductCatalogAdapter(mContext, mProductCatalog);

        mBundle = getArguments();
        String currentCountryCode = HybrisDelegate.getInstance(mContext).getStore().getCountry();
        String countrySelectedByVertical = Utility.getCountryFromPreferenceForKey
                (mContext, IAPConstant.IAP_COUNTRY_KEY);
        boolean isLocalData = ControllerFactory.getInstance().isPlanB();

        if (mBundle != null && mBundle.getStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS) != null) {
            return;
            //returning because in Hybris, If CTN not present in Hybris, we need to return a from onCreate to onCreateView to show UI
            // displayCategorisedProductList(mBundle.getStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS));
        } else {
            if (!isLocalData && currentCountryCode != null && currentCountryCode.equalsIgnoreCase(countrySelectedByVertical) &&
                    CartModelContainer.getInstance().getProductList() != null
                    && CartModelContainer.getInstance().getProductList().size() != 0) {
                onLoadFinished(getCachedProductList(), null);
            } else {
                fetchProductList();
            }
        }
    }

    private ArrayList<ProductCatalogData> getCachedProductList() {
        ArrayList<ProductCatalogData> mProductList = new ArrayList<>();
        HashMap<String, ProductCatalogData> productCatalogDataSaved =
                CartModelContainer.getInstance().getProductList();

        for (Map.Entry<String, ProductCatalogData> entry : productCatalogDataSaved.entrySet()) {
            if (entry != null) {
                mProductList.add(entry.getValue());
            }
        }
        return mProductList;
    }

    private void displayCategorisedProductList(ArrayList<String> categorisedProductList) {
        if (categorisedProductList.size() > 0)
            mPresenter.getCategorizedProductList(categorisedProductList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EventHelper.getInstance().registerEventNotification
                (String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_DETAIL), this);

        final View rootView = inflater.inflate(R.layout.iap_product_catalog_view, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.product_catalog_recycler_view);
        mSearchBox = (SearchBox) rootView.findViewById(R.id.iap_search_box);

        setUpSearch();

        if (savedInstanceState != null) {
            query = savedInstanceState.getString("query");
            searchBoxCollpased = savedInstanceState.getBoolean("collapsed");
        }


        mEmptyCatalogText = (TextView) rootView.findViewById(R.id.iap_productCatalog_emptyProductCatalogText_lebel);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(mRecyclerViewOnScrollListener);

        mShoppingCartAPI = new ShoppingCartPresenter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerViewSeparatorItemDecoration(getContext()));
        if (!mIsProductsAvailable) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyCatalogText.setVisibility(View.VISIBLE);
        }
        if (mBundle != null && mBundle.getStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS) != null) {
            displayCategorisedProductList(mBundle.getStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS));
        }
        return rootView;
    }

    private void setUpSearch() {
        mClearIconView = mSearchBox.getClearIconView();
        mSearchBox.setExpandListener(this);
        mSearchBox.setSearchIconified(isExpandableSearch);
        mSearchBox.setSearchCollapsed(searchBoxCollpased);
        mSearchBox.setQuery(query);
        mSearchBox.setSearchBoxHint(R.string.iap_search_box_hint);
        mSearchBox.setDecoySearchViewHint(R.string.iap_search_box_hint);
        mSearchTextView = mSearchBox.getSearchTextView();
        mSearchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    resetAdapter();
                    return;
                }
                String filterText = s.toString().toLowerCase(Locale.getDefault());
                mAdapter.filter(filterText);
            }
        });

        mClearIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchTextView.getText().clear();
                mSearchTextView.clearFocus();
                resetAdapter();
            }
        });
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("collapsed", mSearchBox.isSearchCollapsed());
        outState.putString("query", String.valueOf(mSearchBox.getQuery()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        IAPAnalytics.trackPage(IAPAnalyticsConstant.PRODUCT_CATALOG_PAGE_NAME);

        setTitleAndBackButtonVisibility(R.string.iap_product_catalog, false);
        if (!ControllerFactory.getInstance().isPlanB()) {
            setCartIconVisibility(true);
            mShoppingCartAPI.getProductCartCount(mContext, mProductCountListener);
        }

        mAdapter.tagProducts();
    }

    @Override
    public void onEventReceived(final String event) {
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_DETAIL))) {
            launchProductDetailFragment();
        }
    }

    private void launchProductDetailFragment() {
        ProductCatalogData productCatalogData = mAdapter.getTheProductDataForDisplayingInProductDetailPage();
        Bundle bundle = new Bundle();
        if (productCatalogData != null) {
            bundle.putString(IAPConstant.PRODUCT_TITLE, productCatalogData.getProductTitle());
            bundle.putString(IAPConstant.PRODUCT_CTN, productCatalogData.getCtnNumber());
            bundle.putString(IAPConstant.PRODUCT_PRICE, productCatalogData.getFormattedPrice());
            bundle.putString(IAPConstant.PRODUCT_VALUE_PRICE, productCatalogData.getPriceValue());
            bundle.putString(IAPConstant.PRODUCT_OVERVIEW, productCatalogData.getMarketingTextHeader());
            bundle.putString(IAPConstant.IAP_PRODUCT_DISCOUNTED_PRICE, productCatalogData.getDiscountedPrice());
            bundle.putBoolean(IAPConstant.IS_PRODUCT_CATALOG, true);
            addFragment(ProductDetailFragment.createInstance(bundle, AnimationType.NONE), ProductDetailFragment.TAG);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification
                (String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_DETAIL), this);
    }

    @Override
    public boolean handleBackEvent() {
        if (getActivity() != null && getActivity() instanceof IAPActivity) {
            int count = getFragmentManager().getBackStackEntryCount();
            for (int i = 0; i < count; i++) {
                getFragmentManager().popBackStack();
            }
            finishActivity();
        }
        return super.handleBackEvent();
    }

    private void fetchProductList() {
        if (!isProgressDialogShowing()) {
            showProgressDialog(mContext, getString(R.string.iap_please_wait));


            if (mPresenter == null)
                mPresenter = ControllerFactory.getInstance().
                        getProductCatalogPresenter(mContext, this);

            if (!mPresenter.getProductCatalog(mCurrentPage++, PAGE_SIZE, null)) {
                mIsProductsAvailable = false;
                dismissProgressDialog();
            }
        }
    }

    private void loadMoreItems() {
        if (mCurrentPage == mTotalPages) {
            dismissProgressDialog();
            return;
        } else
            fetchProductList();
    }

    @Override
    public void onLoadFinished(final ArrayList<ProductCatalogData> dataFetched,
                               PaginationEntity paginationEntity) {
        if (dataFetched.size() > 0) {
            updateProductCatalogList(dataFetched);
            mAdapter.notifyDataSetChanged();
            mAdapter.tagProducts();

            mIapListener.onSuccess();
            dismissProgressDialog();

            if (paginationEntity == null)
                return;

            if (mTotalResults == 0)
                mRemainingProducts = paginationEntity.getTotalResults();

            mTotalResults = paginationEntity.getTotalResults();
            mCurrentPage = paginationEntity.getCurrentPage();
            mTotalPages = paginationEntity.getTotalPages();
            mIsLoading = false;
        } else {
            onLoadError(NetworkUtility.getInstance().createIAPErrorMessage
                    ("", mContext.getString(R.string.iap_no_product_available)));
        }
    }

    @Override
    public void onLoadError(IAPNetworkError error) {
        if (error.getMessage() != null
                && error.getMessage().equalsIgnoreCase(mContext.getResources().getString(R.string.iap_no_product_available))) {
            if (mRecyclerView != null && mEmptyCatalogText != null) {
                mRecyclerView.setVisibility(View.GONE);
                mEmptyCatalogText.setVisibility(View.VISIBLE);
            }
        } else {
            if (mRecyclerView != null && mEmptyCatalogText != null) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyCatalogText.setVisibility(View.GONE);
            }
            NetworkUtility.getInstance().showErrorDialog(mContext, getFragmentManager(),
                    mContext.getString(R.string.iap_ok),
                    NetworkUtility.getInstance().getErrorTitleMessageFromErrorCode(mContext, error.getIAPErrorCode()),
                    NetworkUtility.getInstance().getErrorDescriptionMessageFromErrorCode(mContext, error));
        }

        mIapListener.onFailure(error.getIAPErrorCode());
        dismissProgressDialog();
    }

    private void updateProductCatalogList(final ArrayList<ProductCatalogData> dataFetched) {
        for (ProductCatalogData data : dataFetched) {
            if (!checkIfEntryExists(data))
                mProductCatalog.add(data);
        }
        if (!mProductCatalog.isEmpty()) {

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

    private RecyclerView.OnScrollListener
            mRecyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
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
                    IAPLog.d(TAG, "visibleItem " + visibleItemCount + ", " +
                            "firstvisibleItemPistion " + firstVisibleItemPosition +
                            "itemCount " + lay.getItemCount());
                    loadMoreItems();
                }
            }
        }
    };

    @Override
    public void onSearchExpanded() {
        ((IAPActivity) getActivity()).hideActionBar();
    }

    @Override
    public void onSearchCollapsed() {
        resetAdapter();
        showActionBar();
    }

    void resetAdapter() {
        mAdapter.setSearchFocused(false);
        mAdapter.setData(mProductCatalog);
        mAdapter.notifyDataSetChanged();
    }

    void showActionBar() {
        ((IAPActivity) getActivity()).showActionBar();
    }

    @Override
    public void onStop() {
        mSearchBox.setSearchCollapsed(true);
        super.onStop();
    }
}
