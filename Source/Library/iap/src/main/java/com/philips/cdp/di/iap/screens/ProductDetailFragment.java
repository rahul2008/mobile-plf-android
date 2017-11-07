/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.adapters.ImageAdapter;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.cart.AbstractShoppingCartPresenter.ShoppingCartListener;
import com.philips.cdp.di.iap.cart.IAPCartListener;
import com.philips.cdp.di.iap.cart.ShoppingCartAPI;
import com.philips.cdp.di.iap.cart.ShoppingCartData;
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
import com.philips.cdp.di.iap.utils.AlertListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.CountDropDown;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.uid.view.widget.DotNavigationIndicator;
import com.philips.platform.uid.view.widget.ProgressBarButton;
import com.philips.platform.uid.view.widget.UIPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp.di.iap.R.id.delete_btn;
import static com.philips.cdp.di.iap.utils.IAPConstant.IAP_UPDATE_PRODUCT_COUNT;

public class ProductDetailFragment extends InAppBaseFragment implements
        PRXAssetExecutor.AssetListener, View.OnClickListener, EventListener,
        AbstractModel.DataLoadListener, ErrorDialogFragment.ErrorDialogListener,
        ProductDetailController.ProductSearchListener, ShoppingCartListener<ShoppingCartData>, AlertListener {


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
    private ProgressBarButton mAddToCart;
    private ProgressBarButton mBuyFromRetailers;
    private TextView mProductStockInfo;
    private ScrollView mDetailLayout;
    private LinearLayout mCheckutAndCountinue;
    private LinearLayout mQuantityAndDelete;
    private TextView mQuantity;
    private Button mDeleteProduct;

    private ArrayList<String> mAsset;
    private boolean mLaunchedFromProductCatalog = false;
    private String mCTNValue;
    private String mProductTitle;
    private ErrorDialogFragment mErrorDialogFragment;
    private boolean mIsFromVertical;
    private ArrayList<StoreEntity> mUpdtedStoreEntity;

    private IAPCartListener mBuyProductListener = new IAPCartListener() {
        @Override
        public void onSuccess(final int count) {
            dismissProgressDialog();
            tagItemAddedToCart();
            if (mIapListener != null) {
                mIapListener.onUpdateCartCount();
            }
        }

        @Override
        public void onFailure(final Message msg) {
            if (isProgressDialogShowing())
                dismissProgressDialog();

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
                getInstance().getShoppingCartPresenter(mContext, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        EventHelper.getInstance().registerEventNotification(IAPConstant.IAP_LAUNCH_SHOPPING_CART, this);
        EventHelper.getInstance().registerEventNotification(IAPConstant.IAP_UPDATE_PRODUCT_COUNT, this);
        EventHelper.getInstance().registerEventNotification(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED, this);
        EventHelper.getInstance().registerEventNotification(IAPConstant.IAP_DELETE_PRODUCT, this);
        View rootView = inflater.inflate(R.layout.iap_product_details_screen, container, false);
        initializeViews(rootView);
        return rootView;
    }

    void initializeViews(View rootView) {
        mDetailLayout = rootView.findViewById(R.id.scrollView);
        mProductDescription = rootView.findViewById(R.id.iap_productDetailScreen_productDescription_lebel);
        mCTN = rootView.findViewById(R.id.iap_productDetailsScreen_ctn_lebel);
        mPrice = rootView.findViewById(R.id.iap_productDetailsScreen_individualPrice_lebel);
        mProductOverview = rootView.findViewById(R.id.iap_productDetailsScreen_productOverview);
        mAddToCart = rootView.findViewById(R.id.iap_productDetailsScreen_addToCart_button);
        mBuyFromRetailers = rootView.findViewById(R.id.iap_productDetailsScreen_buyFromRetailor_button);
        mCheckutAndCountinue = rootView.findViewById(R.id.iap_productDetailsScreen_btn_ll);
        mQuantityAndDelete = rootView.findViewById(R.id.iap_productDetailsScreen_quantity_delete_btn_ll);
        mProductDiscountedPrice = rootView.findViewById(R.id.iap_productCatalogItem_discountedPrice_lebel);
        mProductStockInfo = rootView.findViewById(R.id.iap_productDetailsScreen_outOfStock_label);
        mDeleteProduct = rootView.findViewById(delete_btn);
        mDeleteProduct.setOnClickListener(this);
        mQuantity = rootView.findViewById(R.id.quantity_val);
        mViewPager = rootView.findViewById(R.id.pager);
        DotNavigationIndicator indicator = rootView.findViewById(R.id.indicator);
        mImageAdapter = new ImageAdapter(mContext, new ArrayList<String>());
        mViewPager.setAdapter(mImageAdapter);
        indicator.setViewPager(mViewPager);

        mBundle = getArguments();
        if (mBundle != null) {
            if (mBundle.containsKey(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL)) {
                mIsFromVertical = true;
                mCTNValue = mBundle.getString(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL);
                if (isNetworkConnected()) {
                    if (!ControllerFactory.getInstance().isPlanB()) {
                        ProductDetailController controller = new ProductDetailController(mContext, this);
                        if (!mBuyFromRetailers.isActivated()) {
                            //showProgressDialog(mContext, getString(R.string.iap_please_wait));
                            mBuyFromRetailers.showProgressIndicator();

                        }
                        controller.getProductDetail(mCTNValue);
                    } else {
                        fetchProductDetailFromPrx();
                        if (mIapListener != null)
                            mIapListener.onSuccess();
                    }
                }
            } else {
                mCTNValue = mBundle.getString(IAPConstant.PRODUCT_CTN);
                mLaunchedFromProductCatalog = mBundle.getBoolean(IAPConstant.IS_PRODUCT_CATALOG, false);
                mProductTitle = mBundle.getString(IAPConstant.PRODUCT_TITLE);
                populateData();
            }
        }
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
            mImageAdapter = new ImageAdapter(mContext, mAsset);
            if (mAsset == null)
                trackErrorTag(IAPAnalyticsConstant.PRX + mCTNValue + "_" + IAPAnalyticsConstant.No_IMAGES_FOUND);
            mViewPager.setAdapter(mImageAdapter);
            mImageAdapter.notifyDataSetChanged();
            if (mBuyFromRetailers.isActivated()) {
                //dismissProgressDialog();
                mBuyFromRetailers.hideProgressIndicator();
            }
        }
    }

    private void makeSummaryRequest() {
        ArrayList<String> ctnList = new ArrayList<>();
        ctnList.add(mCTNValue);
        if (!CartModelContainer.getInstance().isPRXSummaryPresent(mCTNValue)) {
            if (!mBuyFromRetailers.isActivated()) {
                if (mContext == null) return;
                mBuyFromRetailers.showProgressIndicator();
                //showProgressDialog(mContext, getString(R.string.iap_please_wait));
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
                    mProductStockInfo.setVisibility(View.VISIBLE);
                    mCheckutAndCountinue.setVisibility(View.VISIBLE);
                    mQuantityAndDelete.setVisibility(View.GONE);
                } else {
                    mQuantityAndDelete.setVisibility(View.VISIBLE);
                    IAPAnalytics.trackPage(IAPAnalyticsConstant.SHOPPING_CART_ITEM_DETAIL_PAGE_NAME);
                    setCartIconVisibility(false);
                    int quantity = getArguments().getInt(IAPConstant.PRODUCT_QUANTITY);
                    if (quantity != 0) {
                        setCountArrow(mContext, true);
                        mCheckutAndCountinue.setVisibility(View.GONE);
                        mQuantityAndDelete.setVisibility(View.VISIBLE);
                        //checkForOutOfStock(cartData.getStockLevel(), cartData.getQuantity());
                        mQuantity.setCompoundDrawables(null, null, countArrow, null);

                        mQuantity.setText(quantity + "");
                        int stock = getArguments().getInt(IAPConstant.PRODUCT_STOCK);
                        bindCountView(mQuantity, stock, quantity);
                    } else {
                        mQuantityAndDelete.setVisibility(View.GONE);
                    }
                }
            } else {
                handleViews();
            }
        }
        makeAssetRequest();
        setTitleAndBackButtonVisibility(R.string.iap_product_detail_title, true);
    }

    private Drawable countArrow;
    private int mQuantityStatus;
    private int mNewCount;
    private UIPicker mPopupWindow;

    private void checkForOutOfStock(int pStockLevel, int pQuantity) {
        if (pStockLevel == 0) {

        } else if (pStockLevel < pQuantity) {

        } else {

        }
    }

    private void setCountArrow(final Context context, final boolean isEnable) {
        if (isEnable) {
            countArrow = context.getDrawable(R.drawable.iap_product_count_drop_down);
            countArrow.setColorFilter(new
                    PorterDuffColorFilter(mContext.getResources().getColor(R.color.uid_quiet_button_icon_selector), PorterDuff.Mode.MULTIPLY));
        } else {
            countArrow = VectorDrawable.create(context, R.drawable.iap_product_disable_count_drop_down);
        }
        int width = (int) mContext.getResources().getDimension(R.dimen.iap_count_drop_down_icon_width);
        int height = (int) mContext.getResources().getDimension(R.dimen.iap_count_drop_down_icon_height);
        countArrow.setBounds(0, 0, width, height);
    }

    private void bindCountView(final View view, final int stockLevel, final int quantity) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // final ShoppingCartData data = mData.get(position);

                CountDropDown countPopUp = new CountDropDown(v,v.getContext(), stockLevel, quantity, new CountDropDown.CountUpdateListener() {
                    @Override
                    public void countUpdate(final int oldCount, final int newCount) {
                        // mSelectedItemPosition = position;
                        mQuantityStatus = getQuantityStatus(newCount, oldCount);
                        mNewCount = newCount;
                        EventHelper.getInstance().notifyEventOccurred(IAP_UPDATE_PRODUCT_COUNT);
                    }
                });
                countPopUp.createPopUp();
                mPopupWindow = countPopUp.getPopUpWindow();
                countPopUp.show();
            }
        });
    }

    public int getNewCount() {
        return mNewCount;
    }

    private int getQuantityStatus(int newCount, int oldCount) {
        if (newCount > oldCount)
            return 1;
        else if (newCount < oldCount)
            return 0;
        else
            return -1;
    }

    public int getQuantityStatusInfo() {
        return mQuantityStatus;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.IAP_LAUNCH_SHOPPING_CART, this);
        EventHelper.getInstance().unregisterEventNotification(IAP_UPDATE_PRODUCT_COUNT, this);
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.IAP_DELETE_PRODUCT, this);
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED, this);
        if (mErrorDialogFragment != null)
            mErrorDialogFragment.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mErrorDialogFragment != null)
            mErrorDialogFragment.onDestroy();
    }

    private void handleViews() {
        if (ControllerFactory.getInstance().isPlanB()) {
            //  mBuyFromRetailers.setText(R.string.iap_buy_now);
            mAddToCart.setVisibility(View.GONE);
            setCartIconVisibility(false);
        } else {
            //  mBuyFromRetailers.setText(R.string.iap_buy_from_retailers);
            mAddToCart.setVisibility(View.VISIBLE);
            mAddToCart.setOnClickListener(this);
            // Drawable shoppingCartIcon = VectorDrawable.create(mContext, R.drawable.iap_shopping_cart);
            //mAddToCart.setCompoundDrawablesWithIntrinsicBounds(shoppingCartIcon, null, null, null);

            setCartIconVisibility(true);
            mShoppingCartAPI.getProductCartCount(mContext, mProductCountListener);
        }
        mBuyFromRetailers.setOnClickListener(this);
        mBuyFromRetailers.setVisibility(View.VISIBLE);
    }

    private void getRetailersInformation() {
        ShoppingCartAPI presenter = ControllerFactory.
                getInstance().getShoppingCartPresenter(mContext, this);

        if (!mBuyFromRetailers.isActivated()) {
            //showProgressDialog(mContext, getString(R.string.iap_please_wait));
            mBuyFromRetailers.showProgressIndicator();
            presenter.getRetailersInformation(mCTNValue);
        }
    }

    private void buyFromRetailers(ArrayList<StoreEntity> storeEntities) {
        if (!isNetworkConnected()) return;
        Bundle bundle = new Bundle();
        final ArrayList<StoreEntity> removedBlacklistedRetailers = removedBlacklistedRetailers(storeEntities);
        if (removedBlacklistedRetailers.size() == 1 && (removedBlacklistedRetailers.get(0).getIsPhilipsStore().equalsIgnoreCase("Y"))) {
            bundle.putString(IAPConstant.IAP_BUY_URL, storeEntities.get(0).getBuyURL());
            bundle.putString(IAPConstant.IAP_STORE_NAME, storeEntities.get(0).getName());
            addFragment(WebBuyFromRetailers.createInstance(bundle, AnimationType.NONE), WebBuyFromRetailers.TAG);
        } else {

            bundle.putStringArrayList(IAPConstant.IAP_IGNORE_RETAILER_LIST, getArguments().getStringArrayList(IAPConstant.IAP_IGNORE_RETAILER_LIST));
            bundle.putSerializable(IAPConstant.IAP_RETAILER_INFO, removedBlacklistedRetailers);
            if (removedBlacklistedRetailers.isEmpty()) {
                onRetailerError(NetworkUtility.getInstance().
                        createIAPErrorMessage("", mContext.getString(R.string.iap_no_retailer_message)));
            } else {
                addFragment(BuyFromRetailersFragment.createInstance(bundle, AnimationType.NONE),
                        BuyFromRetailersFragment.TAG);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onFetchAssetSuccess(final Message msg) {
        if (mContext == null) return;
        IAPLog.d(IAPConstant.PRODUCT_DETAIL_FRAGMENT, "Success");
        mAsset = (ArrayList<String>) msg.obj;
        CartModelContainer.getInstance().addProductAsset(mCTNValue, mAsset);
        mImageAdapter = new ImageAdapter(mContext, mAsset);
        mViewPager.setAdapter(mImageAdapter);
        mImageAdapter.notifyDataSetChanged();
        if (mIapListener != null)
            mIapListener.onSuccess();
        if (mBuyFromRetailers.isActivated())
            dismissProgressDialog();
    }

    @Override
    public void onFetchAssetFailure(final Message msg) {
        IAPLog.d(IAPConstant.PRODUCT_DETAIL_FRAGMENT, "Failure");
        if (mBuyFromRetailers.isActivated())
            // dismissProgressDialog();
            mBuyFromRetailers.hideProgressIndicator();
        if (!isNetworkConnected()) return;
        NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);

        if (msg.obj instanceof IAPNetworkError) {
            IAPNetworkError obj = (IAPNetworkError) msg.obj;
            mIapListener.onFailure(obj.getIAPErrorCode());
        }

    }

    void buyProduct(final String ctnNumber) {
        showProgressDialog(mContext, getString(R.string.iap_please_wait));
        mShoppingCartAPI.buyProduct(mContext, ctnNumber, mBuyProductListener);
    }

    private void tagItemAddedToCart() {
        HashMap<String, String> contextData = new HashMap<>();
        contextData.put(IAPAnalyticsConstant.ORIGINAL_PRICE, mPrice.getText().toString());
        if (mProductDiscountedPrice.getVisibility() == View.VISIBLE)
            contextData.put(IAPAnalyticsConstant.DISCOUNTED_PRICE, mProductDiscountedPrice.getText().toString());
        if (mProductStockInfo.getVisibility() == View.VISIBLE && mProductDetail != null)
            contextData.put(IAPAnalyticsConstant.OUT_OF_STOCK, mProductDetail.getStock().getStockLevelStatus());
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
        if (v == mDeleteProduct) {
            deleteProduct();
        }
    }

    private void deleteProduct() {
        Utility.showActionDialog(mContext, getString(R.string.iap_remove_product), getString(R.string.iap_cancel)
                , null, getString(R.string.iap_product_remove_description), getFragmentManager(), this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onModelDataLoadFinished(Message msg) {
        HashMap<String, SummaryModel> msgObj = (HashMap<String, SummaryModel>) msg.obj;
        mProductSummary = msgObj.get(mCTNValue);
        populateData();
        if (mBuyFromRetailers.isActivated()) {
            //dismissProgressDialog();
            mBuyFromRetailers.hideProgressIndicator();
        }
        mDetailLayout.setVisibility(View.VISIBLE);
        if (mIapListener != null)
            mIapListener.onSuccess();
    }

    @Override
    public void onModelDataError(Message msg) {
        mDetailLayout.setVisibility(View.GONE);
        mBuyFromRetailers.setVisibility(View.GONE);
        showErrorDialog(msg);
        //if (isProgressDialogShowing())
        if (mBuyFromRetailers.isActivated())
            //dismissProgressDialog();
            mBuyFromRetailers.hideProgressIndicator();

        if (msg.obj instanceof IAPNetworkError) {
            IAPNetworkError obj = (IAPNetworkError) msg.obj;
            mIapListener.onFailure(obj.getIAPErrorCode());
        }
    }

    @Override
    public void onGetProductDetail(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            if (isProgressDialogShowing()) {
                //dismissProgressDialog();
                mBuyFromRetailers.hideProgressIndicator();
            }
            mDetailLayout.setVisibility(View.GONE);
            //Hard coded strring provided because we dont have
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
        String stockStatus;
        if (mBundle.containsKey(IAPConstant.IAP_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL)) {
            if (mProductSummary != null) {
                mProductTitle = mProductSummary.getData().getProductTitle();
                if (mProductTitle == null) {
                    trackErrorTag(IAPAnalyticsConstant.PRX + mCTNValue + "_" + IAPAnalyticsConstant.PRODUCT_TITLE_MISSING);
                }
                mProductDescription.setText(mProductTitle);
                mCTN.setText(mCTNValue);
                mProductOverview.setText(mProductSummary.getData().getMarketingTextHeader());
                trackErrorTag(IAPAnalyticsConstant.PRX + mCTNValue + "_" + IAPAnalyticsConstant.PRODUCT_DESCRIPTION_MISSING);
                if (mProductDetail != null) {
                    actualPrice = mProductDetail.getPrice().getFormattedValue();
                    discountedPrice = mProductDetail.getDiscountPrice().getFormattedValue();
                    stockStatus = mProductDetail.getStock().getStockLevelStatus();
                    setPrice(actualPrice, discountedPrice);
                    setStockInfo(stockStatus);
                } else {
                    mPrice.setVisibility(View.GONE);
                    mProductDiscountedPrice.setVisibility(View.GONE);
                    mProductStockInfo.setVisibility(View.GONE);
                }
            }
        } else {
            actualPrice = mBundle.getString(IAPConstant.PRODUCT_PRICE);
            discountedPrice = mBundle.getString(IAPConstant.IAP_PRODUCT_DISCOUNTED_PRICE);
            stockStatus = mBundle.getString(IAPConstant.STOCK_STATUS);

            mProductDescription.setText(mBundle.getString(IAPConstant.PRODUCT_TITLE));
            mCTN.setText(mBundle.getString(IAPConstant.PRODUCT_CTN));
            mProductOverview.setText(mBundle.getString(IAPConstant.PRODUCT_OVERVIEW));

            if (mBundle.getString(IAPConstant.PRODUCT_TITLE) == null) {
                trackErrorTag(IAPAnalyticsConstant.PRX + mCTNValue + "_" + IAPAnalyticsConstant.PRODUCT_TITLE_MISSING);
            }
            if (mBundle.getString(IAPConstant.PRODUCT_OVERVIEW) == null) {
                trackErrorTag(IAPAnalyticsConstant.PRX + mCTNValue + "_" + IAPAnalyticsConstant.PRODUCT_DESCRIPTION_MISSING);
            }

            if (mLaunchedFromProductCatalog) {
                setPrice(actualPrice, discountedPrice);
                setStockInfo(stockStatus);
            } else {
                mPrice.setVisibility(View.GONE);
                mProductDiscountedPrice.setText(actualPrice);
            }
        }
    }

    private void setStockInfo(String stockInfo) {
        // String stockLevel = mProductDetail.getStock().getStockLevelStatus();
        if (stockInfo != null && stockInfo.equalsIgnoreCase("outOfStock")) {
            mProductStockInfo.setText(mContext.getString(R.string.iap_out_of_stock));
            mProductStockInfo.setTextColor(ContextCompat.getColor(mContext, R.color.uid_signal_red_level_60));
        } else {
            mProductStockInfo.setVisibility(View.GONE);
        }
    }

    private void trackErrorTag(String value) {
        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.ERROR, value);
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

//    @Override
//    public void onLoadFinished(ArrayList<StoreEntity> data) {
////        dismissProgressDialog();
//        mBuyFromRetailers.hideProgressIndicator();
//        buyFromRetailers(data);
//    }

    @Override
    public void onLoadFinished(ArrayList<? extends Object> data) {
        dismissProgressDialog();


        if (data != null && data.get(0) instanceof ShoppingCartData) {

            final ShoppingCartData shoppingCartData = getShoppingCartDataFromCTN(data);
            if (shoppingCartData != null) {
                mQuantity.setText(shoppingCartData.getQuantity() + "");
            } else {
                getFragmentManager().popBackStack();
            }

        }
        if (!data.isEmpty() && data.get(0) instanceof StoreEntity) {
            mBuyFromRetailers.hideProgressIndicator();
            buyFromRetailers((ArrayList<StoreEntity>) data);
        }
    }

    private ShoppingCartData getShoppingCartDataFromCTN(ArrayList<?> data) {

        final String lCtn = mBundle.getString(IAPConstant.PRODUCT_CTN);

        for (ShoppingCartData shoppingCartData : (ArrayList<ShoppingCartData>) data) {

            if (shoppingCartData.getCtnNumber().equalsIgnoreCase(lCtn)) return shoppingCartData;
        }

        return null;
    }


//    @Override
//    public void onLoadFinished(ShoppingCartData data) {
//        dismissProgressDialog();
//        IAPLog.d(IAPLog.LOG, data.getCtnNumber());
//    }

    @Override
    public void onLoadError(Message msg) {
        IAPLog.d(IAPLog.LOG, "onLoadError == ProductDetailFragment ");
//        dismissProgressDialog();
        mBuyFromRetailers.hideProgressIndicator();
        if (msg.obj instanceof IAPNetworkError) {
            NetworkUtility.getInstance().showErrorMessage(msg, ((FragmentActivity) mContext).getSupportFragmentManager(), mContext);
        } else {
            NetworkUtility.getInstance().showErrorDialog(mContext, ((FragmentActivity) mContext).getSupportFragmentManager(), mContext.getString(R.string.iap_ok),
                    mContext.getString(R.string.iap_server_error), mContext.getString(R.string.iap_something_went_wrong));
        }
    }

    @Override
    public void onRetailerError(IAPNetworkError errorMsg) {
        //   dismissProgressDialog();
        mBuyFromRetailers.hideProgressIndicator();
        NetworkUtility.getInstance().showErrorDialog(mContext,
                getFragmentManager(), mContext.getString(R.string.iap_ok),
                mContext.getString(R.string.iap_retailer_title_for_no_retailers), errorMsg.getMessage());
    }

    @Override
    public void onEventReceived(String event) {
        if (event.equalsIgnoreCase(String.valueOf(IAPConstant.IAP_LAUNCH_SHOPPING_CART))) {
            startShoppingCartFragment();
        } else if (event.equalsIgnoreCase(IAP_UPDATE_PRODUCT_COUNT)) {
            if (!isProgressDialogShowing()) {
                showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
                ShoppingCartData shoppingCartData = (ShoppingCartData) mBundle.getSerializable(IAPConstant.SHOPPING_CART_CODE);
                mShoppingCartAPI.updateProductQuantity(shoppingCartData, getNewCount(), getQuantityStatusInfo());
            }
        } else if (event.equalsIgnoreCase(IAPConstant.IAP_DELETE_PRODUCT)) {
            if (!isProgressDialogShowing()) {
                showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
                ShoppingCartData shoppingCartData = (ShoppingCartData) mBundle.getSerializable(IAPConstant.SHOPPING_CART_CODE);
                mShoppingCartAPI.deleteProduct(shoppingCartData);
            }
        } else if (event.equalsIgnoreCase(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED)) {
            dismissProgressDialog();
            addFragment(EmptyCartFragment.createInstance(new Bundle(), AnimationType.NONE), EmptyCartFragment.TAG);
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
            IAPLog.e(IAPLog.LOG, e.getMessage());
        }
    }

    @Override
    public boolean handleBackEvent() {
        if (mIsFromVertical) {
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
        return super.handleBackEvent();
    }

    private ArrayList<StoreEntity> removedBlacklistedRetailers(ArrayList<StoreEntity> pStoreEntity) {
        final ArrayList<String> list = getArguments().getStringArrayList(IAPConstant.IAP_IGNORE_RETAILER_LIST);
        mUpdtedStoreEntity = new ArrayList<>();
        mUpdtedStoreEntity.addAll(pStoreEntity);
        for (StoreEntity storeEntity : pStoreEntity) {
            final String retailerName = storeEntity.getName().replaceAll("\\s+", "");
            for (int i = 0; i < (list != null ? list.size() : 0); i++) {
                if (Utility.indexOfSubString(true, retailerName, list.get(i)) >= 0) {
                    mUpdtedStoreEntity.remove(storeEntity);
                }
            }
        }
        return mUpdtedStoreEntity;
    }


    @Override
    public void onPositiveBtnClick() {
        EventHelper.getInstance().notifyEventOccurred(IAPConstant.IAP_DELETE_PRODUCT);
    }

    @Override
    public void onNegativeBtnClick() {

    }
}
