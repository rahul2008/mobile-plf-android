/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.IAPUtility;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;
import com.philips.platform.uid.view.widget.SearchBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class ProductCatalogFragment extends InAppBaseFragment
        implements EventListener, ProductCatalogPresenter.ProductCatalogListener, SearchBox.ExpandListener, SearchBox.QuerySubmitListener {

    public static final String TAG = ProductCatalogFragment.class.getName();

    private Context mContext;

    private TextView mEmptyCatalogText;

    private ProductCatalogAdapter mAdapter;
    private ShoppingCartAPI mShoppingCartAPI;
    private RecyclerView mRecyclerView;
    private SearchBox mSearchBox;
    private ProductCatalogAPI mPresenter;
    private List<ProductCatalogData> mProductCatalog = new CopyOnWriteArrayList<>();

    private final int page_size = 20;
    private int mTotalResults = 0;
    private int mCurrentPage = 0;
    private int mRemainingProducts = 0;
    private int mTotalPages = -1;
    private Bundle mBundle;
    private boolean mIsLoading = false;
    private boolean mIsProductsAvailable = true;
    private RelativeLayout mParentLayout;
    private AppCompatAutoCompleteTextView mSearchTextView;
    private LinearLayout mBannerLayout;

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
        if (categorisedProductList.size() > 0) {
            createCustomProgressBar(mParentLayout, BIG);
            mPresenter.getCategorizedProductList(categorisedProductList);
        }
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
        mRecyclerView = rootView.findViewById(R.id.product_catalog_recycler_view);
        mSearchBox = rootView.findViewById(R.id.iap_search_box);
        mBannerLayout = rootView.findViewById(R.id.ll_banner_place_holder);

        if(IAPUtility.getInstance().getBannerView()!=null){
            if(IAPUtility.getInstance().getBannerView().getParent()!=null){
                ((ViewGroup)IAPUtility.getInstance().getBannerView().getParent()).removeAllViews();
            }
            mBannerLayout.addView(IAPUtility.getInstance().getBannerView());
            mBannerLayout.setVisibility(View.VISIBLE);
        }
        mParentLayout = rootView.findViewById(R.id.parent_layout);
        setUpSearch();

        mEmptyCatalogText = rootView.findViewById(R.id.iap_productCatalog_emptyProductCatalogText_lebel);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(mRecyclerViewOnScrollListener);

        mShoppingCartAPI = new ShoppingCartPresenter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecyclerViewSeparatorItemDecoration(getContext()));
        if (!mIsProductsAvailable) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyCatalogText.setVisibility(View.VISIBLE);
            mSearchBox.setVisibility(View.GONE);
        }
        if (mBundle != null && mBundle.getStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS) != null) {
            displayCategorisedProductList(mBundle.getStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS));
        }
        return rootView;
    }

    private void setUpSearch() {
        ImageView mClearIconView = mSearchBox.getClearIconView();
        mSearchBox.setExpandListener(this);
        mSearchBox.setSearchBoxHint(R.string.iap_search_box_hint);
        mSearchBox.setDecoySearchViewHint(R.string.iap_search_box_hint);
        mSearchBox.setQuerySubmitListener(this);
        mSearchTextView = mSearchBox.getSearchTextView();
        mSearchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Do nothing
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
                resetAdapter();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        mBundle = getArguments();

        boolean isLocalData = ControllerFactory.getInstance().isPlanB();

        if (mBundle != null && mBundle.getStringArrayList(IAPConstant.CATEGORISED_PRODUCT_CTNS) != null) {
            return;
        } else {
            if (!isLocalData &&
                    CartModelContainer.getInstance().getProductList() != null
                    && CartModelContainer.getInstance().getProductList().size() != 0) {
                onLoadFinished(getCachedProductList(), null);
            } else {
                fetchProductList();
            }
        }

        IAPAnalytics.trackPage(IAPAnalyticsConstant.PRODUCT_CATALOG_PAGE_NAME);

        setTitleAndBackButtonVisibility(R.string.iap_product_catalog, true);
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
            bundle.putString(IAPConstant.STOCK_LEVEL_STATUS, productCatalogData.getStockLevelStatus());
            bundle.putInt(IAPConstant.STOCK_LEVEL, productCatalogData.getStockLevel());
            bundle.putBoolean(IAPConstant.IS_PRODUCT_CATALOG, true);
            if (getArguments().getStringArrayList(IAPConstant.IAP_IGNORE_RETAILER_LIST) != null) {
                final ArrayList<String> list = getArguments().getStringArrayList(IAPConstant.IAP_IGNORE_RETAILER_LIST);
                bundle.putStringArrayList(IAPConstant.IAP_IGNORE_RETAILER_LIST, list);
            }
            addFragment(ProductDetailFragment.createInstance(bundle, AnimationType.NONE), ProductDetailFragment.TAG, true);
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
        createCustomProgressBar(mParentLayout, BIG);

        if (mPresenter == null)
            mPresenter = ControllerFactory.getInstance().
                    getProductCatalogPresenter(mContext, this);

        if (!mPresenter.getProductCatalog(mCurrentPage++, page_size, null)) {
            mIsProductsAvailable = false;
        }

    }

    private void loadMoreItems() {
        if (mCurrentPage == mTotalPages) {
            hideProgressBar();
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


            if (paginationEntity == null)
                return;

            if (mTotalResults == 0)
                mRemainingProducts = paginationEntity.getTotalResults();

            mTotalResults = paginationEntity.getTotalResults();
            mCurrentPage = paginationEntity.getCurrentPage();
            mTotalPages = paginationEntity.getTotalPages();
            mIsLoading = false;

            hideProgressBar();
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
                mSearchBox.setVisibility(View.GONE);
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
        hideProgressBar();
    }

    private void updateProductCatalogList(final ArrayList<ProductCatalogData> dataFetched) {
        for (ProductCatalogData data : dataFetched) {
            if (!checkIfEntryExists(data))
                mProductCatalog.add(data);
        }
    }

    private boolean checkIfEntryExists(final ProductCatalogData data) {
        for (ProductCatalogData entry : mProductCatalog) {
            final String ctnNumber = entry.getCtnNumber();

            if (ctnNumber != null && ctnNumber.equalsIgnoreCase(data.getCtnNumber())) {
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


            if (!mIsLoading && (visibleItemCount + firstVisibleItemPosition) >= lay.getItemCount()
                    && firstVisibleItemPosition >= 0
                    && mRemainingProducts > page_size) {
                mIsLoading = true;
                IAPLog.d(TAG, "visibleItem " + visibleItemCount + ", " +
                        "firstvisibleItemPistion " + firstVisibleItemPosition +
                        "itemCount " + lay.getItemCount());
                loadMoreItems();
            }

        }
    };

    @Override
    public void onSearchExpanded() {
        //Do nothing
    }

    @Override
    public void onSearchCollapsed() {
        resetAdapter();
    }

    void resetAdapter() {
        mAdapter.setSearchFocused(false);
        mAdapter.setData(mProductCatalog);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        mSearchBox.setSearchCollapsed(true);
        super.onStop();
    }

    @Override
    public void onQuerySubmit(CharSequence query) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mSearchBox.getWindowToken(), 0);
    }
}
