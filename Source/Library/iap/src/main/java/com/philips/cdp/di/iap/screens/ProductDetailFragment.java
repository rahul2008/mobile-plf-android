/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.adapters.ImageAdapter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.cart.IAPCartListener;
import com.philips.cdp.di.iap.cart.ShoppingCartAPI;
import com.philips.cdp.di.iap.cart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.ControllerFactory;
import com.philips.cdp.di.iap.controller.ProductDetailController;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.prx.PRXAssetExecutor;
import com.philips.cdp.di.iap.prx.PRXSummaryExecutor;
import com.philips.cdp.di.iap.response.products.ProductDetailEntity;
import com.philips.cdp.di.iap.response.retailers.StoreEntity;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.uikit.customviews.CircleIndicator;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDetailFragment extends InAppBaseFragment implements
        PRXAssetExecutor.AssetListener, View.OnClickListener, EventListener,
        AbstractModel.DataLoadListener, ErrorDialogFragment.ErrorDialogListener,
        ProductDetailController.ProductSearchListener, ShoppingCartPresenter.ShoppingCartListener<StoreEntity> {


    public static final String TAG = ProductDetailFragment.class.getName();

    private Context mContext;
    private Bundle mBundle;

    private SummaryModel mProductSummary;
    private ShoppingCartAPI mShoppingCartAPI;
    private ProductDetailEntity mProductDetail;
    private ImageAdapter mImageAdapter;
    private ViewPager mViewPager;

    private TextView mProductDiscountedPrice;
    private TextView mProductDescription;
    private TextView mCTN;
    private TextView mPrice;
    private TextView mProductOverview;
    private Button mAddToCart;
    private Button mBuyFromRetailers;
    private ScrollView mDetailLayout;

    private ArrayList<String> mAsset;
    private boolean mLaunchedFromProductCatalog = false;
    private String mCTNValue;
    private String mProductTitle;
    private ErrorDialogFragment mErrorDialogFragment;

    private IAPCartListener mBuyProductListener = new IAPCartListener() {
        @Override
        public void onSuccess(final int count) {
            tagItemAddedToCart();
            if (mIapListener != null) {
                mIapListener.onUpdateCartCount();
            }
        }

        @Override
        public void onFailure(final Message msg) {
            if (Utility.isProgressDialogShowing())
                Utility.dismissProgressDialog();

            IAPNetworkError iapNetworkError = (IAPNetworkError) msg.obj;
            if (null != iapNetworkError.getServerError()) {
                if (iapNetworkError.getIAPErrorCode() == IAPConstant.IAP_ERROR_INSUFFICIENT_STOCK_ERROR) {
                    NetworkUtility.getInstance().showErrorDialog(mContext, getFragmentManager(),
                            mContext.getString(R.string.iap_ok),
                            mContext.getString(R.string.iap_out_of_stock), iapNetworkError.getMessage());
                }
            } else {
                NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
            }
        }
    };

    public static ProductDetailFragment createInstance(Bundle args, AnimationType animType) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mShoppingCartAPI = ControllerFactory.
                getInstance().getShoppingCartPresenter(mContext, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EventHelper.getInstance().registerEventNotification(IAPConstant.IAP_LAUNCH_SHOPPING_CART, this);

        View rootView = inflater.inflate(R.layout.iap_product_details_screen, container, false);
        mDetailLayout = (ScrollView) rootView.findViewById(R.id.scrollView);
        mProductDescription = (TextView) rootView.findViewById(R.id.product_description);
        mCTN = (TextView) rootView.findViewById(R.id.ctn);
        mPrice = (TextView) rootView.findViewById(R.id.individual_price);
        mProductOverview = (TextView) rootView.findViewById(R.id.product_overview);
        mAddToCart = (Button) rootView.findViewById(R.id.add_to_cart);
        mBuyFromRetailers = (Button) rootView.findViewById(R.id.buy_from_retailor);
        mProductDiscountedPrice = (TextView) rootView.findViewById(R.id.tv_discounted_price);
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);

        CircleIndicator indicator = (CircleIndicator) rootView.findViewById(R.id.indicator);
        mImageAdapter = new ImageAdapter(mContext, getFragmentManager(),
                mLaunchedFromProductCatalog, new ArrayList<String>());
        mViewPager.setAdapter(mImageAdapter);
        indicator.setViewPager(mViewPager);

        mBundle = getArguments();
        if (mBundle != null) {
            if (mBundle.containsKey(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL)) {
                mCTNValue = mBundle.getString(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL);
                if (isNetworkConnected()) {
                    if (!ControllerFactory.getInstance().isPlanB()) {
                        ProductDetailController controller = new ProductDetailController(mContext, this);
                        if (!Utility.isProgressDialogShowing()) {
                            Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
                        }
                        controller.getProductDetail(mCTNValue);
                    } else {
                        fetchProductDetailFromPrx();
                    }
                }
            } else {
                mCTNValue = mBundle.getString(IAPConstant.PRODUCT_CTN);
                mLaunchedFromProductCatalog = mBundle.getBoolean(IAPConstant.IS_PRODUCT_CATALOG, false);
                mProductTitle = mBundle.getString(IAPConstant.PRODUCT_TITLE);
                populateData();
            }
        }
        return rootView;
    }

    private void fetchProductDetailFromPrx() {
        makeSummaryRequest();
    }

    private void tagProduct() {
        String productPrice = "";
        HashMap<String, String> contextData = new HashMap<>();
        StringBuilder product = new StringBuilder();
        if (mBundle.getString(IAPConstant.PRODUCT_VALUE_PRICE) != null)
            productPrice = mBundle.getString(IAPConstant.PRODUCT_VALUE_PRICE);
        product = product.append("Tuscany_Campaign").append(";")
                .append(mProductTitle).append(";").append(";")
                .append(productPrice);
        contextData.put(IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.PROD_VIEW);
        contextData.put(IAPAnalyticsConstant.PRODUCTS, product.toString());
        IAPAnalytics.trackMultipleActions(IAPAnalyticsConstant.SEND_DATA, contextData);
    }

    private void makeAssetRequest() {
        if (!CartModelContainer.getInstance().isPRXAssetPresent(mCTNValue)) {
            if (!Utility.isProgressDialogShowing()) {
                Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
            }
            PRXAssetExecutor builder = new PRXAssetExecutor(mContext, mCTNValue, this);
            builder.build();
        } else {
            final HashMap<String, ArrayList<String>> prxAssetObjects =
                    CartModelContainer.getInstance().getPRXAssetList();
            for (Map.Entry<String, ArrayList<String>> entry : prxAssetObjects.entrySet()) {
                if (entry != null && entry.getKey().equalsIgnoreCase(mCTNValue)) {
                    mAsset = entry.getValue();
                    break;
                }
            }
            mImageAdapter = new ImageAdapter(mContext, getFragmentManager(),
                    mLaunchedFromProductCatalog, mAsset);
            mViewPager.setAdapter(mImageAdapter);
            mImageAdapter.notifyDataSetChanged();
            if (Utility.isProgressDialogShowing())
                Utility.dismissProgressDialog();
        }
    }

    private void makeSummaryRequest() {
        ArrayList<String> ctnList = new ArrayList<>();
        ctnList.add(mCTNValue);
        if (!CartModelContainer.getInstance().isPRXSummaryPresent(mCTNValue)) {
            if (!Utility.isProgressDialogShowing()) {
                if (mContext == null) return;
                Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
            }
            PRXSummaryExecutor builder = new PRXSummaryExecutor(mContext, ctnList, this);
            builder.preparePRXDataRequest();
        } else {
            final HashMap<String, SummaryModel> prxAssetObjects =
                    CartModelContainer.getInstance().getPRXSummaryList();
            for (Map.Entry<String, SummaryModel> entry : prxAssetObjects.entrySet()) {
                if (entry != null && entry.getKey().equalsIgnoreCase(mCTNValue)) {
                    mProductSummary = entry.getValue();
                    populateData();
                    break;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mBundle != null) {
            if (!mBundle.containsKey(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL)) {
                tagProduct();
                if (mBundle != null && mLaunchedFromProductCatalog) {
                    IAPAnalytics.trackPage(IAPAnalyticsConstant.PRODUCT_DETAIL_PAGE_NAME);
                    handleViews();
                    mProductDiscountedPrice.setVisibility(View.VISIBLE);
                } else {
                    IAPAnalytics.trackPage(IAPAnalyticsConstant.SHOPPING_CART_ITEM_DETAIL_PAGE_NAME);
                    setCartIconVisibility(false);
                    setTitleAndBackButtonVisibility(R.string.iap_shopping_cart_item, true);
                }
            } else {
                handleViews();
            }
        }
        makeAssetRequest();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.IAP_LAUNCH_SHOPPING_CART, this);
    }

    private void handleViews() {
        setTitleAndBackButtonVisibility(mProductTitle, true);
        if (ControllerFactory.getInstance().isPlanB()) {
            mBuyFromRetailers.setText(R.string.iap_buy_now);
            mAddToCart.setVisibility(View.GONE);
            setCartIconVisibility(false);
        } else {
            mBuyFromRetailers.setText(R.string.iap_buy_from_retailers);
            mAddToCart.setVisibility(View.VISIBLE);
            mAddToCart.setOnClickListener(this);
            Drawable shoppingCartIcon = VectorDrawable.create(mContext, R.drawable.iap_shopping_cart);
            mAddToCart.setCompoundDrawablesWithIntrinsicBounds(shoppingCartIcon, null, null, null);

            setCartIconVisibility(true);
            mShoppingCartAPI.getProductCartCount(mContext, mProductCountListener);
        }
        mBuyFromRetailers.setOnClickListener(this);
        mBuyFromRetailers.setVisibility(View.VISIBLE);
    }

    private void getRetailersInformation() {
        ShoppingCartAPI presenter = ControllerFactory.
                getInstance().getShoppingCartPresenter(mContext, this);

        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
            presenter.getRetailersInformation(mCTNValue);
        }
    }


    private void buyFromRetailers(ArrayList<StoreEntity> storeEntities) {
        if (!isNetworkConnected()) return;

        Bundle bundle = new Bundle();
        bundle.putSerializable(IAPConstant.IAP_RETAILER_INFO, storeEntities);
        addFragment(BuyFromRetailersFragment.createInstance(bundle, AnimationType.NONE),
                BuyFromRetailersFragment.TAG);
    }


    @Override
    public void onFetchAssetSuccess(final Message msg) {
        if (mContext == null) return;
        IAPLog.d(IAPConstant.PRODUCT_DETAIL_FRAGMENT, "Success");
        mAsset = (ArrayList<String>) msg.obj;
        CartModelContainer.getInstance().addProductAsset(mCTNValue, mAsset);
        mImageAdapter = new ImageAdapter(mContext, getFragmentManager(), mLaunchedFromProductCatalog, mAsset);
        mViewPager.setAdapter(mImageAdapter);
        mImageAdapter.notifyDataSetChanged();
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    @Override
    public void onFetchAssetFailure(final Message msg) {
        IAPLog.d(IAPConstant.PRODUCT_DETAIL_FRAGMENT, "Failure");
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
        if (!isNetworkConnected()) return;
        NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);

    }

    void buyProduct(final String ctnNumber) {
        Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
        mShoppingCartAPI.buyProduct(mContext, ctnNumber, mBuyProductListener);
    }

    private void tagItemAddedToCart() {
        HashMap<String, String> contextData = new HashMap<>();
        contextData.put(IAPAnalyticsConstant.ORIGINAL_PRICE, mPrice.getText().toString());
        if (mProductDiscountedPrice.getVisibility() == View.VISIBLE)
            contextData.put(IAPAnalyticsConstant.DISCOUNTED_PRICE, mProductDiscountedPrice.getText().toString());
        contextData.put(IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.ADD_TO_CART);
        IAPAnalytics.trackMultipleActions(IAPAnalyticsConstant.SEND_DATA, contextData);
    }

    @Override
    public void onClick(View v) {
        if (!isNetworkConnected()) return;
        if (v == mAddToCart) {
            buyProduct(mCTNValue);
        }
        if (v == mBuyFromRetailers) {
            getRetailersInformation();
        }
    }


    @Override
    public void onModelDataLoadFinished(Message msg) {
        HashMap<String, SummaryModel> msgObj = (HashMap<String, SummaryModel>) msg.obj;
        mProductSummary = msgObj.get(mCTNValue);
        populateData();
        if (Utility.isProgressDialogShowing()) {
            Utility.dismissProgressDialog();
        }
        mDetailLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onModelDataError(Message msg) {
        mDetailLayout.setVisibility(View.GONE);
        showErrorDialog(msg);
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    @Override
    public void onGetProductDetail(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            if (Utility.isProgressDialogShowing()) {
                Utility.dismissProgressDialog();
            }
            mDetailLayout.setVisibility(View.GONE);
            setTitleAndBackButtonVisibility(mProductTitle, false);
            showErrorDialog(msg);
        } else {
            if (msg.what == RequestCode.SEARCH_PRODUCT) {
                if (msg.obj instanceof ProductDetailEntity) {
                    mProductDetail = (ProductDetailEntity) msg.obj;
                    mCTNValue = mProductDetail.getCode();
                    fetchProductDetailFromPrx();
                    mDetailLayout.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void populateData() {
        String actualPrice;
        String discountedPrice;
        if (mBundle.containsKey(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL)) {
            if (mProductSummary != null) {
                mProductTitle = mProductSummary.getData().getProductTitle();
                setTitleAndBackButtonVisibility(mProductTitle, false);

                mProductDescription.setText(mProductTitle);
                mCTN.setText(mCTNValue);
                mProductOverview.setText(mProductSummary.getData().getMarketingTextHeader());

                if (mProductDetail != null) {
                    actualPrice = mProductDetail.getPrice().getFormattedValue();
                    discountedPrice = mProductDetail.getDiscountPrice().getFormattedValue();
                    setPrice(actualPrice, discountedPrice);
                } else {
                    mPrice.setVisibility(View.GONE);
                    mProductDiscountedPrice.setVisibility(View.GONE);
                }
            }
        } else {
            actualPrice = mBundle.getString(IAPConstant.PRODUCT_PRICE);
            discountedPrice = mBundle.getString(IAPConstant.IAP_PRODUCT_DISCOUNTED_PRICE);

            mProductDescription.setText(mBundle.getString(IAPConstant.PRODUCT_TITLE));
            mCTN.setText(mBundle.getString(IAPConstant.PRODUCT_CTN));
            mProductOverview.setText(mBundle.getString(IAPConstant.PRODUCT_OVERVIEW));

            if (mLaunchedFromProductCatalog) {
                setPrice(actualPrice, discountedPrice);
            } else {
                mPrice.setVisibility(View.GONE);
                mProductDiscountedPrice.setText(actualPrice);
            }
        }
    }

    private void setPrice(String actualPrice, String discountedPrice) {
        if (!ControllerFactory.getInstance().isPlanB())
            setCartIconVisibility(true);
        mPrice.setText(actualPrice);
        if (discountedPrice == null || discountedPrice.equalsIgnoreCase("")) {
            mProductDiscountedPrice.setVisibility(View.GONE);
            mPrice.setTextColor(Utility.getThemeColor(mContext));
        } else if (actualPrice != null && discountedPrice.equalsIgnoreCase(actualPrice)) {
            mPrice.setVisibility(View.GONE);
            mProductDiscountedPrice.setVisibility(View.VISIBLE);
            mProductDiscountedPrice.setText(discountedPrice);
        } else {
            mProductDiscountedPrice.setVisibility(View.VISIBLE);
            mProductDiscountedPrice.setText(discountedPrice);
            mPrice.setPaintFlags(mPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public void onLoadFinished(ArrayList data) {
        buyFromRetailers(data);
    }


    @Override
    public void onLoadError(Message msg) {
        IAPLog.d(IAPLog.LOG, "onLoadError == ProductDetailFragment ");
        if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg, ((FragmentActivity) mContext).getSupportFragmentManager(), mContext);
        } else {
            NetworkUtility.getInstance().showErrorDialog(mContext, ((FragmentActivity) mContext).getSupportFragmentManager(), mContext.getString(R.string.iap_ok),
                    mContext.getString(R.string.iap_server_error), mContext.getString(R.string.iap_something_went_wrong));
        }
    }

    @Override
    public void onRetailerError(IAPNetworkError errorMsg) {
        NetworkUtility.getInstance().showErrorDialog(mContext,
                getFragmentManager(), mContext.getString(R.string.iap_ok),
                mContext.getString(R.string.iap_retailer_title_for_no_retailers), errorMsg.getMessage());
    }

    @Override
    public void onEventReceived(String event) {
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.IAP_LAUNCH_SHOPPING_CART))) {
            startShoppingCartFragment();
        }
    }

    private void startShoppingCartFragment() {
        addFragment(ShoppingCartFragment.createInstance(new Bundle(), AnimationType.NONE), ShoppingCartFragment.TAG);
    }

    @Override
    public void onDialogOkClick() {
        if (getActivity() != null && getActivity() instanceof IAPActivity) {
            int count = getFragmentManager().getBackStackEntryCount();
            for (int i = 0; i < count; i++) {
                getFragmentManager().popBackStack();
            }
            finishActivity();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    private void showErrorDialog(Message msg) {
        Bundle bundle = new Bundle();
        if (msg.obj instanceof IAPNetworkError) {
            IAPNetworkError error = (IAPNetworkError) msg.obj;
            bundle.putString(IAPConstant.SINGLE_BUTTON_DIALOG_TITLE,
                    NetworkUtility.getInstance().getErrorTitleMessageFromErrorCode(mContext, error.getIAPErrorCode()));
            bundle.putString(IAPConstant.SINGLE_BUTTON_DIALOG_DESCRIPTION,
                    NetworkUtility.getInstance().getErrorDescriptionMessageFromErrorCode(mContext, error));
        } else {
            bundle.putString(IAPConstant.SINGLE_BUTTON_DIALOG_DESCRIPTION, (String) msg.obj);
        }

        bundle.putString(IAPConstant.SINGLE_BUTTON_DIALOG_TEXT, mContext.getString(R.string.iap_ok));

        if (mErrorDialogFragment == null) {
            mErrorDialogFragment = new ErrorDialogFragment();
            mErrorDialogFragment.setErrorDialogListener(this);
            mErrorDialogFragment.setArguments(bundle);
            mErrorDialogFragment.setShowsDialog(false);
        }
        try {
            mErrorDialogFragment.show(getFragmentManager(), "NetworkErrorDialog");
            mErrorDialogFragment.setShowsDialog(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
